import React, { useState } from 'react';
import { BreakRecord } from '../types';
import { ReasonIcon } from './ReasonIcons';
import { formatTimeString, formatDurationBadge } from '../utils/storage';
import { sound } from '../utils/audio';

interface BreakHistoryProps {
  records: BreakRecord[];
  onDeleteRecord: (id: string) => void;
  onClearAll: () => void;
}

export const BreakHistory: React.FC<BreakHistoryProps> = ({
  records,
  onDeleteRecord,
  onClearAll,
}) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [hoveredId, setHoveredId] = useState<string | null>(null);

  return (
    <div className="bg-[#FFFDFB] border border-[#ECE4DB] rounded-[28px] p-5 shadow-xs relative flex flex-col flex-1">
      {/* Header */}
      <div className="flex items-center justify-between mb-3 relative">
        <h2 className="text-xl md:text-2xl font-bold tracking-tight text-[#3A2C29]">
          break history
        </h2>

        {/* Action button ••• */}
        {records.length > 0 && (
          <div className="relative">
            <button
              onClick={() => {
                sound.playPop();
                setMenuOpen(!menuOpen);
              }}
              className="w-8 h-8 rounded-full flex items-center justify-center text-[#9E8E8A] hover:text-[#3A2C29] hover:bg-[#F5EFE9] transition-colors cursor-pointer tracking-widest text-lg font-bold"
              title="History options"
            >
              •••
            </button>

            {/* Dropdown Menu */}
            {menuOpen && (
              <div className="absolute right-0 top-10 w-44 bg-white border border-[#EAE1D7] rounded-2xl shadow-lg py-1.5 z-30 font-medium text-xs text-[#5C4D4A]">
                <button
                  onClick={() => {
                    sound.playPop();
                    onClearAll();
                    setMenuOpen(false);
                  }}
                  className="w-full text-left px-4 py-2 hover:bg-[#FFF2F5] hover:text-[#E8556D] cursor-pointer flex items-center gap-2 text-rose-500 font-semibold"
                >
                  <span>🗑</span> Clear all history
                </button>
              </div>
            )}
          </div>
        )}
      </div>

      {/* History List / Empty State */}
      <div className="space-y-3 overflow-y-auto max-h-[380px] pr-1 flex-1 flex flex-col">
        {records.length === 0 ? (
          <div className="py-10 px-4 text-center select-none my-auto flex flex-col items-center justify-center">
            {/* Cute Sleepy Daisy Illustration */}
            <div className="w-24 h-24 mb-3 relative flex items-center justify-center">
              {/* Soft gentle pastel pink & baby blue background puddle */}
              <div className="absolute inset-0 rounded-full bg-gradient-to-tr from-[#FFE5EC]/70 via-[#FFF9F2] to-[#E0F2FE]/70 blur-[1px]" />

              <svg
                viewBox="0 0 100 100"
                className="w-20 h-20 relative z-10 drop-shadow-xs"
                fill="none"
                xmlns="http://www.w3.org/2000/svg"
              >
                {/* Gentle ground shadow / leaf base */}
                <ellipse cx="50" cy="86" rx="22" ry="5" fill="#EAE0D6" />

                {/* Curved little sleepy stem */}
                <path
                  d="M50 84 C48 70 53 58 50 48"
                  stroke="#3A2C29"
                  strokeWidth="3"
                  strokeLinecap="round"
                />

                {/* Soft pastel green leaf */}
                <path
                  d="M49 68 C42 66 38 60 40 55 C46 56 49 61 49 68 Z"
                  fill="#BBF7D0"
                  stroke="#3A2C29"
                  strokeWidth="2"
                  strokeLinejoin="round"
                />
                <path
                  d="M51 63 C58 62 63 56 62 50 C56 51 52 57 51 63 Z"
                  fill="#BBF7D0"
                  stroke="#3A2C29"
                  strokeWidth="2"
                  strokeLinejoin="round"
                />

                {/* Daisy Flower Head (Slightly tilted, resting peacefully) */}
                <g transform="rotate(-8 50 42)">
                  {/* Petal 1 - Top */}
                  <ellipse
                    cx="50"
                    cy="25"
                    rx="9"
                    ry="12"
                    fill="#FFCCD7"
                    stroke="#3A2C29"
                    strokeWidth="2.4"
                  />
                  {/* Petal 2 - Top Right */}
                  <ellipse
                    cx="65"
                    cy="33"
                    rx="9"
                    ry="12"
                    fill="#BCE0FD"
                    stroke="#3A2C29"
                    strokeWidth="2.4"
                    transform="rotate(68 65 33)"
                  />
                  {/* Petal 3 - Bottom Right */}
                  <ellipse
                    cx="60"
                    cy="52"
                    rx="9"
                    ry="12"
                    fill="#FFCCD7"
                    stroke="#3A2C29"
                    strokeWidth="2.4"
                    transform="rotate(136 60 52)"
                  />
                  {/* Petal 4 - Bottom Left */}
                  <ellipse
                    cx="40"
                    cy="52"
                    rx="9"
                    ry="12"
                    fill="#BCE0FD"
                    stroke="#3A2C29"
                    strokeWidth="2.4"
                    transform="rotate(-136 40 52)"
                  />
                  {/* Petal 5 - Top Left */}
                  <ellipse
                    cx="35"
                    cy="33"
                    rx="9"
                    ry="12"
                    fill="#FFD8E2"
                    stroke="#3A2C29"
                    strokeWidth="2.4"
                    transform="rotate(-68 35 33)"
                  />

                  {/* Soft Yellow Center Core */}
                  <circle
                    cx="50"
                    cy="40"
                    r="14"
                    fill="#FEF08A"
                    stroke="#3A2C29"
                    strokeWidth="2.6"
                  />

                  {/* Rosy blush cheeks */}
                  <ellipse cx="41" cy="42" rx="3.5" ry="2" fill="#FFAEC0" />
                  <ellipse cx="59" cy="42" rx="3.5" ry="2" fill="#FFAEC0" />

                  {/* Sleepy closed curved eyes: ⌒   ⌒ */}
                  <path
                    d="M42 38 Q45 35 48 38"
                    stroke="#3A2C29"
                    strokeWidth="2.2"
                    strokeLinecap="round"
                    fill="none"
                  />
                  <path
                    d="M52 38 Q55 35 58 38"
                    stroke="#3A2C29"
                    strokeWidth="2.2"
                    strokeLinecap="round"
                    fill="none"
                  />

                  {/* Tiny sleepy resting smile */}
                  <path
                    d="M48 43 Q50 45 52 43"
                    stroke="#3A2C29"
                    strokeWidth="2"
                    strokeLinecap="round"
                    fill="none"
                  />
                </g>

                {/* Cute Floating Sleepy Snoozes "z z" */}
                <text
                  x="72"
                  y="26"
                  fill="#7EB8F7"
                  fontSize="12"
                  fontWeight="bold"
                  fontFamily="'Patrick Hand', cursive, sans-serif"
                >
                  z
                </text>
                <text
                  x="80"
                  y="18"
                  fill="#FFA8BA"
                  fontSize="14"
                  fontWeight="bold"
                  fontFamily="'Patrick Hand', cursive, sans-serif"
                >
                  Z
                </text>
              </svg>
            </div>

            {/* Empty State Message */}
            <p className="font-doodle text-base md:text-lg text-[#5A4945] font-bold max-w-[280px] leading-snug">
              “no breaks yet… your procrastination lore starts here 🌼”
            </p>
            <p className="font-doodle text-xs md:text-sm text-[#A08F8B] mt-1.5 max-w-[260px]">
              ready when you need water, fresh air, or an academic pause ✨
            </p>
          </div>
        ) : (
          records.map((item) => (
            <div
              key={item.id}
              onMouseEnter={() => setHoveredId(item.id)}
              onMouseLeave={() => setHoveredId(null)}
              className="flex items-center gap-3 py-2 px-2.5 rounded-xl hover:bg-[#FAF4EE] transition-colors group relative"
            >
              {/* Start Time */}
              <div className="text-xs md:text-sm font-semibold text-[#8C7A76] w-16 md:w-20 shrink-0">
                {formatTimeString(item.startTime)}
              </div>

              {/* Pink Duration Badge */}
              <div className="shrink-0 bg-[#FFEBF0] text-[#E8556D] font-bold text-xs md:text-sm px-2.5 py-0.5 rounded-full border border-[#FFCCD7]">
                {formatDurationBadge(item.durationSeconds)}
              </div>

              {/* Reason Icon */}
              <div className="w-8 h-8 rounded-lg bg-white border border-[#EAE2D8] flex items-center justify-center shrink-0 shadow-2xs">
                <ReasonIcon reason={item.reason} size={20} />
              </div>

              {/* Reason Title & Note */}
              <div className="min-w-0 flex-1">
                <div className="font-bold text-sm text-[#3A2C29] capitalize leading-snug">
                  {item.reason === 'brain'
                    ? 'brain not braining'
                    : item.reason === 'grass'
                    ? 'touching grass'
                    : item.reason === 'existing'
                    ? 'just existing'
                    : item.reason}
                </div>
                <div className="text-xs font-doodle text-[#7E6E6A] truncate">
                  {item.note || 'vibing...'}
                </div>
              </div>

              {/* Delete button (shows on hover or always subtle) */}
              <button
                onClick={() => {
                  sound.playPop();
                  onDeleteRecord(item.id);
                }}
                className={`w-7 h-7 rounded-full flex items-center justify-center text-[#B5A5A1] hover:text-rose-500 hover:bg-[#FFEAEF] transition-all cursor-pointer ${
                  hoveredId === item.id ? 'opacity-100' : 'opacity-0 md:opacity-20'
                }`}
                title="Delete this break entry"
              >
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
                  <path d="M3 6h18" />
                  <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
                  <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
                </svg>
              </button>
            </div>
          ))
        )}
      </div>
    </div>
  );
};
