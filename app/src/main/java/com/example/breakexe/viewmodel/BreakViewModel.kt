package com.example.breakexe.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.breakexe.audio.SoundEffectsController
import com.example.breakexe.audio.VoiceAlarmController
import com.example.breakexe.data.BreakPreferencesRepository
import com.example.breakexe.model.ActiveBreak
import com.example.breakexe.model.BreakQuotes
import com.example.breakexe.model.BreakReason
import com.example.breakexe.model.BreakRecord
import com.example.breakexe.model.CharacterConfig
import com.example.breakexe.model.TodayStats
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class BreakViewModel(application: Application) : AndroidViewModel(application) {
    val repository = BreakPreferencesRepository(application)
    val soundEffects = SoundEffectsController()
    val voiceAlarm = VoiceAlarmController(application)

    private val _activeBreak = MutableStateFlow<ActiveBreak?>(repository.loadActiveBreak())
    val activeBreak: StateFlow<ActiveBreak?> = _activeBreak.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(0L)
    val remainingSeconds: StateFlow<Long> = _remainingSeconds.asStateFlow()

    private val _overdueSeconds = MutableStateFlow(0L)
    val overdueSeconds: StateFlow<Long> = _overdueSeconds.asStateFlow()

    private val _isOverdue = MutableStateFlow(false)
    val isOverdue: StateFlow<Boolean> = _isOverdue.asStateFlow()

    private val _targetMinutes = MutableStateFlow(_activeBreak.value?.targetMinutes ?: 13)
    val targetMinutes: StateFlow<Int> = _targetMinutes.asStateFlow()

    private val _selectedReason = MutableStateFlow(_activeBreak.value?.reason ?: BreakReason.PHONE)
    val selectedReason: StateFlow<BreakReason> = _selectedReason.asStateFlow()

    private val _explanationNote = MutableStateFlow(_activeBreak.value?.note ?: "")
    val explanationNote: StateFlow<String> = _explanationNote.asStateFlow()

    private val _characterConfig = MutableStateFlow(repository.loadCharacter())
    val characterConfig: StateFlow<CharacterConfig> = _characterConfig.asStateFlow()

    private val _history = MutableStateFlow(repository.loadHistory())
    val history: StateFlow<List<BreakRecord>> = _history.asStateFlow()

    private val _stats = MutableStateFlow(repository.calculateTodayStats(_history.value))
    val stats: StateFlow<TodayStats> = _stats.asStateFlow()

    private val _soundEnabled = MutableStateFlow(repository.isSoundEnabled())
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    val isAlarmRinging: StateFlow<Boolean> = voiceAlarm.isRinging

    private val _welcomeBackToast = MutableStateFlow<String?>(null)
    val welcomeBackToast: StateFlow<String?> = _welcomeBackToast.asStateFlow()

    private val _celebrationToast = MutableStateFlow<String?>(null)
    val celebrationToast: StateFlow<String?> = _celebrationToast.asStateFlow()

    private val _endingSoonNotice = MutableStateFlow<String?>(null)
    val endingSoonNotice: StateFlow<String?> = _endingSoonNotice.asStateFlow()

    private var timerJob: Job? = null

    init {
        soundEffects.enabled = _soundEnabled.value

        // Check if loaded break was already overdue on launch
        val initial = _activeBreak.value
        if (initial != null && initial.targetMinutes > 0) {
            val targetEnd = initial.targetEndTime ?: (initial.startTime + initial.targetMinutes * 60 * 1000L)
            val now = System.currentTimeMillis()
            if (now >= targetEnd && !initial.alarmDismissed) {
                voiceAlarm.start()
            }
        }

        startTimerLoop()
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val current = _activeBreak.value
                if (current != null) {
                    val now = System.currentTimeMillis()
                    val totalElapsed = maxOf(0L, (now - current.startTime) / 1000L)
                    _elapsedSeconds.value = totalElapsed

                    if (current.targetMinutes > 0) {
                        val targetEnd = current.targetEndTime ?: (current.startTime + current.targetMinutes * 60 * 1000L)
                        val diffToTarget = (targetEnd - now) / 1000L

                        if (diffToTarget > 0) {
                            _remainingSeconds.value = diffToTarget
                            _isOverdue.value = false
                            _overdueSeconds.value = 0L

                            // 30s ending soon notice
                            if (diffToTarget <= 30 && !current.notifiedAboutToEnd) {
                                soundEffects.playEndingSoonReminder()
                                _endingSoonNotice.value = "break is wrapping up in ~30s... get ready ☕"
                                val updated = current.copy(notifiedAboutToEnd = true)
                                _activeBreak.value = updated
                                repository.saveActiveBreak(updated)

                                viewModelScope.launch {
                                    delay(5000)
                                    _endingSoonNotice.value = null
                                }
                            }
                        } else {
                            // Target reached or overdue!
                            _remainingSeconds.value = 0L
                            _isOverdue.value = true
                            _overdueSeconds.value = kotlin.math.abs(diffToTarget)

                            if (!current.alarmDismissed && !voiceAlarm.isRinging.value) {
                                voiceAlarm.start()
                            }
                        }
                    } else {
                        // Free vibe
                        _remainingSeconds.value = 0L
                        _isOverdue.value = false
                        _overdueSeconds.value = 0L
                    }
                } else {
                    _elapsedSeconds.value = 0L
                    _remainingSeconds.value = 0L
                    _overdueSeconds.value = 0L
                    _isOverdue.value = false
                    if (voiceAlarm.isRinging.value) {
                        voiceAlarm.stop()
                    }
                }

                delay(1000L)
            }
        }
    }

    fun startBreak() {
        soundEffects.playStartChime()
        val now = System.currentTimeMillis()
        val targetMins = _targetMinutes.value
        val targetEnd = if (targetMins > 0) now + targetMins * 60 * 1000L else null

        val newBreak = ActiveBreak(
            startTime = now,
            targetEndTime = targetEnd,
            reason = _selectedReason.value,
            note = _explanationNote.value.trim(),
            targetMinutes = targetMins,
            notifiedAboutToEnd = false,
            alarmDismissed = false
        )
        _activeBreak.value = newBreak
        repository.saveActiveBreak(newBreak)
    }

    fun endBreak() {
        val current = _activeBreak.value ?: return

        if (voiceAlarm.isRinging.value) {
            voiceAlarm.stop()
        }
        soundEffects.playEndChime()

        val now = System.currentTimeMillis()
        val duration = maxOf(1L, (now - current.startTime) / 1000L)
        val defaultNote = current.reason.humorFallback
        val finalNote = current.note.ifBlank { _explanationNote.value.trim().ifBlank { defaultNote } }

        val newRecord = BreakRecord(
            id = "break-" + now + "-" + UUID.randomUUID().toString().take(4),
            startTime = current.startTime,
            endTime = now,
            durationSeconds = duration,
            reason = current.reason,
            note = finalNote,
            dateKey = repository.getTodayKey()
        )

        val updatedHistory = listOf(newRecord) + _history.value
        _history.value = updatedHistory
        repository.saveHistory(updatedHistory)
        _stats.value = repository.calculateTodayStats(updatedHistory)

        _activeBreak.value = null
        repository.saveActiveBreak(null)
        _explanationNote.value = ""
        _endingSoonNotice.value = null
        _isOverdue.value = false

        // Show celebration toast
        val celebration = BreakQuotes.CELEBRATIONS.random()
        _celebrationToast.value = celebration
        viewModelScope.launch {
            delay(4500)
            _celebrationToast.value = null
        }
    }

    fun dismissAlarm() {
        voiceAlarm.stop()
        val current = _activeBreak.value
        if (current != null) {
            val updated = current.copy(alarmDismissed = true)
            _activeBreak.value = updated
            repository.saveActiveBreak(updated)
        }

        // Show cute welcome back bestie toast
        _welcomeBackToast.value = "finally, welcome back bestie 🌸"
        viewModelScope.launch {
            delay(5500)
            _welcomeBackToast.value = null
        }
    }

    fun onGlobalTap() {
        if (voiceAlarm.isRinging.value) {
            dismissAlarm()
        }
    }

    fun setTargetMinutes(mins: Int) {
        _targetMinutes.value = mins
    }

    fun setSelectedReason(reason: BreakReason) {
        _selectedReason.value = reason
    }

    fun setExplanationNote(note: String) {
        _explanationNote.value = note
    }

    fun saveCharacterConfig(config: CharacterConfig) {
        _characterConfig.value = config
        repository.saveCharacter(config)
    }

    fun deleteRecord(id: String) {
        val updated = _history.value.filter { it.id != id }
        _history.value = updated
        repository.saveHistory(updated)
        _stats.value = repository.calculateTodayStats(updated)
    }

    fun clearAllHistory() {
        _history.value = emptyList()
        repository.saveHistory(emptyList())
        _stats.value = TodayStats()
    }

    fun toggleSound(enabled: Boolean) {
        _soundEnabled.value = enabled
        soundEffects.enabled = enabled
        repository.setSoundEnabled(enabled)
        if (enabled) {
            soundEffects.playStartChime()
        }
    }

    fun testSound(type: String) {
        when (type) {
            "pop" -> soundEffects.playPop()
            "start" -> soundEffects.playStartChime()
            "soon" -> soundEffects.playEndingSoonReminder()
            "end" -> soundEffects.playEndChime()
            "giggle" -> soundEffects.playGiggle()
            "voice" -> {
                if (voiceAlarm.isRinging.value) {
                    voiceAlarm.stop()
                } else {
                    voiceAlarm.start()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        voiceAlarm.release()
    }
}
