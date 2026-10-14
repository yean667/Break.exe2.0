// Web Audio API synthesizer for charming, minimalist, non-intrusive sound effects
class SoundController {
  private ctx: AudioContext | null = null;
  public enabled: boolean = true;
  public volume: number = 0.6; // 0.0 to 1.0

  constructor() {
    if (typeof window !== 'undefined') {
      const savedMute = localStorage.getItem('break_exe_sound_enabled');
      if (savedMute !== null) {
        this.enabled = savedMute === 'true';
      }
    }
  }

  public setEnabled(val: boolean) {
    this.enabled = val;
    if (typeof window !== 'undefined') {
      localStorage.setItem('break_exe_sound_enabled', String(val));
    }
  }

  public initCtx() {
    if (!this.ctx && typeof window !== 'undefined') {
      const AudioCtx = window.AudioContext || (window as unknown as { webkitAudioContext: typeof AudioContext }).webkitAudioContext;
      if (AudioCtx) {
        this.ctx = new AudioCtx();
      }
    }
    if (this.ctx && this.ctx.state === 'suspended') {
      this.ctx.resume();
    }
  }

  // Cute bubble pop sound for UI clicks
  playPop() {
    if (!this.enabled) return;
    try {
      this.initCtx();
      if (!this.ctx) return;
      const osc = this.ctx.createOscillator();
      const gain = this.ctx.createGain();
      
      const now = this.ctx.currentTime;
      osc.type = 'sine';
      osc.frequency.setValueAtTime(480, now);
      osc.frequency.exponentialRampToValueAtTime(780, now + 0.07);

      gain.gain.setValueAtTime(0.09 * this.volume, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.07);

      osc.connect(gain);
      gain.connect(this.ctx.destination);

      osc.start(now);
      osc.stop(now + 0.08);
    } catch {
      // Audio context might be restricted
    }
  }

  // Gentle, charming ascending kalimba chime for starting a break
  playStartChime() {
    if (!this.enabled) return;
    try {
      this.initCtx();
      if (!this.ctx) return;
      // Gentle pentatonic kalimba tones (C5, D5, G5, A5)
      const notes = [523.25, 587.33, 783.99, 880.0];
      notes.forEach((freq, i) => {
        if (!this.ctx) return;
        const osc = this.ctx.createOscillator();
        const gain = this.ctx.createGain();
        const now = this.ctx.currentTime + i * 0.08;

        osc.type = 'triangle';
        osc.frequency.setValueAtTime(freq, now);

        gain.gain.setValueAtTime(0.12 * this.volume, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.38);

        osc.connect(gain);
        gain.connect(this.ctx.destination);

        osc.start(now);
        osc.stop(now + 0.4);
      });
    } catch {
      // ignore
    }
  }

  // Distinct, rewarding warm bell chime when ending a break
  playEndChime() {
    if (!this.enabled) return;
    try {
      this.initCtx();
      if (!this.ctx) return;
      // Welcoming chord (G5, E5, C5, F5, G5)
      const notes = [783.99, 659.25, 523.25, 698.46];
      notes.forEach((freq, i) => {
        if (!this.ctx) return;
        const osc = this.ctx.createOscillator();
        const gain = this.ctx.createGain();
        const now = this.ctx.currentTime + i * 0.09;

        osc.type = 'sine';
        osc.frequency.setValueAtTime(freq, now);

        gain.gain.setValueAtTime(0.11 * this.volume, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.45);

        osc.connect(gain);
        gain.connect(this.ctx.destination);

        osc.start(now);
        osc.stop(now + 0.48);
      });
    } catch {
      // ignore
    }
  }

  // Subtle, soft, non-intrusive notification when a break is about to end
  playEndingSoonReminder() {
    if (!this.enabled) return;
    try {
      this.initCtx();
      if (!this.ctx) return;
      // Two soft gentle glass-bell chimes (E5, B5), very mellow and serene
      const notes = [659.25, 987.77];
      notes.forEach((freq, i) => {
        if (!this.ctx) return;
        const osc = this.ctx.createOscillator();
        const gain = this.ctx.createGain();
        const now = this.ctx.currentTime + i * 0.22;

        osc.type = 'sine';
        osc.frequency.setValueAtTime(freq, now);

        // Very soft volume so it is gentle and not jarring
        gain.gain.setValueAtTime(0.08 * this.volume, now);
        gain.gain.exponentialRampToValueAtTime(0.001, now + 0.65);

        osc.connect(gain);
        gain.connect(this.ctx.destination);

        osc.start(now);
        osc.stop(now + 0.7);
      });
    } catch {
      // ignore
    }
  }

  // Adorable giggly squish sound when poking the character
  playGiggle() {
    if (!this.enabled) return;
    try {
      this.initCtx();
      if (!this.ctx) return;
      const osc = this.ctx.createOscillator();
      const gain = this.ctx.createGain();
      const now = this.ctx.currentTime;

      osc.type = 'sine';
      osc.frequency.setValueAtTime(580, now);
      osc.frequency.linearRampToValueAtTime(920, now + 0.06);
      osc.frequency.linearRampToValueAtTime(740, now + 0.12);
      osc.frequency.linearRampToValueAtTime(1080, now + 0.18);

      gain.gain.setValueAtTime(0.08 * this.volume, now);
      gain.gain.exponentialRampToValueAtTime(0.001, now + 0.22);

      osc.connect(gain);
      gain.connect(this.ctx.destination);

      osc.start(now);
      osc.stop(now + 0.23);
    } catch {
      // ignore
    }
  }
}

export const sound = new SoundController();

