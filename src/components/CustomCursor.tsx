import React, { useEffect, useState, useRef } from 'react';

/**
 * Little Pastel Daisy Cursor 🌼
 * Follows mouse smoothly with zero latency, wiggles gently over clickables,
 * and bounces playfully on clicks.
 */
export const CustomCursor: React.FC = () => {
  const [isVisible, setIsVisible] = useState(false);
  const [isHoveringClickable, setIsHoveringClickable] = useState(false);
  const [isOverTextInput, setIsOverTextInput] = useState(false);
  const [isClicking, setIsClicking] = useState(false);
  const [isTouchDevice, setIsTouchDevice] = useState(false);

  const cursorRef = useRef<HTMLDivElement>(null);
  const flowerRef = useRef<HTMLDivElement>(null);
  const lastPos = useRef({ x: -100, y: -100 });
  const tiltTimeoutRef = useRef<number | null>(null);
  const clickTimeoutRef = useRef<number | null>(null);
  const reducedMotionRef = useRef(false);

  useEffect(() => {
    // 1. Check if device is touch or coarse pointer
    const checkTouch = () => {
      const isTouch =
        'ontouchstart' in window ||
        navigator.maxTouchPoints > 0 ||
        window.matchMedia('(pointer: coarse)').matches;
      setIsTouchDevice(isTouch);
      return isTouch;
    };

    if (checkTouch()) {
      return;
    }

    // 2. Check prefers-reduced-motion
    reducedMotionRef.current = window.matchMedia(
      '(prefers-reduced-motion: reduce)'
    ).matches;

    // 3. Mouse move tracking with direct DOM transform for 0ms lag
    const handleMouseMove = (e: MouseEvent) => {
      if (cursorRef.current) {
        cursorRef.current.style.transform = `translate3d(${e.clientX}px, ${e.clientY}px, 0)`;
      }

      if (!isVisible) {
        setIsVisible(true);
      }

      // Check element under pointer
      const target = e.target as HTMLElement | null;
      if (target) {
        // Detect text inputs
        const isInput =
          target.tagName === 'INPUT' &&
          !['button', 'submit', 'checkbox', 'radio', 'range', 'color'].includes(
            (target as HTMLInputElement).type
          );
        const isTextarea = target.tagName === 'TEXTAREA';
        const isEditable = target.isContentEditable;
        const textTarget = isInput || isTextarea || isEditable;
        setIsOverTextInput(textTarget);

        // Detect clickables (buttons, links, interactive items)
        if (!textTarget) {
          const clickable = Boolean(
            target.closest('button') ||
              target.closest('a') ||
              target.closest('[role="button"]') ||
              target.closest('input[type="range"]') ||
              target.closest('.cursor-pointer') ||
              window.getComputedStyle(target).cursor === 'pointer'
          );
          setIsHoveringClickable(clickable);
        } else {
          setIsHoveringClickable(false);
        }
      }

      // Subtle dynamic tilt while moving (disabled for reduced motion)
      if (!reducedMotionRef.current && flowerRef.current) {
        const dx = e.clientX - lastPos.current.x;
        const tilt = Math.max(-14, Math.min(14, dx * 1.2));
        flowerRef.current.style.transform = `translate(-50%, -50%) rotate(${tilt}deg)`;

        if (tiltTimeoutRef.current) {
          clearTimeout(tiltTimeoutRef.current);
        }
        tiltTimeoutRef.current = window.setTimeout(() => {
          if (flowerRef.current) {
            flowerRef.current.style.transform = 'translate(-50%, -50%) rotate(0deg)';
          }
        }, 80);
      }

      lastPos.current = { x: e.clientX, y: e.clientY };
    };

    // 4. Mouse click bounce
    const handleMouseDown = () => {
      if (reducedMotionRef.current) return;
      setIsClicking(true);
    };

    const handleMouseUp = () => {
      if (reducedMotionRef.current) return;
      if (clickTimeoutRef.current) clearTimeout(clickTimeoutRef.current);
      clickTimeoutRef.current = window.setTimeout(() => {
        setIsClicking(false);
      }, 260);
    };

    // 5. Visibility handlers
    const handleMouseEnter = () => setIsVisible(true);
    const handleMouseLeave = () => setIsVisible(false);

    window.addEventListener('mousemove', handleMouseMove, { passive: true });
    window.addEventListener('mousedown', handleMouseDown);
    window.addEventListener('mouseup', handleMouseUp);
    document.addEventListener('mouseenter', handleMouseEnter);
    document.addEventListener('mouseleave', handleMouseLeave);

    // Apply custom-cursor-enabled to body now that custom cursor initialized
    document.body.classList.add('custom-cursor-enabled');

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      window.removeEventListener('mousedown', handleMouseDown);
      window.removeEventListener('mouseup', handleMouseUp);
      document.removeEventListener('mouseenter', handleMouseEnter);
      document.removeEventListener('mouseleave', handleMouseLeave);
      if (tiltTimeoutRef.current) clearTimeout(tiltTimeoutRef.current);
      if (clickTimeoutRef.current) clearTimeout(clickTimeoutRef.current);
      document.body.classList.remove('custom-cursor-enabled');
    };
  }, [isVisible]);

  // If touch device or mouse outside window or over text input, do not render cursor overlay
  if (isTouchDevice || !isVisible || isOverTextInput) {
    return null;
  }

  return (
    <div
      ref={cursorRef}
      className="pointer-events-none fixed top-0 left-0 z-[99999] select-none will-change-transform"
      style={{
        transform: 'translate3d(-100px, -100px, 0)',
      }}
      aria-hidden="true"
    >
      <div
        ref={flowerRef}
        className={`pointer-events-none flex items-center justify-center -translate-x-1/2 -translate-y-1/2 transition-transform duration-75 ${
          isClicking
            ? 'animate-daisy-bounce'
            : isHoveringClickable
            ? 'animate-daisy-wiggle'
            : ''
        }`}
        style={{
          transform: 'translate(-50%, -50%)',
        }}
      >
        {/* Tiny Adorable Pastel Daisy (20px crisp width) */}
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className="drop-shadow-[0_1px_2px_rgba(58,44,41,0.15)] overflow-visible"
        >
          {/* 5 Petals evenly arranged at 0°, 72°, 144°, 216°, 288° */}
          {/* Petal 1 (Top) */}
          <ellipse
            cx="12"
            cy="5.2"
            rx="3.4"
            ry="4.8"
            fill="#FFA8BA"
            stroke="#3A2C29"
            strokeWidth="1.3"
          />
          {/* Petal 2 (Top Right) */}
          <ellipse
            cx="12"
            cy="5.2"
            rx="3.4"
            ry="4.8"
            fill="#BCE0FD"
            stroke="#3A2C29"
            strokeWidth="1.3"
            transform="rotate(72 12 12)"
          />
          {/* Petal 3 (Bottom Right) */}
          <ellipse
            cx="12"
            cy="5.2"
            rx="3.4"
            ry="4.8"
            fill="#FFA8BA"
            stroke="#3A2C29"
            strokeWidth="1.3"
            transform="rotate(144 12 12)"
          />
          {/* Petal 4 (Bottom Left) */}
          <ellipse
            cx="12"
            cy="5.2"
            rx="3.4"
            ry="4.8"
            fill="#BCE0FD"
            stroke="#3A2C29"
            strokeWidth="1.3"
            transform="rotate(216 12 12)"
          />
          {/* Petal 5 (Top Left) */}
          <ellipse
            cx="12"
            cy="5.2"
            rx="3.4"
            ry="4.8"
            fill="#FFB6C6"
            stroke="#3A2C29"
            strokeWidth="1.3"
            transform="rotate(288 12 12)"
          />

          {/* Soft Sunny Yellow Daisy Center */}
          <circle
            cx="12"
            cy="12"
            r="3.8"
            fill="#FDE047"
            stroke="#3A2C29"
            strokeWidth="1.3"
          />

          {/* Adorable micro doodle face inside the center */}
          {/* Tiny eyes */}
          <circle cx="10.8" cy="11.4" r="0.6" fill="#3A2C29" />
          <circle cx="13.2" cy="11.4" r="0.6" fill="#3A2C29" />
          {/* Rosy blush dots */}
          <circle cx="9.9" cy="12.4" r="0.5" fill="#FFAEC0" />
          <circle cx="14.1" cy="12.4" r="0.5" fill="#FFAEC0" />
          {/* Micro smile */}
          <path
            d="M11.3 12.6 Q12 13.3 12.7 12.6"
            stroke="#3A2C29"
            strokeWidth="0.8"
            strokeLinecap="round"
            fill="none"
          />
        </svg>
      </div>
    </div>
  );
};
