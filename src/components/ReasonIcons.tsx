import React from 'react';
import { BreakReason } from '../types';

interface ReasonIconProps {
  reason: BreakReason;
  className?: string;
  size?: number;
}

export const ReasonIcon: React.FC<ReasonIconProps> = ({ reason, className = '', size = 20 }) => {
  switch (reason) {
    case 'water':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          <path
            d="M12 2.5 C12 2.5 5 11 5 16 C5 19.8 8.1 23 12 23 C15.9 23 19 19.8 19 16 C19 11 12 2.5 12 2.5 Z"
            fill="#E0F2FE"
            stroke="#0284C7"
            strokeWidth="1.8"
            strokeLinejoin="round"
          />
          <path
            d="M14.5 14 C14.5 17 12 18.5 10 18.5"
            stroke="#38BDF8"
            strokeWidth="1.4"
            strokeLinecap="round"
          />
        </svg>
      );
    case 'food':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Cute toast / snack with smiley */}
          <rect
            x="3.5"
            y="4.5"
            width="17"
            height="15"
            rx="4"
            fill="#FEF3C7"
            stroke="#D97706"
            strokeWidth="1.8"
          />
          <circle cx="8.5" cy="11.5" r="1.1" fill="#78350F" />
          <circle cx="15.5" cy="11.5" r="1.1" fill="#78350F" />
          <path d="M10.5 13.5 Q12 15 13.5 13.5" stroke="#78350F" strokeWidth="1.4" strokeLinecap="round" />
          <ellipse cx="6.5" cy="12.5" rx="1.2" ry="0.8" fill="#FCA5A5" />
          <ellipse cx="17.5" cy="12.5" rx="1.2" ry="0.8" fill="#FCA5A5" />
        </svg>
      );
    case 'phone':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          <rect
            x="6"
            y="2.5"
            width="12"
            height="19"
            rx="3.5"
            fill="#F1F5F9"
            stroke="#475569"
            strokeWidth="1.8"
          />
          <rect x="8" y="5" width="8" height="12" rx="1" fill="#E2E8F0" />
          <circle cx="12" cy="19" r="0.9" fill="#64748B" />
          <line x1="10" y1="3.8" x2="14" y2="3.8" stroke="#64748B" strokeWidth="1" strokeLinecap="round" />
        </svg>
      );
    case 'bathroom':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Cute toilet / water cup fixture */}
          <path
            d="M6 5 H18 V10 C18 13.5 15.3 16 12 16 C8.7 16 6 13.5 6 10 V5 Z"
            fill="#E0F2FE"
            stroke="#0284C7"
            strokeWidth="1.8"
          />
          <path d="M9 16 V20 H15 V16" stroke="#0284C7" strokeWidth="1.8" strokeLinecap="round" />
          <line x1="7" y1="20" x2="17" y2="20" stroke="#0284C7" strokeWidth="1.8" strokeLinecap="round" />
        </svg>
      );
    case 'brain':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Cute pink brain */}
          <path
            d="M9 4.5 C6.5 4.5 4.5 6.5 4.5 9 C4.5 10 4.8 11 5.3 11.8 C4.5 12.5 4 13.6 4 14.8 C4 17.1 5.9 19 8.2 19 C8.8 19 9.4 18.8 10 18.5 C10.6 19.4 11.5 20 12.6 20 C14.3 20 15.7 18.8 16 17.2 C16.6 17.5 17.3 17.7 18 17.7 C19.9 17.7 21.5 16.1 21.5 14.2 C21.5 13.2 21.1 12.2 20.4 11.6 C20.8 10.8 21 10 21 9 C21 6.8 19.2 5 17 5 C16.5 5 16 5.1 15.6 5.3 C14.8 4.2 13.5 3.5 12 3.5 C10.8 3.5 9.8 4 9 4.5 Z"
            fill="#FFE4E6"
            stroke="#E11D48"
            strokeWidth="1.6"
            strokeLinejoin="round"
          />
          <path d="M12 6 V17" stroke="#FDA4AF" strokeWidth="1.5" strokeLinecap="round" />
        </svg>
      );
    case 'grass':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Sprout / leaf */}
          <path
            d="M12 21 V12 M12 12 C12 7 7 6 7 6 C7 11 12 12 12 12 Z"
            fill="#DCFCE7"
            stroke="#16A34A"
            strokeWidth="1.8"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
          <path
            d="M12 15 C14 11 19 11 19 11 C19 16 14 16 12 15 Z"
            fill="#BBF7D0"
            stroke="#16A34A"
            strokeWidth="1.8"
            strokeLinecap="round"
            strokeLinejoin="round"
          />
        </svg>
      );
    case 'existing':
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Cute sleepy cat/blob */}
          <path
            d="M4.5 15 C4.5 10 8 7 12 7 C16 7 19.5 10 19.5 15 C19.5 18 16 19.5 12 19.5 C8 19.5 4.5 18 4.5 15 Z"
            fill="#FFFFFF"
            stroke="#475569"
            strokeWidth="1.8"
          />
          <path d="M6 9 L8 12 M18 9 L16 12" stroke="#475569" strokeWidth="1.6" strokeLinecap="round" />
          <path d="M9 14 Q10.5 12 12 14" stroke="#475569" strokeWidth="1.5" strokeLinecap="round" fill="none" />
          <path d="M12 14 Q13.5 12 15 14" stroke="#475569" strokeWidth="1.5" strokeLinecap="round" fill="none" />
        </svg>
      );
    case 'other':
    default:
      return (
        <svg
          width={size}
          height={size}
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={className}
        >
          {/* Sparkles */}
          <path
            d="M12 2 L13.5 7.5 L19 9 L13.5 10.5 L12 16 L10.5 10.5 L5 9 L10.5 7.5 Z"
            fill="#FEF08A"
            stroke="#CA8A04"
            strokeWidth="1.5"
          />
          <circle cx="18" cy="17" r="1.5" fill="#FACC15" />
          <circle cx="6" cy="18" r="1" fill="#FACC15" />
        </svg>
      );
  }
};
