import React from 'react';

// Tiny cute blob avatar used in header and status pills
export const BlobAvatar: React.FC<{
  className?: string;
  size?: number;
  mood?: 'happy' | 'cheeky' | 'sleepy';
  bodyColor?: string;
  blushColor?: string;
}> = ({
  className = '',
  size = 32,
  mood = 'happy',
  bodyColor = '#FFFFFF',
  blushColor = '#FFB2C0',
}) => (
  <svg
    width={size}
    height={size}
    viewBox="0 0 40 40"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    className={`inline-block ${className}`}
  >
    {/* Blob body */}
    <path
      d="M8 26C6 17 14 10 20 10C26 10 34 17 32 26C31 31 27 33 20 33C13 33 9 31 8 26Z"
      fill={bodyColor}
      stroke="#3A2C29"
      strokeWidth="2.2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    {/* Blushes */}
    <ellipse cx="12" cy="24" rx="2.5" ry="1.5" fill={blushColor} />
    <ellipse cx="28" cy="24" rx="2.5" ry="1.5" fill={blushColor} />
    {/* Eyes */}
    {mood === 'sleepy' ? (
      <>
        <path d="M14 20 Q16 23 18 20" stroke="#3A2C29" strokeWidth="2" strokeLinecap="round" />
        <path d="M22 20 Q24 23 26 20" stroke="#3A2C29" strokeWidth="2" strokeLinecap="round" />
      </>
    ) : mood === 'cheeky' ? (
      <>
        <circle cx="16" cy="19" r="1.8" fill="#3A2C29" />
        <circle cx="24" cy="19" r="1.8" fill="#3A2C29" />
      </>
    ) : (
      <>
        <path d="M14 20 Q16 17 18 20" stroke="#3A2C29" strokeWidth="2.2" strokeLinecap="round" />
        <path d="M22 20 Q24 17 26 20" stroke="#3A2C29" strokeWidth="2.2" strokeLinecap="round" />
      </>
    )}
    {/* Smile */}
    <path
      d="M18 24 Q20 27 22 24"
      stroke="#3A2C29"
      strokeWidth="2"
      strokeLinecap="round"
      fill="none"
    />
    {/* Cat-like ears / tiny bumps */}
    <path d="M11 14 Q10 9 14 12" stroke="#3A2C29" strokeWidth="2" strokeLinecap="round" fill={bodyColor} />
    <path d="M29 14 Q30 9 26 12" stroke="#3A2C29" strokeWidth="2" strokeLinecap="round" fill={bodyColor} />
  </svg>
);

// Peeking Blob doodle with speech line
export const PeekingBlob: React.FC<{ text?: string }> = ({ text = "no judgment... ~ maybe" }) => (
  <div className="flex items-center gap-1.5 select-none pointer-events-none">
    <svg width="42" height="30" viewBox="0 0 50 35" fill="none" className="shrink-0">
      {/* Head peeking from bottom */}
      <path
        d="M6 35 C6 18 16 8 25 8 C34 8 44 18 44 35"
        fill="#FFFFFF"
        stroke="#3D2E2B"
        strokeWidth="2.5"
      />
      {/* Blushes */}
      <ellipse cx="14" cy="22" rx="3.5" ry="2" fill="#FFAEC0" />
      <ellipse cx="36" cy="22" rx="3.5" ry="2" fill="#FFAEC0" />
      {/* Eyes */}
      <circle cx="19" cy="18" r="2.2" fill="#3D2E2B" />
      <circle cx="31" cy="18" r="2.2" fill="#3D2E2B" />
      {/* Tiny smile */}
      <path d="M22 23 Q25 26 28 23" stroke="#3D2E2B" strokeWidth="2" strokeLinecap="round" fill="none" />
      {/* Tiny ear nubs */}
      <path d="M10 14 Q9 8 15 11" stroke="#3D2E2B" strokeWidth="2.2" fill="#FFFFFF" />
      <path d="M40 14 Q41 8 35 11" stroke="#3D2E2B" strokeWidth="2.2" fill="#FFFFFF" />
    </svg>
    <div className="font-doodle text-xs md:text-sm text-[#7D6B66] leading-tight">
      {text.split('~').map((part, i) => (
        <React.Fragment key={i}>
          {i > 0 && <span className="opacity-75 block text-[11px] md:text-xs">~ {part.trim()}</span>}
          {i === 0 && <span>{part.trim()}</span>}
        </React.Fragment>
      ))}
    </div>
  </div>
);

