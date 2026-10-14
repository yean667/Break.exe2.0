/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useEffect, useRef, useCallback } from 'react';
import confetti from 'canvas-confetti';
import { BreakReason, BreakRecord, ActiveBreak, TodayStats, CharacterConfig } from './types';
import { REASON_CONFIGS, getBlobSpeech, COMPLETION_CELEBRATIONS } from './utils/quotes';
import {
  loadHistory,
  saveHistory,
  loadActiveBreak,
  saveActiveBreak,
  calculateTodayStats,
  getTodayKey,
} from './utils/storage';
import { loadSavedCharacter, saveCharacter } from './utils/characters';
import { sound } from './utils/audio';
import { voiceAlarm } from './utils/voiceAlarm';
import { DancingBlob } from './components/DancingBlob';
import {
  BlobAvatar,
  PeekingBlob,
  BooksStackDoodle,
  CurvedArrowLeft,
  CurvedArrowRight,
  RadiatingTicks,
} from './components/DoodleIcons';
import { ReasonIcon } from './components/ReasonIcons';
import { TodayDamage } from './components/TodayDamage';
import { BreakHistory } from './components/BreakHistory';
import { CharacterCustomizerModal } from './components/CharacterCustomizerModal';
import { CustomCursor } from './components/CustomCursor';
import { Footer } from './components/Footer';

const TARGET_PRESETS = [
  { mins: 0, label: 'Free Vibe' },
  { mins: 5, label: '5m Quick' },
  { mins: 10, label: '10m Coffee' },
  { mins: 13, label: '13m Target 🌸' },
  { mins: 15, label: '15m Stretch' },
  { mins: 25, label: '25m Long' },
];

