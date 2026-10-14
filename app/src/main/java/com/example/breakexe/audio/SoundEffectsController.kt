package com.example.breakexe.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundEffectsController {
    var enabled: Boolean = true
    private val scope = CoroutineScope(Dispatchers.Default)

    fun playPop() {
        if (!enabled) return
        playFrequencies(doubleArrayOf(520.0, 780.0), noteDuration = 0.04, amplitude = 4000)
    }

    fun playStartChime() {
        if (!enabled) return
        // Gentle pentatonic kalimba tones (C5, D5, G5, A5)
        playFrequencies(doubleArrayOf(523.25, 587.33, 783.99, 880.0), noteDuration = 0.09, amplitude = 5500)
    }

    fun playEndChime() {
        if (!enabled) return
        // Welcoming chord (G5, E5, C5, F5)
        playFrequencies(doubleArrayOf(783.99, 659.25, 523.25, 698.46), noteDuration = 0.10, amplitude = 6000)
    }

    fun playEndingSoonReminder() {
        if (!enabled) return
        // Two soft gentle glass-bell chimes (E5, B5)
        playFrequencies(doubleArrayOf(659.25, 987.77), noteDuration = 0.16, amplitude = 4500)
    }

    fun playGiggle() {
        if (!enabled) return
        // Cute playful squeak with frequency sweep
        playFrequencies(doubleArrayOf(580.0, 880.0, 720.0, 1020.0), noteDuration = 0.05, amplitude = 4800)
    }

    private fun playFrequencies(frequencies: DoubleArray, noteDuration: Double, amplitude: Int) {
        scope.launch {
            try {
                val sampleRate = 44100
                val samplesPerNote = (noteDuration * sampleRate).toInt()
                val totalSamples = samplesPerNote * frequencies.size
                val buffer = ShortArray(totalSamples)

                var offset = 0
                for (freq in frequencies) {
                    for (i in 0 until samplesPerNote) {
                        val time = i.toDouble() / sampleRate
                        val envelope = 1.0 - (i.toDouble() / samplesPerNote)
                        val sinVal = sin(2.0 * Math.PI * freq * time)
                        buffer[offset + i] = (sinVal * envelope * amplitude).toInt().toShort()
                    }
                    offset += samplesPerNote
                }

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
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

                delay((noteDuration * frequencies.size * 1000).toLong() + 100)
                try {
                    track.stop()
                    track.release()
                } catch (e: Exception) {
                    // ignore
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }
}
