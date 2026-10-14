package com.example.breakexe.data

import android.content.Context
import android.content.SharedPreferences
import com.example.breakexe.model.ActiveBreak
import com.example.breakexe.model.BreakRecord
import com.example.breakexe.model.CharacterConfig
import com.example.breakexe.model.CharacterPalettes
import com.example.breakexe.model.TodayStats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BreakPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("break_exe_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getTodayKey(): String {
        return dateFormat.format(Date())
    }

    fun loadActiveBreak(): ActiveBreak? {
        val json = prefs.getString(KEY_ACTIVE_BREAK, null) ?: return null
        return try {
            gson.fromJson(json, ActiveBreak::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun saveActiveBreak(activeBreak: ActiveBreak?) {
        if (activeBreak == null) {
            prefs.edit().remove(KEY_ACTIVE_BREAK).apply()
        } else {
            val json = gson.toJson(activeBreak)
            prefs.edit().putString(KEY_ACTIVE_BREAK, json).apply()
        }
    }

    fun loadHistory(): List<BreakRecord> {
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<BreakRecord>>() {}.type
            gson.fromJson<List<BreakRecord>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveHistory(history: List<BreakRecord>) {
        val json = gson.toJson(history)
        prefs.edit().putString(KEY_HISTORY, json).apply()
    }

    fun calculateTodayStats(history: List<BreakRecord>): TodayStats {
        val today = getTodayKey()
        val todayRecords = history.filter { it.dateKey == today }
        if (todayRecords.isEmpty()) {
            return TodayStats()
        }
        val totalSecs = todayRecords.sumOf { it.durationSeconds }
        val longest = todayRecords.maxOf { it.durationSeconds }
        val avg = totalSecs / todayRecords.size
        return TodayStats(
            totalSeconds = totalSecs,
            count = todayRecords.size,
            longestSeconds = longest,
            averageSeconds = avg
        )
    }

    fun loadCharacter(): CharacterConfig {
        val json = prefs.getString(KEY_CHARACTER, null) ?: return CharacterPalettes.PRESETS.first()
        return try {
            gson.fromJson(json, CharacterConfig::class.java) ?: CharacterPalettes.PRESETS.first()
        } catch (e: Exception) {
            CharacterPalettes.PRESETS.first()
        }
    }

    fun saveCharacter(config: CharacterConfig) {
        val json = gson.toJson(config)
        prefs.edit().putString(KEY_CHARACTER, json).apply()
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean(KEY_SOUND_ENABLED, true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }

    companion object {
        private const val KEY_ACTIVE_BREAK = "key_active_break_v1"
        private const val KEY_HISTORY = "key_history_v1"
        private const val KEY_CHARACTER = "key_character_v1"
        private const val KEY_SOUND_ENABLED = "key_sound_enabled_v1"

        fun formatDurationBadge(seconds: Long): String {
            if (seconds < 60) {
                return "${seconds}s"
            }
            val mins = seconds / 60
            val hours = mins / 60
            val remMins = mins % 60
            return if (hours > 0) {
                if (remMins > 0) "${hours}h ${remMins}m" else "${hours}h"
            } else {
                "${mins}m"
            }
        }

        fun formatStatsTime(seconds: Long): String {
            if (seconds <= 0) return "0m"
            val hours = seconds / 3600
            val mins = (seconds % 3600) / 60
            return if (hours > 0) {
                "${hours}h ${mins}m"
            } else {
                "${mins}m"
            }
        }

        fun formatTimerDigits(totalSec: Long): String {
            val hours = totalSec / 3600
            val mins = (totalSec % 3600) / 60
            val secs = totalSec % 60
            return if (hours > 0) {
                String.format(Locale.US, "%02d:%02d:%02d", hours, mins, secs)
            } else {
                String.format(Locale.US, "%02d:%02d", mins, secs)
            }
        }

        fun formatTimeString(timestamp: Long): String {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