export default function App() {
  // Break state
  const [activeBreak, setActiveBreak] = useState<ActiveBreak | null>(() => loadActiveBreak());
  const [elapsedSeconds, setElapsedSeconds] = useState<number>(0);
  const [remainingSeconds, setRemainingSeconds] = useState<number>(0);
  const [overdueSeconds, setOverdueSeconds] = useState<number>(0);
  const [isOverdue, setIsOverdue] = useState<boolean>(false);

  const [selectedReason, setSelectedReason] = useState<BreakReason>('phone');
  const [targetMinutes, setTargetMinutes] = useState<number>(() => {
    const loaded = loadActiveBreak();
    // Default to 13 minutes or previously saved target
    return loaded ? loaded.targetMinutes : 13;
  });
  const [explanationNote, setExplanationNote] = useState<string>('');

  // Voice alarm state
  const [isAlarmRinging, setIsAlarmRinging] = useState<boolean>(false);
  const [autoplayBlocked, setAutoplayBlocked] = useState<boolean>(false);
  const [welcomeBackToast, setWelcomeBackToast] = useState<string | null>(null);

  // Character companion state
  const [characterConfig, setCharacterConfig] = useState<CharacterConfig>(() => loadSavedCharacter());
  const [isCustomizerOpen, setIsCustomizerOpen] = useState<boolean>(false);

  // History and stats
  const [history, setHistory] = useState<BreakRecord[]>(() => loadHistory());
  const [stats, setStats] = useState<TodayStats>(() => calculateTodayStats(loadHistory()));

  // Sound settings
  const [soundEnabled, setSoundEnabled] = useState<boolean>(() => sound.enabled);
  const [celebrationToast, setCelebrationToast] = useState<string | null>(null);
  const [endingSoonNotice, setEndingSoonNotice] = useState<string | null>(null);
  const intervalRef = useRef<number | null>(null);

  // Sync sound controller
  useEffect(() => {
    sound.setEnabled(soundEnabled);
  }, [soundEnabled]);

  // Recalculate stats whenever history changes
  useEffect(() => {
    setStats(calculateTodayStats(history));
    saveHistory(history);
  }, [history]);

  // Subscribe to voice alarm status changes
  useEffect(() => {
    const unsubscribe = voiceAlarm.subscribe(({ isActive, isBlockedByAutoplay }) => {
      setIsAlarmRinging(isActive);
      setAutoplayBlocked(isBlockedByAutoplay);
    });
    return unsubscribe;
  }, []);

  // Dismiss voice alarm handler
  const handleDismissAlarm = useCallback(() => {
    voiceAlarm.stop();
    setIsAlarmRinging(false);
    setAutoplayBlocked(false);

    // Persist that the alarm was dismissed for this session so it doesn't re-ring
    if (activeBreak) {
      const updated: ActiveBreak = {
        ...activeBreak,
        alarmDismissed: true,
      };
      setActiveBreak(updated);
      saveActiveBreak(updated);
    }

    // Requirement 6: When the alarm stops, show a cute message such as “finally, welcome back bestie 🌸”
    setWelcomeBackToast('finally, welcome back bestie 🌸');
    setTimeout(() => {
      setWelcomeBackToast(null);
    }, 5500);
  }, [activeBreak]);

  // Requirement 4 & 5: Page-wide click/tap handler that immediately silences the alarm
  // Using capture: true so the first click reliably dismisses the alarm while preserving normal button functionality
  useEffect(() => {
    if (!isAlarmRinging) return;

    const onGlobalPointerDown = () => {
      handleDismissAlarm();
    };

    const onGlobalKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape' || e.key === ' ' || e.key === 'Enter') {
        handleDismissAlarm();
      }
    };

    window.addEventListener('click', onGlobalPointerDown, { capture: true });
    window.addEventListener('touchstart', onGlobalPointerDown, { capture: true });
    window.addEventListener('keydown', onGlobalKeyDown, { capture: true });

    return () => {
      window.removeEventListener('click', onGlobalPointerDown, { capture: true });
      window.removeEventListener('touchstart', onGlobalPointerDown, { capture: true });
      window.removeEventListener('keydown', onGlobalKeyDown, { capture: true });
    };
  }, [isAlarmRinging, handleDismissAlarm]);

  // Check on initial load/mount if refreshed break was already overdue
  useEffect(() => {
    const loaded = loadActiveBreak();
    if (loaded && loaded.targetMinutes > 0) {
      const targetEnd =
        loaded.targetEndTime || loaded.startTime + loaded.targetMinutes * 60 * 1000;
      const now = Date.now();
      if (now >= targetEnd && !loaded.alarmDismissed) {
        // Requirement 9: Detect overdue break after refresh and activate alarm
        voiceAlarm.start();
        setIsAlarmRinging(true);
      }
    }
  }, []);

  // Active break timer tick loop
  useEffect(() => {
    if (activeBreak) {
      const updateTimer = () => {
        const now = Date.now();
        // Requirement 8: Continue recording actual total break duration until End Break
        const totalElapsed = Math.max(0, Math.floor((now - activeBreak.startTime) / 1000));
        setElapsedSeconds(totalElapsed);

        if (activeBreak.targetMinutes > 0) {
          const targetEnd =
            activeBreak.targetEndTime ||
            activeBreak.startTime + activeBreak.targetMinutes * 60 * 1000;
          const diffToTarget = Math.floor((targetEnd - now) / 1000);

          if (diffToTarget > 0) {
            // Requirement 1: Countdown starts at targetMinutes:00 and runs down to 00:00
            setRemainingSeconds(diffToTarget);
            setIsOverdue(false);
            setOverdueSeconds(0);

            // Subtle reminder 30s before target end
            if (diffToTarget <= 30 && !activeBreak.notifiedAboutToEnd) {
              sound.playEndingSoonReminder();
              setEndingSoonNotice('break is wrapping up in ~30s... get ready ☕');

              const updatedActive = { ...activeBreak, notifiedAboutToEnd: true };
              setActiveBreak(updatedActive);
              saveActiveBreak(updatedActive);

              setTimeout(() => {
                setEndingSoonNotice(null);
              }, 5000);
            }
          } else {
            // Reached zero or overdue!
            // Requirement 1 & 7: Countdown reaches 00:00 and stays at 00:00 (does not restart)
            setRemainingSeconds(0);
            setIsOverdue(true);
            setOverdueSeconds(Math.abs(diffToTarget));

            // Requirement 2: The moment it reaches zero, trigger funny spoken alarm
            // Repeat continuously until dismissed
            if (!activeBreak.alarmDismissed && !voiceAlarm.getStatus().isActive) {
              voiceAlarm.start();
              setIsAlarmRinging(true);
            }
          }
        } else {
          // Free vibe mode (stopwatch counting up)
          setRemainingSeconds(0);
          setIsOverdue(false);
          setOverdueSeconds(0);
        }
      };

      updateTimer();
      intervalRef.current = window.setInterval(updateTimer, 1000);
      setSelectedReason(activeBreak.reason);
      setExplanationNote(activeBreak.note || '');
      setTargetMinutes(activeBreak.targetMinutes || 0);
    } else {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
        intervalRef.current = null;
      }
      setElapsedSeconds(0);
      setRemainingSeconds(0);
      setOverdueSeconds(0);
      setIsOverdue(false);
      setEndingSoonNotice(null);
      if (voiceAlarm.getStatus().isActive) {
        voiceAlarm.stop();
        setIsAlarmRinging(false);
      }
    }

    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
  }, [activeBreak]);

  // Format digital timer string MM:SS or HH:MM:SS
  const formatTimerDigits = (totalSec: number): string => {
    const hours = Math.floor(totalSec / 3600);
    const mins = Math.floor((totalSec % 3600) / 60);
    const secs = totalSec % 60;

    const pad = (n: number) => String(n).padStart(2, '0');

    if (hours > 0) {
      return `${pad(hours)}:${pad(mins)}:${pad(secs)}`;
    }
    return `${pad(mins)}:${pad(secs)}`;
  };

  // Start Break action
  const handleStartBreak = () => {
    // Technical requirement: Initialize audio & speech through user's interaction
    voiceAlarm.primeUserGesture();
    sound.initCtx();
    sound.playStartChime();

    const now = Date.now();
    const targetEndTime =
      targetMinutes > 0 ? now + targetMinutes * 60 * 1000 : undefined;

    const newActive: ActiveBreak = {
      startTime: now,
      targetEndTime: targetEndTime,
      targetMinutes: targetMinutes,
      reason: selectedReason,
      note: explanationNote.trim(),
      notifiedAboutToEnd: false,
      alarmDismissed: false,
    };

    setActiveBreak(newActive);
    saveActiveBreak(newActive);
  };

  // End Break action
  const handleEndBreak = () => {
    if (!activeBreak) return;

    // Stop voice alarm immediately if it was ringing
    if (voiceAlarm.getStatus().isActive) {
      voiceAlarm.stop();
      setIsAlarmRinging(false);
    }

    sound.playEndChime();
    const now = Date.now();
    // Requirement 8: Continue recording actual break duration until End Break
    const duration = Math.max(1, Math.floor((now - activeBreak.startTime) / 1000));

    // Choose fallback note if none entered
    const defaultNote =
      REASON_CONFIGS.find((r) => r.id === activeBreak.reason)?.humorFallback ||
      'well deserved break';

    const finalNote = activeBreak.note || explanationNote.trim() || defaultNote;

    const newRecord: BreakRecord = {
      id: 'break-' + Date.now() + '-' + Math.random().toString(36).substring(2, 6),
      startTime: activeBreak.startTime,
      endTime: now,
      durationSeconds: duration,
      reason: activeBreak.reason,
      note: finalNote,
      dateKey: getTodayKey(),
    };

    const updatedHistory = [newRecord, ...history];
    setHistory(updatedHistory);
    setActiveBreak(null);
    saveActiveBreak(null);
    setExplanationNote('');
    setEndingSoonNotice(null);
    setIsOverdue(false);

    // Trigger cute pastel confetti celebration
    try {
      confetti({
        particleCount: 50,
        spread: 65,
        origin: { y: 0.65 },
        colors: ['#FFA8BA', '#BCE0FD', '#FEF08A', '#F5E4F8', '#BBF7D0'],
      });
    } catch {
      // ignore
    }

    // Show celebration banner
    const randomMsg =
      COMPLETION_CELEBRATIONS[
        Math.floor(Math.random() * COMPLETION_CELEBRATIONS.length)
      ];
    setCelebrationToast(randomMsg);
    setTimeout(() => {
      setCelebrationToast(null);
    }, 4500);
  };

  // Delete a specific record
  const handleDeleteRecord = (id: string) => {
    setHistory((prev) => prev.filter((r) => r.id !== id));
  };

  // Clear all history
  const handleClearAll = () => {
    setHistory([]);
  };

  // Handle character customization save
  const handleSaveCharacter = (newConfig: CharacterConfig) => {
    setCharacterConfig(newConfig);
    saveCharacter(newConfig);
  };

  // Dynamic blob speech and expressions
  const blobInfo = activeBreak
    ? getBlobSpeech(elapsedSeconds, activeBreak.reason, isOverdue)
    : {
        main: 'vibing... (temporarily)',
        subtitle: 'your books can wait (again)',
        expression: 'dancing' as const,
      };

  // Determine what big digits to display
  // If active break with target: countdown from target (e.g. 13:00 to 00:00)
  // If inactive with target: show target (e.g. 13:00)
  // If free vibe (0m): show elapsed stopwatch
  const getDisplayDigits = (): string => {
    if (activeBreak) {
      if (activeBreak.targetMinutes > 0) {
        return formatTimerDigits(remainingSeconds);
      }
      return formatTimerDigits(elapsedSeconds);
    }
    if (targetMinutes > 0) {
      return formatTimerDigits(targetMinutes * 60);
    }
    return '00:00';
  };

  return (
    <div className="min-h-screen bg-[#FBF8F4] text-[#4A3E3D] selection:bg-[#FFE5EC] selection:text-[#E8556D] p-3 sm:p-5 md:p-8 flex flex-col items-center justify-center relative">
      {/* Adorable Custom Cursor (Desktop Only) */}
      <CustomCursor />

      {/* Requirement 10: Soft pastel-themed persistent visual alarm alert (NO harsh sirens) */}
      {isAlarmRinging && (
        <div
          onClick={handleDismissAlarm}
          className="fixed top-4 left-1/2 -translate-x-1/2 z-50 w-11/12 max-w-lg bg-[#FFF0F4] border-2 border-[#FFA8BA] p-4 rounded-3xl shadow-xl flex items-center gap-3.5 animate-bounce cursor-pointer select-none"
          role="alert"
          aria-live="assertive"
        >
          <div className="w-12 h-12 rounded-2xl bg-[#FFE4E9] border border-[#FFCCD5] flex items-center justify-center text-2xl shrink-0 shadow-xs">
            🔔
          </div>
          <div className="flex-1 min-w-0">
            <div className="flex items-center gap-2">
              <span className="font-display font-black text-[#E8556D] text-sm sm:text-base tracking-tight truncate">
                “Break khatam ho gaya!”
              </span>
              <span className="text-[11px] bg-[#FFD6DF] text-[#D83B55] px-2 py-0.5 rounded-full font-bold shrink-0">
                Time&apos;s Up!
              </span>
            </div>
            <p className="font-doodle text-xs sm:text-sm text-[#7D6B66] leading-tight mt-0.5">
              {autoplayBlocked
                ? 'Tap anywhere on screen to enable funny voice alarm 🌸'
                : 'Click or tap anywhere on the screen to silence the alarm ✨'}
            </p>
          </div>
          <button
            type="button"
            onClick={(e) => {
              e.stopPropagation();
              handleDismissAlarm();
            }}
            className="bg-[#FF6E85] hover:bg-[#FA5B74] text-white px-3.5 py-1.5 rounded-xl font-display font-bold text-xs shadow-xs shrink-0 cursor-pointer transition-transform active:scale-95"
            aria-label="Silence Alarm"
          >
            Silence 🌸
          </button>
        </div>
      )}

      {/* Requirement 6: Cute message when alarm stops: “finally, welcome back bestie 🌸” */}
      {welcomeBackToast && (
        <div className="fixed top-5 left-1/2 -translate-x-1/2 z-50 bg-[#FFF5F7] border-2 border-[#FFA8BA] px-5 py-2.5 rounded-full shadow-lg flex items-center gap-2.5 animate-bounce">
          <BlobAvatar
            size={26}
            mood="happy"
            bodyColor={characterConfig.bodyColor}
            blushColor={characterConfig.blushColor}
          />
          <span className="font-doodle text-sm md:text-base font-bold text-[#E8556D]">
            {welcomeBackToast}
          </span>
        </div>
      )}

      {/* Toast Notification (Break Completed) */}
      {celebrationToast && (
        <div className="fixed top-5 left-1/2 -translate-x-1/2 z-50 bg-white border-2 border-[#FFA8BA] px-5 py-2.5 rounded-full shadow-lg flex items-center gap-2 animate-bounce">
          <BlobAvatar
            size={24}
            mood="happy"
            bodyColor={characterConfig.bodyColor}
            blushColor={characterConfig.blushColor}
          />
          <span className="font-doodle text-sm md:text-base font-bold text-[#E8556D]">
            {celebrationToast}
          </span>
        </div>
      )}

      {/* Gentle Ending Soon Audio/Visual Notice */}
      {endingSoonNotice && (
        <div className="fixed top-18 left-1/2 -translate-x-1/2 z-50 bg-[#FFF7ED] border border-[#FDBA74] px-4 py-2 rounded-full shadow-md flex items-center gap-2 animate-pulse">
          <span className="text-sm">🔔</span>
          <span className="font-doodle text-xs md:text-sm font-bold text-[#C2410C]">
            {endingSoonNotice}
          </span>
        </div>
      )}

      {/* Main Container */}
      <div className="w-full max-w-5xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-5 md:gap-6 items-start">
        {/* ============================================================== */}
        {/* LEFT COLUMN: Main Break Card                                   */}
        {/* ============================================================== */}
        <div className="lg:col-span-7 bg-[#FFFDFB] border border-[#ECE4DB] rounded-[32px] p-5 md:p-7 shadow-xs flex flex-col justify-between">
          <div>
            {/* Header */}
            <div className="flex items-center justify-between mb-4">
              {/* App Title & Subtitle */}
              <div className="flex items-center gap-2.5">
                <BlobAvatar
                  size={36}
                  mood={isOverdue ? 'happy' : activeBreak ? 'happy' : 'cheeky'}
                  bodyColor={characterConfig.bodyColor}
                  blushColor={characterConfig.blushColor}
                />
                <div>
                  <h1 className="text-2xl md:text-3xl font-black tracking-tight text-[#3A2C29] flex items-center gap-1.5 leading-none font-display">
                    break.exe
                  </h1>
                  <p className="text-xs text-[#9E8E8A] font-medium mt-1">
                    because even robots need breaks
                  </p>
                </div>
              </div>

              {/* Right Action Buttons */}
              <div className="flex items-center gap-2">
                {/* Instant voice alarm test button */}
                <button
                  type="button"
                  onClick={() => {
                    voiceAlarm.primeUserGesture();
                    if (isAlarmRinging) {
                      handleDismissAlarm();
                    } else {
                      voiceAlarm.start();
                      setIsAlarmRinging(true);
                    }
                  }}
                  className={`border rounded-full px-2.5 py-1 flex items-center gap-1 shadow-2xs transition-colors cursor-pointer text-xs font-semibold ${
                    isAlarmRinging
                      ? 'bg-[#FFE6EC] border-[#FFA8BA] text-[#E8556D] animate-pulse'
                      : 'bg-[#FAF4ED] border-[#EFE5DB] text-[#6B5A56] hover:bg-[#FFF5F7] hover:border-[#FFA8BA]'
                  }`}
                  title="Test funny voice alarm: 'Break khatam ho gaya!'"
                >
                  <span>{isAlarmRinging ? '🛑' : '🔊'}</span>
                  <span className="font-doodle hidden sm:inline">
                    {isAlarmRinging ? 'Stop Alarm' : 'Test Voice'}
                  </span>
                </button>

                {/* Customize Companion Button */}
                <button
                  onClick={() => {
                    sound.playPop();
                    setIsCustomizerOpen(true);
                  }}
                  className="bg-[#FAF4ED] border border-[#EFE5DB] hover:border-[#FFA8BA] hover:bg-[#FFF5F7] rounded-full px-3 py-1 flex items-center gap-1.5 shadow-2xs transition-colors cursor-pointer text-xs font-semibold text-[#6B5A56]"
                  title="Customize your companion character"
                >
                  <span>✨</span>
                  <span className="font-doodle hidden sm:inline">{characterConfig.name}</span>
                  <span className="font-doodle sm:hidden">edit</span>
                </button>

                {/* Sound mute/unmute button */}
                <button
                  onClick={() => {
                    const newVal = !soundEnabled;
                    setSoundEnabled(newVal);
                    sound.setEnabled(newVal);
                    if (newVal) sound.playStartChime();
                  }}
                  className={`w-8 h-8 rounded-full border flex items-center justify-center transition-colors cursor-pointer text-xs ${
                    soundEnabled
                      ? 'bg-[#FAF4ED] border-[#EFE5DB] text-[#7E6E6A] hover:text-[#3A2C29]'
                      : 'bg-[#F2ECE6] border-[#DFD5CB] text-[#A89894]'
                  }`}
                  title={soundEnabled ? 'Mute sounds' : 'Enable sounds'}
                >
                  {soundEnabled ? '🔔' : '🔕'}
                </button>
              </div>
            </div>

            {/* Inner Pastel Pink Card (Dancing Blob & Digital Timer Centerpiece) */}
            <div
              className={`border rounded-[26px] p-5 md:p-6 my-4 relative overflow-hidden select-none transition-all duration-300 ${
                isOverdue
                  ? 'bg-[#FFF0F3] border-[#FFB6C4] shadow-sm'
                  : 'bg-[#FFF0F2] border-[#FFE1E6]'
              }`}
            >
              {/* Overdue Alert Banner inside card */}
              {isOverdue && (
                <div className="mb-2 bg-[#FFE6EC] border border-[#FFA8BA] rounded-xl px-3 py-1 flex items-center justify-between text-xs font-display animate-pulse">
                  <div className="flex items-center gap-1.5 text-[#E8556D] font-bold">
                    <span>⏰</span>
                    <span>Break expired! +{formatTimerDigits(overdueSeconds)} overdue</span>
                  </div>
                  {isAlarmRinging && (
                    <span className="font-doodle text-[11px] text-[#A84557]">
                      tap anywhere to silence 🌸
                    </span>
                  )}
                </div>
              )}

              {/* Top Digital Timer with Radiating Ticks */}
              <div className="flex items-center justify-center gap-3 md:gap-4 mb-2">
                <RadiatingTicks side="left" />
                <div
                  className={`text-5xl md:text-6xl font-black tracking-tight font-display drop-shadow-2xs transition-colors ${
                    isOverdue ? 'text-[#E8556D]' : 'text-[#EB5E6F]'
                  }`}
                >
                  {getDisplayDigits()}
                </div>
                <RadiatingTicks side="right" />
              </div>

              {/* Center Row: Left Quote + Dancing Blob + Right Books Doodle */}
              <div className="relative flex items-center justify-between py-1">
                {/* Left humorous note with hand-drawn arrow */}
                <div className="w-24 md:w-32 flex flex-col items-center text-center">
                  <p className="font-doodle text-xs md:text-sm text-[#7D6B66] leading-tight">
                    {blobInfo.main}
                  </p>
                  <CurvedArrowLeft className="mt-1" />
                </div>

                {/* The Adorable Dancing Character */}
                <div className="flex-1 flex justify-center">
                  <DancingBlob
                    isDancing={Boolean(activeBreak)}
                    expression={blobInfo.expression}
                    config={characterConfig}
                  />
                </div>

                {/* Right stacked books doodle with hand-drawn arrow */}
                <div className="w-24 md:w-32 flex flex-col items-center text-center">
                  <p className="font-doodle text-xs md:text-sm text-[#7D6B66] leading-tight">
                    {blobInfo.subtitle}
                  </p>
                  <div className="flex items-center justify-center mt-1">
                    <CurvedArrowRight className="mr-0.5" />
                    <BooksStackDoodle />
                  </div>
                </div>
              </div>

              {/* Status / Duration sub-badge */}
              {activeBreak && (
                <div className="mt-2 text-center flex flex-col items-center gap-1">
                  {activeBreak.targetMinutes > 0 ? (
                    <div className="flex flex-wrap items-center justify-center gap-1.5">
                      <span
                        className={`font-doodle text-xs px-3 py-1 rounded-full border ${
                          isOverdue
                            ? 'bg-[#FFE6EC] border-[#FFA8BA] text-[#E8556D] font-bold'
                            : 'bg-white/80 border-[#FFD5DC] text-[#C85567]'
                        }`}
                      >
                        {isOverdue
                          ? `target (${activeBreak.targetMinutes}m) reached • overdue: +${formatTimerDigits(overdueSeconds)}`
                          : `countdown: ${formatTimerDigits(remainingSeconds)} left (target: ${activeBreak.targetMinutes}m)`}
                      </span>
                      <span className="font-doodle text-[11px] text-[#8F7F7B] bg-white/60 px-2 py-0.5 rounded-full border border-[#EAE2D8]">
                        total time: {formatTimerDigits(elapsedSeconds)}
                      </span>
                    </div>
                  ) : (
                    <span className="font-doodle text-xs text-[#C85567] bg-white/70 px-3 py-1 rounded-full border border-[#FFD5DC]">
                      free vibe stopwatch • {formatTimerDigits(elapsedSeconds)} elapsed
                    </span>
                  )}
                </div>
              )}
            </div>

            {/* Target Duration Selector & Custom Timer (when break is inactive) */}
            {!activeBreak && (
              <div className="mb-4 bg-[#FAF7F3] border border-[#EFE7DE] rounded-2xl p-3.5">
                <div className="flex items-center justify-between mb-2">
                  <label className="text-xs font-bold text-[#6D5D59] uppercase tracking-wider flex items-center gap-1.5">
                    <span>⏱️</span>
                    <span>Set Break Timer Length:</span>
                  </label>
                  <span className="font-doodle text-xs text-[#E8556D] font-bold">
                    {targetMinutes === 0
                      ? 'free-form stopwatch'
                      : `timer: ${targetMinutes} min countdown 🔔`}
                  </span>
                </div>

                {/* Quick Presets */}
                <div className="flex gap-1.5 overflow-x-auto pb-1.5">
                  {TARGET_PRESETS.map((t) => (
                    <button
                      key={t.mins}
                      type="button"
                      onClick={() => {
                        sound.playPop();
                        setTargetMinutes(t.mins);
                      }}
                      className={`px-3 py-1.5 rounded-xl border text-xs font-bold transition-all cursor-pointer shrink-0 ${
                        targetMinutes === t.mins
                          ? 'bg-[#FFE6EC] border-[#FFA8BA] text-[#E8556D] shadow-2xs scale-102'
                          : 'bg-white border-[#EAE2D8] text-[#6E5E5A] hover:bg-[#FFF5F7]'
                      }`}
                    >
                      {t.label}
                    </button>
                  ))}
                </div>

                {/* Custom Minutes Input Stepper */}
                <div className="flex items-center justify-between mt-2 pt-2 border-t border-[#EAE2D8]/70">
                  <span className="text-xs text-[#7A6B67] font-medium">
                    Or custom duration:
                  </span>
                  <div className="flex items-center gap-2 bg-white border border-[#EAE2D8] rounded-xl px-2.5 py-1 shadow-2xs">
                    <button
                      type="button"
                      onClick={() => {
                        sound.playPop();
                        setTargetMinutes((prev) => Math.max(1, prev - 1));
                      }}
                      className="w-6 h-6 rounded-lg bg-[#FAF4ED] hover:bg-[#FFE6EC] text-[#554643] text-sm font-bold flex items-center justify-center cursor-pointer transition-colors"
                      title="Decrease by 1 minute"
                    >
                      -
                    </button>
                    <div className="flex items-center gap-1">
                      <input
                        type="number"
                        min={1}
                        max={180}
                        value={targetMinutes || ''}
                        onChange={(e) => {
                          const val = parseInt(e.target.value, 10);
                          setTargetMinutes(isNaN(val) ? 0 : Math.max(1, Math.min(180, val)));
                        }}
                        className="w-10 text-center font-display font-bold text-sm text-[#E8556D] bg-transparent focus:outline-none"
                      />
                      <span className="text-xs text-[#8F7F7B] font-medium">min</span>
                    </div>
                    <button
                      type="button"
                      onClick={() => {
                        sound.playPop();
                        setTargetMinutes((prev) => Math.min(180, (prev || 0) + 1));
                      }}
                      className="w-6 h-6 rounded-lg bg-[#FAF4ED] hover:bg-[#FFE6EC] text-[#554643] text-sm font-bold flex items-center justify-center cursor-pointer transition-colors"
                      title="Increase by 1 minute"
                    >
                      +
                    </button>
                  </div>
                </div>
              </div>
            )}

            {/* Break Reason Selection */}
            <div>
              <label className="block text-sm md:text-base font-bold text-[#3A2C29] mb-2.5 font-display">
                why are you taking a break?
              </label>

              {/* Reasons Pills Grid */}
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 mb-3">
                {REASON_CONFIGS.map((r) => {
                  const isSelected = selectedReason === r.id;
                  return (
                    <button
                      key={r.id}
                      type="button"
                      disabled={Boolean(activeBreak)}
                      onClick={() => {
                        sound.playPop();
                        setSelectedReason(r.id);
                      }}
                      className={`flex items-center justify-center gap-1.5 py-2 px-3 rounded-2xl border text-xs md:text-sm font-semibold transition-all cursor-pointer ${
                        activeBreak ? 'cursor-not-allowed opacity-90' : ''
                      } ${
                        isSelected
                          ? 'bg-[#FFF0F3] border-[#FFA8BA] text-[#E8556D] shadow-2xs'
                          : 'bg-white border-[#EAE2D8] text-[#554643] hover:bg-[#FDF9F6]'
                      }`}
                    >
                      <ReasonIcon reason={r.id} size={17} />
                      <span className="truncate">{r.label}</span>
                    </button>
                  );
                })}
              </div>

              {/* Optional Explanation Input with Peeking Blob */}
              <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3 mt-3">
                <input
                  type="text"
                  value={explanationNote}
                  disabled={Boolean(activeBreak)}
                  onChange={(e) => setExplanationNote(e.target.value)}
                  placeholder="any explanation? (optional)"
                  maxLength={60}
                  className="flex-1 bg-white border border-[#EAE2D8] rounded-2xl px-4 py-2.5 text-xs md:text-sm text-[#4A3E3D] placeholder-[#A69793] focus:outline-none focus:border-[#FFA8BA] transition-colors disabled:bg-[#FAF6F2]"
                />

                <div className="shrink-0 flex justify-end">
                  <PeekingBlob text="no judgment... ~ maybe" />
                </div>
              </div>
            </div>
          </div>

          {/* Action Button: Start or End Break */}
          <div className="mt-6">
            {!activeBreak ? (
              <button
                type="button"
                onClick={handleStartBreak}
                className="w-full bg-[#FF6E85] hover:bg-[#FA5B74] active:scale-[0.99] text-white font-bold text-base md:text-lg py-3.5 px-6 rounded-2xl shadow-sm transition-all cursor-pointer flex items-center justify-center gap-2 font-display"
              >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
                  <polygon points="5 3 19 12 5 21 5 3" />
                </svg>
                <span>
                  {targetMinutes > 0
                    ? `Start ${targetMinutes}m Break`
                    : 'Start Free Break'}
                </span>
              </button>
            ) : (
              <button
                type="button"
                onClick={handleEndBreak}
                className={`w-full text-white font-bold text-base md:text-lg py-3.5 px-6 rounded-2xl shadow-sm transition-all cursor-pointer flex items-center justify-center gap-2 font-display ${
                  isOverdue
                    ? 'bg-[#E8556D] hover:bg-[#D4445C] animate-bounce'
                    : 'bg-[#FF6E85] hover:bg-[#FA5B74] animate-pulse-glow'
                }`}
              >
                {/* White stop square */}
                <rect width="16" height="16" rx="3" className="fill-white" />
                <span>
                  {isOverdue ? 'End Break (You survived! 🎓)' : 'End Break'}
                </span>
              </button>
            )}

            {/* Bottom Sarcastic Caption */}
            <p className="font-doodle text-xs md:text-sm text-[#8F7F7B] mt-2.5 text-center select-none">
              {isOverdue
                ? 'Break khatam ho gaya! Time to get back to studying 🌸'
                : 'come back soon... your future self is watching 👀'}
            </p>
          </div>
        </div>

        {/* ============================================================== */}
        {/* RIGHT COLUMN: Today's Damage + Break History                   */}
        {/* ============================================================== */}
        <div className="lg:col-span-5 flex flex-col gap-5 md:gap-6">
          {/* Top Card: today's damage */}
          <TodayDamage stats={stats} />

          {/* Bottom Card: break history */}
          <BreakHistory
            records={history}
            onDeleteRecord={handleDeleteRecord}
            onClearAll={handleClearAll}
          />
        </div>
      </div>

      {/* Creator Credits and Socials Footer */}
      <Footer />

      {/* Character Customizer & Sound Setup Modal */}
      <CharacterCustomizerModal
        isOpen={isCustomizerOpen}
        onClose={() => setIsCustomizerOpen(false)}
        currentConfig={characterConfig}
        onSave={handleSaveCharacter}
        soundEnabled={soundEnabled}
        onToggleSound={setSoundEnabled}
      />
    </div>
  );
}