// Stack of books doodle
export const BooksStackDoodle: React.FC<{ className?: string }> = ({ className = '' }) => (
  <svg
    width="54"
    height="42"
    viewBox="0 0 65 50"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    className={className}
  >
    {/* Bottom book - baby blue */}
    <rect
      x="8"
      y="33"
      width="48"
      height="12"
      rx="3"
      fill="#DCEBFA"
      stroke="#3D2E2B"
      strokeWidth="2.5"
    />
    <line x1="8" y1="39" x2="56" y2="39" stroke="#3D2E2B" strokeWidth="1.5" strokeDasharray="3 3" opacity="0.4" />
    {/* Middle book - pastel lavender */}
    <rect
      x="12"
      y="22"
      width="42"
      height="11"
      rx="3"
      fill="#F5E4F8"
      stroke="#3D2E2B"
      strokeWidth="2.5"
    />
    <line x1="12" y1="27" x2="54" y2="27" stroke="#3D2E2B" strokeWidth="1.5" strokeDasharray="3 3" opacity="0.4" />
    {/* Top book - pastel pink */}
    <rect
      x="16"
      y="11"
      width="36"
      height="11"
      rx="3"
      fill="#FFDFE5"
      stroke="#3D2E2B"
      strokeWidth="2.5"
    />
    {/* Bookmark ribbon */}
    <path
      d="M44 11 V20 L47 17 L50 20 V11"
      fill="#FFA8BA"
      stroke="#3D2E2B"
      strokeWidth="1.5"
    />
  </svg>
);

// Curved doodle arrows
export const CurvedArrowLeft: React.FC<{ className?: string }> = ({ className = '' }) => (
  <svg
    width="34"
    height="24"
    viewBox="0 0 40 30"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    className={className}
  >
    <path
      d="M6 8 C18 10 28 16 34 26"
      stroke="#7D6B66"
      strokeWidth="2"
      strokeLinecap="round"
      strokeDasharray="0"
    />
    <path
      d="M34 26 L26 24 M34 26 L32 18"
      stroke="#7D6B66"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

export const CurvedArrowRight: React.FC<{ className?: string }> = ({ className = '' }) => (
  <svg
    width="34"
    height="24"
    viewBox="0 0 40 30"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    className={className}
  >
    <path
      d="M34 8 C22 10 12 16 6 26"
      stroke="#7D6B66"
      strokeWidth="2"
      strokeLinecap="round"
    />
    <path
      d="M6 26 L14 24 M6 26 L8 18"
      stroke="#7D6B66"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);

// Radiating dash marks for timer
export const RadiatingTicks: React.FC<{ side: 'left' | 'right' }> = ({ side }) => {
  if (side === 'left') {
    return (
      <svg width="28" height="24" viewBox="0 0 35 30" fill="none" className="shrink-0 text-[#FFA8BA]">
        <line x1="28" y1="15" x2="8" y2="7" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
        <line x1="30" y1="20" x2="10" y2="20" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
        <line x1="28" y1="25" x2="12" y2="33" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
      </svg>
    );
  }
  return (
    <svg width="28" height="24" viewBox="0 0 35 30" fill="none" className="shrink-0 text-[#FFA8BA]">
      <line x1="7" y1="15" x2="27" y2="7" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
      <line x1="5" y1="20" x2="25" y2="20" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
      <line x1="7" y1="25" x2="23" y2="33" stroke="currentColor" strokeWidth="3" strokeLinecap="round" />
    </svg>
  );
};
