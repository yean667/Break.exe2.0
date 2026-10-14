import React, { useState } from 'react';
import { sound } from '../utils/audio';
import { CharacterConfig } from '../types';

interface DancingBlobProps {
  isDancing: boolean;
  expression?: 'dancing' | 'happy' | 'sleepy' | 'confused';
  config?: CharacterConfig;
  onPoke?: () => void;
}

export const DancingBlob: React.FC<DancingBlobProps> = ({
  isDancing,
  expression = 'dancing',
  config = {
    model: 'blob',
    name: 'Mochi Blob',
    bodyColor: '#FFFFFF',
    headphoneColor: '#FFAEC0',
    accessory: 'headphones',
    blushColor: '#FFAEC0',
  },
  onPoke,
}) => {
  const [isPoked, setIsPoked] = useState(false);
  const [pokeCount, setPokeCount] = useState(0);

  const handleBlobClick = () => {
    sound.playGiggle();
    setIsPoked(true);
    setPokeCount((c) => c + 1);
    if (onPoke) onPoke();
    setTimeout(() => setIsPoked(false), 500);
  };

  const currentExpression = isPoked ? 'happy' : expression;
  const bodyFill = config.bodyColor || '#FFFFFF';
  const blushFill = config.blushColor || '#FFAEC0';
  const headphoneFill = config.headphoneColor || '#FFAEC0';
  const strokeColor = '#38292C';

  return (
    <div
      onClick={handleBlobClick}
      title="Click me to squish! :3"
      className="relative flex items-center justify-center cursor-pointer select-none group py-1"
    >
      {/* Motion marks (( on left and )) on right */}
      {isDancing && (
        <>
          <div className="absolute -left-3 top-1/2 -translate-y-1/2 text-[#3D2E2B] font-bold text-lg animate-wiggle-marks select-none pointer-events-none opacity-60">
            ((
          </div>
          <div className="absolute -right-3 top-1/2 -translate-y-1/2 text-[#3D2E2B] font-bold text-lg animate-wiggle-marks select-none pointer-events-none opacity-60">
            ))
          </div>
        </>
      )}

      {/* Floating Musical Notes when dancing */}
      {isDancing && (
        <div className="absolute inset-0 pointer-events-none overflow-visible">
          <div className="absolute left-2 top-2 text-[#3D2E2B] font-bold text-xl animate-note-1 select-none">
            ♫
          </div>
          <div className="absolute right-4 top-1 text-[#3D2E2B] font-bold text-lg animate-note-2 select-none">
            ♪
          </div>
          <div className="absolute right-1 bottom-4 text-[#3D2E2B] font-bold text-base animate-note-3 select-none">
            ♬
          </div>
        </div>
      )}

      {/* Floating ZZZ when sleepy */}
      {expression === 'sleepy' && !isDancing && (
        <div className="absolute -top-3 right-6 pointer-events-none font-doodle text-sm font-bold text-[#E87588] animate-pulse">
          z<span className="text-xs">z</span><span className="text-[10px]">z</span>
        </div>
      )}

      {/* The Animated Character Container */}
      <div
        className={`transition-transform duration-200 ${
          isPoked
            ? 'scale-110 -rotate-3'
            : isDancing
            ? 'animate-cute-dance'
            : 'animate-cute-sway'
        }`}
      >
        <svg
          width="135"
          height="135"
          viewBox="0 0 140 140"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className="drop-shadow-sm filter overflow-visible"
        >
          {/* Ground Shadow */}
          <ellipse
            cx="70"
            cy="125"
            rx="36"
            ry="7"
            fill="#EADCDA"
            opacity="0.65"
          />

          {/* Dancing Feet / Paws */}
          <g className={isDancing ? 'animate-pulse' : ''}>
            <ellipse
              cx="54"
              cy="118"
              rx="9"
              ry="6"
              fill={bodyFill}
              stroke={strokeColor}
              strokeWidth="3.2"
            />
            <ellipse
              cx="86"
              cy="118"
              rx="9"
              ry="6"
              fill={bodyFill}
              stroke={strokeColor}
              strokeWidth="3.2"
            />
          </g>

          {/* ============================================== */}
          {/* CHARACTER BODIES & EARS BASED ON MODEL         */}
          {/* ============================================== */}
          
          {/* 1. BUNNY EARS */}
          {config.model === 'bunny' && (
            <g className={isDancing ? 'animate-cute-sway' : ''}>
              {/* Left Bunny Ear */}
              <path
                d="M48 50 C40 22 42 10 52 10 C62 10 60 25 56 50 Z"
                fill={bodyFill}
                stroke={strokeColor}
                strokeWidth="3.2"
              />
              <path
                d="M50 44 C45 25 46 16 52 16 C58 16 57 26 54 44 Z"
                fill={blushFill}
                opacity="0.65"
              />
              {/* Right Bunny Ear */}
              <path
                d="M84 50 C80 25 78 10 88 10 C98 10 100 22 92 50 Z"
                fill={bodyFill}
                stroke={strokeColor}
                strokeWidth="3.2"
              />
              <path
                d="M86 44 C83 26 82 16 88 16 C94 16 95 25 90 44 Z"
                fill={blushFill}
                opacity="0.65"
              />
            </g>
          )}

          {/* 2. BEAR EARS */}
          {config.model === 'bear' && (
            <g>
              {/* Left Bear Ear */}
              <circle cx="44" cy="48" r="14" fill={bodyFill} stroke={strokeColor} strokeWidth="3.2" />
              <circle cx="44" cy="48" r="7" fill={blushFill} opacity="0.65" />
              {/* Right Bear Ear */}
              <circle cx="96" cy="48" r="14" fill={bodyFill} stroke={strokeColor} strokeWidth="3.2" />
              <circle cx="96" cy="48" r="7" fill={blushFill} opacity="0.65" />
            </g>
          )}

          {/* 3. SLIME BODY */}
          {config.model === 'slime' ? (
            <g>
              {/* Droplet Slime shape with bottom spread */}
              <path
                d="M70 38 C90 38 112 60 114 90 C116 112 128 118 120 123 C108 128 32 128 20 123 C12 118 24 112 26 90 C28 60 50 38 70 38 Z"
                fill={bodyFill}
                stroke={strokeColor}
                strokeWidth="3.4"
                strokeLinejoin="round"
              />
              {/* Glossy highlights on slime */}
              <path
                d="M45 58 C52 50 64 47 68 47"
                stroke="#FFFFFF"
                strokeWidth="3.5"
                strokeLinecap="round"
              />
              <circle cx="40" cy="65" r="2.5" fill="#FFFFFF" />
            </g>
          ) : config.model === 'cloud' ? (
            /* 4. CLOUD BODY */
            <g>
              <path
                d="M36 105 C22 105 18 90 26 80 C20 70 28 55 42 55 C46 42 64 40 72 44 C82 38 100 42 102 55 C116 55 124 70 118 82 C126 92 120 105 106 105 C98 122 42 122 36 105 Z"
                fill={bodyFill}
                stroke={strokeColor}
                strokeWidth="3.4"
                strokeLinejoin="round"
              />
            </g>
          ) : (
            /* 1 & DEFAULT: CHUBBY BLOB / BUNNY / BEAR BODY */
            <path
              d="M34 105 C24 72 38 42 70 42 C102 42 116 72 106 105 C102 118 88 122 70 122 C52 122 38 118 34 105 Z"
              fill={bodyFill}
              stroke={strokeColor}
              strokeWidth="3.4"
              strokeLinejoin="round"
            />
          )}

          {/* Left Arm waving / dancing */}
          <g className={isDancing ? 'animate-arm-left' : ''}>
            <path
              d="M36 82 C22 76 18 64 28 66 C35 67 40 76 40 78"
              fill={bodyFill}
              stroke={strokeColor}
              strokeWidth="3.2"
              strokeLinecap="round"
            />
          </g>

          {/* Right Arm waving / dancing */}
          <g className={isDancing ? 'animate-arm-right' : ''}>
            <path
              d="M104 82 C118 76 122 64 112 66 C105 67 100 76 100 78"
              fill={bodyFill}
              stroke={strokeColor}
              strokeWidth="3.2"
              strokeLinecap="round"
            />
          </g>

          {/* Cheeks - cute customized blush */}
          <ellipse cx="49" cy="85" rx="5.5" ry="3.5" fill={blushFill} />
          <ellipse cx="91" cy="85" rx="5.5" ry="3.5" fill={blushFill} />

          {/* Bear muzzle detail */}
          {config.model === 'bear' && (
            <ellipse cx="70" cy="82" rx="10" ry="7" fill="#FFFFFF" opacity="0.8" />
          )}

          {/* Face Expressions */}
          {currentExpression === 'dancing' && (
            <g>
              <path
                d="M54 75 Q60 68 66 75"
                stroke={strokeColor}
                strokeWidth="3.2"
                strokeLinecap="round"
                fill="none"
              />
              <path
                d="M74 75 Q80 68 86 75"
                stroke={strokeColor}
                strokeWidth="3.2"
                strokeLinecap="round"
                fill="none"
              />
              {config.model === 'bear' ? (
                <>
                  <ellipse cx="70" cy="79" rx="2.5" ry="1.8" fill={strokeColor} />
                  <path d="M68 82 Q70 85 72 82" stroke={strokeColor} strokeWidth="2.2" strokeLinecap="round" fill="none" />
                </>
              ) : (
                <path
                  d="M66 84 Q70 89 74 84"
                  stroke={strokeColor}
                  strokeWidth="2.8"
                  strokeLinecap="round"
                  fill="none"
                />
              )}
            </g>
          )}

          {currentExpression === 'happy' && (
            <g>
              <circle cx="60" cy="74" r="3.2" fill={strokeColor} />
              <circle cx="80" cy="74" r="3.2" fill={strokeColor} />
              <circle cx="61.5" cy="72.5" r="1.1" fill="#FFFFFF" />
              <circle cx="81.5" cy="72.5" r="1.1" fill="#FFFFFF" />
              {config.model === 'bear' && (
                <ellipse cx="70" cy="79" rx="2.5" ry="1.8" fill={strokeColor} />
              )}
              <path
                d="M65 82 Q70 87 75 82"
                stroke={strokeColor}
                strokeWidth="2.8"
                strokeLinecap="round"
                fill="none"
              />
            </g>
          )}

          {currentExpression === 'sleepy' && (
            <g>
              <path
                d="M54 76 Q60 81 66 76"
                stroke={strokeColor}
                strokeWidth="3"
                strokeLinecap="round"
                fill="none"
              />
              <path
                d="M74 76 Q80 81 86 76"
                stroke={strokeColor}
                strokeWidth="3"
                strokeLinecap="round"
                fill="none"
              />
              <path
                d="M68 84 Q70 86 72 84"
                stroke={strokeColor}
                strokeWidth="2.4"
                strokeLinecap="round"
                fill="none"
              />
            </g>
          )}

          {currentExpression === 'confused' && (
            <g>
              <circle cx="59" cy="74" r="3.2" fill={strokeColor} />
              <path
                d="M76 75 Q80 71 85 75"
                stroke={strokeColor}
                strokeWidth="3"
                strokeLinecap="round"
                fill="none"
              />
              <path
                d="M66 84 Q70 82 74 85"
                stroke={strokeColor}
                strokeWidth="2.6"
                strokeLinecap="round"
                fill="none"
              />
              <path
                d="M96 66 C96 62 98 58 98 58 C98 58 100 62 100 66 C100 68 98 70 96 66 Z"
                fill="#BCE0FD"
                stroke={strokeColor}
                strokeWidth="1.5"
              />
            </g>
          )}

          {/* ============================================== */}
          {/* ACCESSORIES                                   */}
          {/* ============================================== */}

          {/* ACCESSORY 1: HEADPHONES */}
          {config.accessory === 'headphones' && (
            <g className={isDancing ? 'animate-headphone' : ''}>
              {/* Dark Headband Arched Over Head */}
              <path
                d="M32 68 C32 30 108 30 108 68"
                stroke={strokeColor}
                strokeWidth="5.5"
                strokeLinecap="round"
                fill="none"
              />
              {/* Top Cushion on Headband */}
              <path
                d="M48 37 C60 32 80 32 92 37"
                stroke={headphoneFill}
                strokeWidth="4"
                strokeLinecap="round"
                fill="none"
              />

              {/* Left Ear Cushion */}
              <rect
                x="25"
                y="56"
                width="13"
                height="26"
                rx="6.5"
                fill={headphoneFill}
                stroke={strokeColor}
                strokeWidth="3.2"
              />
              <rect
                x="28"
                y="60"
                width="7"
                height="18"
                rx="3.5"
                fill="#FFFFFF"
                opacity="0.3"
              />
              <path d="M31 56 L31 51" stroke={strokeColor} strokeWidth="3" strokeLinecap="round" />

              {/* Right Ear Cushion */}
              <rect
                x="102"
                y="56"
                width="13"
                height="26"
                rx="6.5"
                fill={headphoneFill}
                stroke={strokeColor}
                strokeWidth="3.2"
              />
              <rect
                x="105"
                y="60"
                width="7"
                height="18"
                rx="3.5"
                fill="#FFFFFF"
                opacity="0.3"
              />
              <path d="M109 56 L109 51" stroke={strokeColor} strokeWidth="3" strokeLinecap="round" />
            </g>
          )}

          {/* ACCESSORY 2: PASTEL BOW (from image 3) */}
          {config.accessory === 'bow' && (
            <g className={isDancing ? 'animate-headphone' : ''} transform="translate(70, 38)">
              {/* Left bow loop */}
              <path
                d="M-3 -4 C-16 -16 -24 -4 -16 6 C-10 10 -4 4 -2 0 Z"
                fill={headphoneFill}
                stroke={strokeColor}
                strokeWidth="2.8"
              />
              {/* Right bow loop */}
              <path
                d="M3 -4 C16 -16 24 -4 16 6 C10 10 4 4 2 0 Z"
                fill={headphoneFill}
                stroke={strokeColor}
                strokeWidth="2.8"
              />
              {/* Center knot */}
              <circle cx="0" cy="0" r="4.5" fill="#FFFFFF" stroke={strokeColor} strokeWidth="2.5" />
              {/* Ribbons hanging */}
              <path d="M-2 4 L-6 15" stroke={strokeColor} strokeWidth="2.5" strokeLinecap="round" />
              <path d="M2 4 L6 15" stroke={strokeColor} strokeWidth="2.5" strokeLinecap="round" />
            </g>
          )}

          {/* ACCESSORY 3: TINY SPROUT */}
          {config.accessory === 'sprout' && (
            <g className={isDancing ? 'animate-headphone' : ''} transform="translate(70, 36)">
              {/* Stem */}
              <path d="M0 6 V-4" stroke="#16A34A" strokeWidth="2.8" strokeLinecap="round" />
              {/* Left Leaf */}
              <path
                d="M0 -4 C-8 -12 -12 -2 -2 -2 Z"
                fill="#86EFAC"
                stroke="#16A34A"
                strokeWidth="2"
              />
              {/* Right Leaf */}
              <path
                d="M0 -4 C8 -12 12 -2 2 -2 Z"
                fill="#4ADE80"
                stroke="#16A34A"
                strokeWidth="2"
              />
            </g>
          )}

          {/* ACCESSORY 4: STAR PIN */}
          {config.accessory === 'star' && (
            <g className={isDancing ? 'animate-headphone' : ''} transform="translate(88, 48)">
              <polygon
                points="0,-8 2.5,-2.5 8,0 2.5,2.5 0,8 -2.5,2.5 -8,0 -2.5,-2.5"
                fill="#FBBF24"
                stroke={strokeColor}
                strokeWidth="2"
              />
              <circle cx="0" cy="0" r="1.5" fill="#FFFFFF" />
            </g>
          )}
        </svg>
      </div>

      {/* Floating mini speech bubble if poked */}
      {isPoked && (
        <div className="absolute -top-7 left-1/2 -translate-x-1/2 bg-white px-2.5 py-0.5 rounded-full border border-[#FFCCD5] shadow-xs text-xs font-doodle text-[#E8556D] whitespace-nowrap z-20 animate-bounce">
          {pokeCount % 4 === 0
            ? 'giggle! :3'
            : pokeCount % 4 === 1
            ? 'you poked me!'
            : pokeCount % 4 === 2
            ? 'best study buddy ever!'
            : 'dance dance! ♫'}
        </div>
      )}
    </div>
  );
};
