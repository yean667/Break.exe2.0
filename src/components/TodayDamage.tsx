import React from 'react';
import { TodayStats } from '../types';
import { formatStatsTime } from '../utils/storage';

interface TodayDamageProps {
  stats: TodayStats;
}

export const TodayDamage: React.FC<TodayDamageProps> = ({ stats }) => {
  return (
    <div className="bg-[#E9F3FC] border border-[#D5E7F7] rounded-[28px] p-5 shadow-xs relative">
      {/* Header */}
      <div className="flex items-start justify-between mb-4">
        <h2 className="text-xl md:text-2xl font-bold tracking-tight text-[#3A2C29]">
          today's damage
        </h2>

        {/* Top-Right Peeking Blob with Handwritten speech */}
        <div className="flex items-center gap-1 -mt-2 -mr-1 select-none pointer-events-none">
          <div className="font-doodle text-xs md:text-sm text-[#5B758E] leading-tight text-right">
            <span>~ still better</span>
            <span className="block text-[11px] opacity-80">than yesterday (i hope)</span>
          </div>

          <svg width="40" height="30" viewBox="0 0 50 35" fill="none" className="shrink-0">
            {/* Peeking head */}
            <path
              d="M7 35 C7 16 16 7 25 7 C34 7 43 16 43 35"
              fill="#FFFFFF"
              stroke="#38292C"
              strokeWidth="2.5"
            />
            {/* Blushes */}
            <ellipse cx="14" cy="22" rx="3.5" ry="2" fill="#FFAEC0" />
            <ellipse cx="36" cy="22" rx="3.5" ry="2" fill="#FFAEC0" />
            {/* Eyes */}
            <path d="M17 18 Q20 15 22 18" stroke="#38292C" strokeWidth="2.2" strokeLinecap="round" />
            <path d="M28 18 Q30 15 33 18" stroke="#38292C" strokeWidth="2.2" strokeLinecap="round" />
            {/* Smile */}
            <path d="M23 23 Q25 25 27 23" stroke="#38292C" strokeWidth="2" strokeLinecap="round" />
            {/* Ear nubs */}
            <path d="M11 13 Q9 8 14 10" stroke="#38292C" strokeWidth="2" fill="#FFFFFF" />
            <path d="M39 13 Q41 8 36 10" stroke="#38292C" strokeWidth="2" fill="#FFFFFF" />
          </svg>
        </div>
      </div>

      {/* 2x2 Grid of White Metric Cards */}
      <div className="grid grid-cols-2 gap-3.5">
        {/* Card 1: Total break time */}
        <div className="bg-white rounded-2xl p-3.5 border border-[#E3EDF6] shadow-xs flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-[#FFEAEF] flex items-center justify-center shrink-0 border border-[#FFCCD7]">
            {/* Clock icon */}
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <circle cx="12" cy="12" r="9" stroke="#E8556D" strokeWidth="2" />
              <path d="M12 7 V12 L15 14" stroke="#E8556D" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </div>
          <div>
            <div className="text-lg md:text-xl font-bold text-[#3A2C29] leading-tight">
              {formatStatsTime(stats.totalSeconds)}
            </div>
            <div className="text-xs text-[#8A7B77] font-medium leading-tight mt-0.5">
              total break time
            </div>
          </div>
        </div>

        {/* Card 2: Breaks taken */}
        <div className="bg-white rounded-2xl p-3.5 border border-[#E3EDF6] shadow-xs flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-[#EBF7FF] flex items-center justify-center shrink-0 border border-[#D0EAFA]">
            {/* Steaming Mug doodle */}
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
              <rect x="5" y="7" width="12" height="13" rx="3" fill="#EBF4FE" stroke="#3B82F6" strokeWidth="1.8" />
              <path d="M17 10 C19 10 20.5 11 20.5 13 C20.5 15 19 16 17 16" stroke="#3B82F6" strokeWidth="1.8" />
              {/* Steam */}
              <path d="M8 3 Q9 4.5 8 6" stroke="#93C5FD" strokeWidth="1.5" strokeLinecap="round" />
              <path d="M12 2 Q13 3.5 12 5" stroke="#93C5FD" strokeWidth="1.5" strokeLinecap="round" />
              {/* Tiny face on mug */}
              <circle cx="9" cy="13" r="0.8" fill="#1D4ED8" />
              <circle cx="13" cy="13" r="0.8" fill="#1D4ED8" />
              <path d="M10.5 15 Q11 16 11.5 15" stroke="#1D4ED8" strokeWidth="1" strokeLinecap="round" />
            </svg>
          </div>
          <div>
            <div className="text-lg md:text-xl font-bold text-[#3A2C29] leading-tight">
              {stats.count}
            </div>
            <div className="text-xs text-[#8A7B77] font-medium leading-tight mt-0.5">
              breaks taken
            </div>
          </div>
        </div>

        {/* Card 3: Longest break */}
        <div className="bg-white rounded-2xl p-3.5 border border-[#E3EDF6] shadow-xs flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-[#FFF5EB] flex items-center justify-center shrink-0 border border-[#FEE1C7]">
            {/* Cute blob face icon */}
            <svg width="22" height="22" viewBox="0 0 30 30" fill="none">
              <path
                d="M5 19 C5 11 11 6 15 6 C19 6 25 11 25 19 C25 23 21 24 15 24 C9 24 5 23 5 19 Z"
                fill="#FFF"
                stroke="#F97316"
                strokeWidth="2"
              />
              <circle cx="11" cy="14" r="1.3" fill="#C2410C" />
              <circle cx="19" cy="14" r="1.3" fill="#C2410C" />
              <path d="M13.5 17 Q15 19 16.5 17" stroke="#C2410C" strokeWidth="1.4" strokeLinecap="round" fill="none" />
              <ellipse cx="8.5" cy="16" rx="1.5" ry="1" fill="#FDBA74" />
              <ellipse cx="21.5" cy="16" rx="1.5" ry="1" fill="#FDBA74" />
            </svg>
          </div>
          <div>
            <div className="text-lg md:text-xl font-bold text-[#3A2C29] leading-tight">
              {formatStatsTime(stats.longestSeconds)}
            </div>
            <div className="text-xs text-[#8A7B77] font-medium leading-tight mt-0.5">
              longest break
            </div>
          </div>
        </div>

        {/* Card 4: Average break */}
        <div className="bg-white rounded-2xl p-3.5 border border-[#E3EDF6] shadow-xs flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-[#EDFBEF] flex items-center justify-center shrink-0 border border-[#CFECD4]">
            {/* Sprout / leaf icon */}
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
              <path
                d="M12 21 V13 M12 13 C12 8 8 7 8 7 C8 12 12 13 12 13 Z"
                fill="#DCFCE7"
                stroke="#16A34A"
                strokeWidth="1.8"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
              <path
                d="M12 15 C14 11 18 11 18 11 C18 15 14 16 12 15 Z"
                fill="#BBF7D0"
                stroke="#16A34A"
                strokeWidth="1.8"
                strokeLinecap="round"
                strokeLinejoin="round"
              />
            </svg>
          </div>
          <div>
            <div className="text-lg md:text-xl font-bold text-[#3A2C29] leading-tight">
              {formatStatsTime(stats.averageSeconds)}
            </div>
            <div className="text-xs text-[#8A7B77] font-medium leading-tight mt-0.5">
              average break
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
