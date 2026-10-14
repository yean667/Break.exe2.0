/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

// Voice Alarm Controller with SpeechSynthesis and Web Audio API fallback
// Plays cute, playful announcement: "Break khatam ho gaya! Break khatam ho gaya!"
// Repeats continuously with short pauses until dismissed by any user click/tap.

type AlarmStateListener = (state: {
  isActive: boolean;
  isBlockedByAutoplay: boolean;
  repeatCount: number;
}) => void;

class VoiceAlarmService {
  private isActive: boolean = false;
  private isBlockedByAutoplay: boolean = false;
  private repeatCount: number = 0;
  private loopTimeoutId: number | null = null;
  private safetyTimeoutId: number | null = null;
  private activeUtterance: SpeechSynthesisUtterance | null = null;
  private listeners: Set<AlarmStateListener> = new Set();
  private audioCtx: AudioContext | null = null;
  private chimeNodes: { osc: OscillatorNode; gain: GainNode }[] = [];

  constructor() {
    if (typeof window !== 'undefined') {
      // Warm up voices as soon as browser loads them
      if ('speechSynthesis' in window) {
        window.speechSynthesis.onvoiceschanged = () => {
          // Pre-populate voice list
          try {
            window.speechSynthesis.getVoices();
          } catch {
            // ignore
          }
        };
      }
    }
  }

  // Subscribe to alarm state changes
  public subscribe(listener: AlarmStateListener): () => void {
    this.listeners.add(listener);
    listener({
      isActive: this.isActive,
      isBlockedByAutoplay: this.isBlockedByAutoplay,
      repeatCount: this.repeatCount,
    });
    return () => {
      this.listeners.delete(listener);
    };
  }

  private notify() {
    this.listeners.forEach((fn) =>
      fn({
        isActive: this.isActive,
        isBlockedByAutoplay: this.isBlockedByAutoplay,
        repeatCount: this.repeatCount,
      })
    );
  }

  // Audio Context initializer / user gesture warm-up
  public initAudioContext(): AudioContext | null {
    if (typeof window === 'undefined') return null;
    try {
      if (!this.audioCtx) {
        const AudioCtx =
          window.AudioContext ||
          (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext;
        if (AudioCtx) {
          this.audioCtx = new AudioCtx();
        }
      }
      if (this.audioCtx && this.audioCtx.state === 'suspended') {
        this.audioCtx.resume();
      }
    } catch {
      // ignore
    }
    return this.audioCtx;
  }

  // Pre-warm SpeechSynthesis and AudioContext during button clicks (e.g. Start Break)
  public primeUserGesture() {
    this.initAudioContext();
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      try {
        window.speechSynthesis.getVoices();
        // Resume synthesis if it was paused
        if (window.speechSynthesis.paused) {
          window.speechSynthesis.resume();
        }
      } catch {
        // ignore
      }
    }
  }

  // Play a gentle, sweet pastel kalimba chime before each speech announcement
  // Soft and cute, avoiding harsh emergency sirens
  private playSoftPastelChime(): Promise<void> {
    return new Promise((resolve) => {
      try {
        const ctx = this.initAudioContext();
        if (!ctx) {
          resolve();
          return;
        }

        const now = ctx.currentTime;
        // Cute, cheerful 3-note ascending melodic chime (C5, E5, G5)
        const notes = [523.25, 659.25, 783.99];

        notes.forEach((freq, idx) => {
          const osc = ctx.createOscillator();
          const gain = ctx.createGain();

          osc.type = 'triangle'; // mellow, warm, kalimba-like
          osc.frequency.setValueAtTime(freq, now + idx * 0.1);

          gain.gain.setValueAtTime(0.12, now + idx * 0.1);
          gain.gain.exponentialRampToValueAtTime(0.001, now + idx * 0.1 + 0.35);

          osc.connect(gain);
          gain.connect(ctx.destination);

          osc.start(now + idx * 0.1);
          osc.stop(now + idx * 0.1 + 0.36);

          this.chimeNodes.push({ osc, gain });
        });

        // Resolve after chime completes
        setTimeout(resolve, 360);
      } catch {
        resolve();
      }
    });
  }

  // Find the most appropriate, natural, playful voice
  private getBestVoice(): SpeechSynthesisVoice | null {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) return null;
    const voices = window.speechSynthesis.getVoices();
    if (!voices || voices.length === 0) return null;

    // 1. Check for Hindi voice (pronounces Hindi naturally & hilariously)
    const hindiVoice = voices.find(
      (v) =>
        v.lang.toLowerCase().startsWith('hi') ||
        v.name.toLowerCase().includes('hindi')
    );
    if (hindiVoice) return hindiVoice;

    // 2. Check for Indian English voice (sounds authentic with Hinglish phrase)
    const enInVoice = voices.find(
      (v) =>
        v.lang.toLowerCase().includes('en-in') ||
        v.name.toLowerCase().includes('india')
    );
    if (enInVoice) return enInVoice;

