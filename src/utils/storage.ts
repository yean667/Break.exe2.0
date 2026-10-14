import { BreakRecord, ActiveBreak, TodayStats } from '../types';

const STORAGE_KEY_HISTORY = 'break_exe_history_v1';
const STORAGE_KEY_ACTIVE = 'break_exe_active_v1';

// Format date key: YYYY-MM-DD
export function getTodayKey(): string {
  const d = new Date();
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

export function formatTimeString(timestamp: number): string {
  const date = new Date(timestamp);
  return date.toLocaleTimeString([], { hour: 'numeric', minute: '2-digit' });
}

export function formatDurationBadge(seconds: number): string {
  if (seconds < 60) {
    return `${seconds}s`;
  }
  const mins = Math.floor(seconds / 60);
  const hours = Math.floor(mins / 60);
  const remMins = mins % 60;
  if (hours > 0) {
    return remMins > 0 ? `${hours}h ${remMins}m` : `${hours}h`;
  }
  return `${mins}m`;
}

export function formatStatsTime(seconds: number): string {
  if (seconds <= 0) return '0m';
  const hours = Math.floor(seconds / 3600);
  const mins = Math.floor((seconds % 3600) / 60);
  if (hours > 0) {
    return `${hours}h ${mins}m`;
  }
  return `${mins}m`;
}



export function loadHistory(): BreakRecord[] {
  try {
    const raw = localStorage.getItem(STORAGE_KEY_HISTORY);
    if (!raw) {
      return [];
    }
    const parsed = JSON.parse(raw);
    if (!Array.isArray(parsed)) return [];
    // Ensure no legacy sample/demo records are displayed
    return parsed.filter((r) => r && typeof r.id === 'string' && !r.id.startsWith('sample-'));
  } catch {
    return [];
  }
}

export function saveHistory(records: BreakRecord[]): void {
  try {
    localStorage.setItem(STORAGE_KEY_HISTORY, JSON.stringify(records));
  } catch (err) {
    console.error('Failed to save break history to localStorage', err);
  }
}

export function loadActiveBreak(): ActiveBreak | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY_ACTIVE);
    if (!raw) return null;
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export function saveActiveBreak(active: ActiveBreak | null): void {
  try {
    if (!active) {
      localStorage.removeItem(STORAGE_KEY_ACTIVE);
    } else {
      localStorage.setItem(STORAGE_KEY_ACTIVE, JSON.stringify(active));
    }
  } catch (err) {
    console.error('Failed to save active break', err);
  }
}

export function calculateTodayStats(records: BreakRecord[]): TodayStats {
  const todayKey = getTodayKey();
  const todayRecords = records.filter((r) => r.dateKey === todayKey);

  if (todayRecords.length === 0) {
    return {
      totalSeconds: 0,
      count: 0,
      longestSeconds: 0,
      averageSeconds: 0,
    };
  }

  const totalSeconds = todayRecords.reduce((acc, r) => acc + r.durationSeconds, 0);
  const longestSeconds = Math.max(...todayRecords.map((r) => r.durationSeconds));
  const averageSeconds = Math.round(totalSeconds / todayRecords.length);

  return {
    totalSeconds,
    count: todayRecords.length,
    longestSeconds,
    averageSeconds,
  };
}
