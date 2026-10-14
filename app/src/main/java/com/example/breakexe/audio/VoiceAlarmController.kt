package com.example.breakexe.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class VoiceAlarmController(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private val scope = CoroutineScope(Dispatchers.Default)
    private var alarmJob: Job? = null

    private val _isRinging = MutableStateFlow(false)
    val isRinging: StateFlow<Boolean> = _isRinging.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isTtsInitialized = true
            // Try Hindi voice first, fallback to Indian English or default
            val hindi = Locale("hi", "IN")
            val enIn = Locale("en", "IN")
            val res = tts?.setLanguage(hindi)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                val resEn = tts?.setLanguage(enIn)
                if (resEn == TextToSpeech.LANG_MISSING_DATA || resEn == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.getDefault()
                }
            }
            tts?.setPitch(1.3f)
            tts?.setSpeechRate(1.05f)

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {}
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {}
            })
        }
    }

    fun start() {
        if (_isRinging.value) return
        _isRinging.value = true

        alarmJob?.cancel()
        alarmJob = scope.launch {
            while (isActive && _isRinging.value) {
                // 1. Play soft gentle 3-note chime (C5, E5, G5)
                playPastelChime()
                triggerHaptic()

                // 2. Speak phrase
                speakAlarmPhrase()

                // 3. Short pleasant pause between repetitions
                delay(2200L)
            }
        }
    }

    fun stop() {
        _isRinging.value = false
        alarmJob?.cancel()
        alarmJob = null
        try {
            tts?.stop()
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun speakAlarmPhrase() {
        if (isTtsInitialized) {
            try {
                val text = "Break khatam ho gaya! Break khatam ho gaya!"
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "break_alarm_id")
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    private fun playPastelChime() {
        try {
            val sampleRate = 44100
            val frequencies = doubleArrayOf(523.25, 659.25, 783.99) // C5, E5, G5
            val durationPerNote = 0.12
            val numSamplesPerNote = (durationPerNote * sampleRate).toInt()
            val totalSamples = numSamplesPerNote * frequencies.size
            val buffer = ShortArray(totalSamples)

            var offset = 0
            for (freq in frequencies) {
                for (i in 0 until numSamplesPerNote) {
                    val time = i.toDouble() / sampleRate
                    // Apply exponential-like decay envelope
                    val envelope = 1.0 - (i.toDouble() / numSamplesPerNote)
                    val sinVal = sin(2.0 * Math.PI * freq * time)
                    buffer[offset + i] = (sinVal * envelope * 6000).toInt().toShort()
                }
                offset += numSamplesPerNote
            }

            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            // release after finish
            scope.launch {
                delay(500)
                try {
                    track.stop()
                    track.release()
                } catch (e: Exception) {
                    // ignore
                }
            }
        } catch (e: Exception) {
            // audio track fallback
        }
    }

    private fun triggerHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val effect = VibrationEffect.createWaveform(longArrayOf(0, 150, 100, 150), -1)
                vibratorManager?.vibrate(CombinedVibration.createParallel(effect))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 150, 100, 150), -1))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(longArrayOf(0, 150, 100, 150), -1)
                }
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    fun release() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