    // 3. Cheerful, natural female or expressively pitched voices
    const cuteVoices = ['samantha', 'victoria', 'karen', 'moira', 'tessa', 'fiona', 'google', 'zira', 'natural'];
    for (const name of cuteVoices) {
      const match = voices.find(
        (v) =>
          v.name.toLowerCase().includes(name) &&
          (v.lang.startsWith('en') || v.lang.startsWith('hi'))
      );
      if (match) return match;
    }

    // 4. Default voice
    return voices.find((v) => v.default) || voices[0] || null;
  }

  // Start persistent looping voice alarm
  public start() {
    if (this.isActive) return; // already running

    this.isActive = true;
    this.isBlockedByAutoplay = false;
    this.repeatCount = 0;
    this.notify();

    this.executeAlarmCycle();
  }

  private async executeAlarmCycle() {
    if (!this.isActive) return;

    this.repeatCount += 1;
    this.notify();

    // 1. Play soft gentle melodic chime first
    await this.playSoftPastelChime();

    if (!this.isActive) return;

    // 2. Speak: "Break khatam ho gaya! Break khatam ho gaya!"
    const spoken = this.speakPhrase();

    // If SpeechSynthesis is unavailable or completely blocked, fallback to repeating chime
    if (!spoken) {
      this.scheduleNextCycle(1800);
    }
  }

  private speakPhrase(): boolean {
    if (typeof window === 'undefined' || !('speechSynthesis' in window)) {
      return false;
    }

    try {
      // Cancel any lingering utterances to prevent overlap
      window.speechSynthesis.cancel();

      const text = 'Break khatam ho gaya! Break khatam ho gaya!';
      const utterance = new SpeechSynthesisUtterance(text);

      const voice = this.getBestVoice();
      if (voice) {
        utterance.voice = voice;
        utterance.lang = voice.lang;
      } else {
        utterance.lang = 'hi-IN';
      }

      // Cute, playful, funny tuning
      utterance.pitch = 1.35; // slightly higher, cute and expressive
      utterance.rate = 1.08;  // cheerful, snappy tempo
      utterance.volume = 1.0;

      let cycleEnded = false;

      const finishAndNext = () => {
        if (cycleEnded) return;
        cycleEnded = true;
        if (this.safetyTimeoutId) {
          clearTimeout(this.safetyTimeoutId);
          this.safetyTimeoutId = null;
        }

        if (this.isActive) {
          // Requirement: Short pause between repetitions
          this.scheduleNextCycle(800);
        }
      };

      utterance.onend = () => {
        finishAndNext();
      };

      utterance.onerror = (e) => {
        // If audio blocked by browser autoplay policy
        if (e.error === 'not-allowed') {
          this.isBlockedByAutoplay = true;
          this.notify();
        }
        finishAndNext();
      };

      // Keep strong reference so Chromium won't garbage-collect before onend
      this.activeUtterance = utterance;

      // Watchdog timeout in case onend gets delayed by browser
      this.safetyTimeoutId = window.setTimeout(() => {
        finishAndNext();
      }, 4200);

      window.speechSynthesis.speak(utterance);
      return true;
    } catch {
      this.isBlockedByAutoplay = true;
      this.notify();
      return false;
    }
  }

  private scheduleNextCycle(delayMs: number) {
    if (!this.isActive) return;
    if (this.loopTimeoutId) {
      clearTimeout(this.loopTimeoutId);
    }
    this.loopTimeoutId = window.setTimeout(() => {
      this.executeAlarmCycle();
    }, delayMs);
  }

  // Stop the alarm immediately, cancel speech and any audio nodes
  public stop() {
    this.isActive = false;
    this.isBlockedByAutoplay = false;
    this.repeatCount = 0;

    if (this.loopTimeoutId) {
      clearTimeout(this.loopTimeoutId);
      this.loopTimeoutId = null;
    }

    if (this.safetyTimeoutId) {
      clearTimeout(this.safetyTimeoutId);
      this.safetyTimeoutId = null;
    }

    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      try {
        window.speechSynthesis.cancel();
      } catch {
        // ignore
      }
    }

    this.activeUtterance = null;

    // Stop active audio oscillators if any
    try {
      this.chimeNodes.forEach(({ osc }) => {
        try {
          osc.stop();
          osc.disconnect();
        } catch {
          // ignore
        }
      });
      this.chimeNodes = [];
    } catch {
      // ignore
    }

    this.notify();
  }

  public getStatus() {
    return {
      isActive: this.isActive,
      isBlockedByAutoplay: this.isBlockedByAutoplay,
      repeatCount: this.repeatCount,
    };
  }
}

export const voiceAlarm = new VoiceAlarmService();
