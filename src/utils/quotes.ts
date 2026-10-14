import { BreakReason } from '../types';

export const REASON_CONFIGS: { id: BreakReason; label: string; icon: string; emoji: string; humorFallback: string }[] = [
  { id: 'water', label: 'water', icon: 'water', emoji: '💧', humorFallback: 'hydration era ✨' },
  { id: 'food', label: 'food', icon: 'food', emoji: '🥪', humorFallback: 'had to refuel the machine' },
  { id: 'phone', label: 'phone', icon: 'phone', emoji: '📱', humorFallback: 'one reel turned into research' },
  { id: 'bathroom', label: 'bathroom', icon: 'bathroom', emoji: '🚽', humorFallback: 'nature called' },
  { id: 'brain', label: 'brain not braining', icon: 'brain', emoji: '🧠', humorFallback: 'my brain took a vacation' },
  { id: 'grass', label: 'touching grass', icon: 'grass', emoji: '🌿', humorFallback: 'remembered the sun exists' },
  { id: 'existing', label: 'just existing', icon: 'existing', emoji: '💤', humorFallback: 'staring into the abyss politely' },
  { id: 'other', label: 'other...', icon: 'other', emoji: '✨', humorFallback: 'unclassified secret mission' },
];

export const VIBE_QUOTES = [
  'vibing... (temporarily)',
  'your books can wait. probably.',
  'bro said one minute.',
  'the academic comeback is buffering…',
  'your chair misses you.',
  'one reel became a documentary.',
  'no judgment. okay, a little judgment.',
  'welcome back, stranger.',
  'your future self is watching 👀',
  'brain defragmentation in progress...',
  'absorbing ambient knowledge by osmosis',
  '5 more minutes won’t hurt (narrator: it did)',
  'running break.exe in safe mode',
  'recalibrating motivation levels (currently 12%)',
  'technically this is productive rest'
];

export function getBlobSpeech(
  seconds: number,
  reason: BreakReason,
  isOverdue: boolean = false
): { main: string; subtitle: string; expression: 'happy' | 'dancing' | 'sleepy' | 'confused' } {
  if (isOverdue) {
    return {
      main: 'Break khatam ho gaya! ⏰',
      subtitle: 'ready to get back, bestie? 🌸',
      expression: 'happy'
    };
  }
  if (seconds < 60) {
    return {
      main: 'vibing... (temporarily)',
      subtitle: 'your books can wait (again)',
      expression: 'dancing'
    };
  }
  if (seconds < 180) {
    return {
      main: 'just catching breath',
      subtitle: 'the academic comeback is buffering…',
      expression: 'dancing'
    };
  }
  if (seconds < 360) {
    if (reason === 'phone') {
      return {
        main: 'one reel became a docu',
        subtitle: 'your algorithm knows you too well',
        expression: 'happy'
      };
    }
    if (reason === 'water') {
      return {
        main: 'hydration era ✨',
        subtitle: '80% water, 20% stress',
        expression: 'happy'
      };
    }
    return {
      main: 'bro said one minute.',
      subtitle: 'time is an illusion anyway',
      expression: 'happy'
    };
  }
  if (seconds < 900) {
    return {
      main: 'your chair misses you.',
      subtitle: 'your books are getting cold',
      expression: 'confused'
    };
  }
  if (seconds < 1800) {
    return {
      main: 'are we still studying today?',
      subtitle: 'future self is sending distress signals',
      expression: 'sleepy'
    };
  }
  return {
    main: 'you live here now.',
    subtitle: 'academic comeback has left the chat',
    expression: 'sleepy'
  };
}

export const COMPLETION_CELEBRATIONS = [
  'welcome back, scholar! ✨',
  'brain successfully recharged 🔋',
  'you survived the break! time to cook 📚',
  'back in the arena 🧠',
  '100 aura points regained ✨'
];
