import React, { useState } from 'react';
import { CharacterConfig, CharacterModel, CharacterAccessory } from '../types';
import {
  CHARACTER_PRESETS,
  BODY_COLOR_PALETTES,
  HEADPHONE_PALETTES,
  ACCESSORY_OPTIONS,
} from '../utils/characters';
import { DancingBlob } from './DancingBlob';
import { sound } from '../utils/audio';

interface CharacterCustomizerModalProps {
  isOpen: boolean;
  onClose: () => void;
  currentConfig: CharacterConfig;
  onSave: (config: CharacterConfig) => void;
  soundEnabled: boolean;
  onToggleSound: (enabled: boolean) => void;
}

export const CharacterCustomizerModal: React.FC<CharacterCustomizerModalProps> = ({
  isOpen,
  onClose,
  currentConfig,
  onSave,
  soundEnabled,
  onToggleSound,
}) => {
  const [tempConfig, setTempConfig] = useState<CharacterConfig>(currentConfig);
  const [activeTab, setActiveTab] = useState<'character' | 'sounds'>('character');

  if (!isOpen) return null;

  const handleSelectPreset = (preset: CharacterConfig) => {
    sound.playPop();
    setTempConfig({ ...preset });
  };

  const handleSelectModel = (model: CharacterModel) => {
    sound.playPop();
    setTempConfig((prev) => ({ ...prev, model }));
  };

  const handleSelectBodyColor = (hex: string) => {
    sound.playPop();
    setTempConfig((prev) => ({ ...prev, bodyColor: hex }));
  };

  const handleSelectHeadphoneColor = (hex: string) => {
    sound.playPop();
    setTempConfig((prev) => ({ ...prev, headphoneColor: hex }));
  };

  const handleSelectAccessory = (acc: CharacterAccessory) => {
    sound.playPop();
    setTempConfig((prev) => ({ ...prev, accessory: acc }));
  };

  const handleSave = () => {
    sound.playEndChime();
    onSave(tempConfig);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-3 bg-black/30 backdrop-blur-xs animate-fadeIn">
      <div className="bg-[#FFFDFB] border border-[#ECE4DB] rounded-[32px] w-full max-w-lg p-5 md:p-6 shadow-2xl relative max-h-[92vh] flex flex-col overflow-hidden">
        {/* Header */}
        <div className="flex items-center justify-between pb-3 border-b border-[#F0E8DF]">
          <div>
            <h2 className="text-xl md:text-2xl font-bold text-[#3A2C29] flex items-center gap-2">
              <span>✨</span> companion & sound setup
            </h2>
            <p className="text-xs text-[#8F7F7B] font-medium">
              customize your little desk friend and soothing audio cues
            </p>
          </div>
          <button
            onClick={() => {
              sound.playPop();
              onClose();
            }}
            className="w-8 h-8 rounded-full bg-[#FAF4ED] text-[#7E6E6A] hover:bg-[#F2E8DE] flex items-center justify-center text-sm font-bold cursor-pointer transition-colors"
          >
            ✕
          </button>
        </div>

        {/* Tab switch */}
        <div className="flex gap-2 my-3 p-1 bg-[#F5EFE9] rounded-2xl">
          <button
            onClick={() => {
              sound.playPop();
              setActiveTab('character');
            }}
            className={`flex-1 py-1.5 rounded-xl text-xs md:text-sm font-bold transition-all cursor-pointer ${
              activeTab === 'character'
                ? 'bg-white text-[#3A2C29] shadow-xs'
                : 'text-[#8C7B76] hover:text-[#3A2C29]'
            }`}
          >
            🎨 Companion Character
          </button>
          <button
            onClick={() => {
              sound.playPop();
              setActiveTab('sounds');
            }}
            className={`flex-1 py-1.5 rounded-xl text-xs md:text-sm font-bold transition-all cursor-pointer ${
              activeTab === 'sounds'
                ? 'bg-white text-[#3A2C29] shadow-xs'
                : 'text-[#8C7B76] hover:text-[#3A2C29]'
            }`}
          >
            🎵 Sound Effects
          </button>
        </div>

        {/* Content Body */}
        <div className="flex-1 overflow-y-auto pr-1 space-y-4">
          {activeTab === 'character' ? (
            <>
              {/* Live Preview Box */}
              <div className="bg-[#FFF0F2] border border-[#FFE1E6] rounded-2xl p-3 flex flex-col items-center justify-center relative select-none">
                <span className="absolute top-2 left-3 font-doodle text-xs text-[#E8556D] font-bold">
                  live preview:
                </span>
                <div className="scale-90">
                  <DancingBlob
                    isDancing={true}
                    expression="dancing"
                    config={tempConfig}
                  />
                </div>
                <div className="font-doodle text-xs text-[#7A6A66] -mt-1">
                  (click to squish and hear preview! :3)
                </div>
              </div>

              {/* 1. Pre-designed Characters */}
              <div>
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-2">
                  1. Choose a Pre-designed Companion (5 styles)
                </label>
                <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
                  {CHARACTER_PRESETS.map((preset) => {
                    const isSelected =
                      tempConfig.model === preset.model &&
                      tempConfig.bodyColor === preset.bodyColor;
                    return (
                      <button
                        key={preset.name}
                        onClick={() => handleSelectPreset(preset)}
                        className={`p-2 rounded-2xl border text-left flex items-center gap-2 transition-all cursor-pointer ${
                          isSelected
                            ? 'bg-[#FFF0F3] border-[#FFA8BA] ring-2 ring-[#FFA8BA]/30'
                            : 'bg-white border-[#EAE2D8] hover:bg-[#FDF9F6]'
                        }`}
                      >
                        <div
                          className="w-7 h-7 rounded-full border border-[#DCD3C9] flex items-center justify-center text-xs shrink-0"
                          style={{ backgroundColor: preset.bodyColor }}
                        >
                          {preset.model === 'bunny'
                            ? '🐰'
                            : preset.model === 'slime'
                            ? '💧'
                            : preset.model === 'bear'
                            ? '🐻'
                            : preset.model === 'cloud'
                            ? '☁️'
                            : '🍡'}
                        </div>
                        <div className="min-w-0">
                          <div className="text-xs font-bold text-[#3A2C29] truncate">
                            {preset.name}
                          </div>
                          <div className="text-[10px] text-[#8C7C78] capitalize truncate">
                            {preset.model}
                          </div>
                        </div>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* 2. Character Model Shape */}
              <div>
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-1.5">
                  2. Companion Shape
                </label>
                <div className="flex flex-wrap gap-1.5">
                  {(['blob', 'slime', 'bunny', 'bear', 'cloud'] as CharacterModel[]).map(
                    (m) => (
                      <button
                        key={m}
                        onClick={() => handleSelectModel(m)}
                        className={`px-3 py-1 rounded-xl text-xs font-bold capitalize transition-all cursor-pointer border ${
                          tempConfig.model === m
                            ? 'bg-[#E9F3FC] border-[#B5D7F8] text-[#2563EB]'
                            : 'bg-white border-[#EAE2D8] text-[#6B5B57] hover:bg-[#FAF4EE]'
                        }`}
                      >
                        {m === 'blob'
                          ? '🍡 Dumpling Blob'
                          : m === 'slime'
                          ? '💧 Slime Droplet'
                          : m === 'bunny'
                          ? '🐰 Boba Bunny'
                          : m === 'bear'
                          ? '🐻 Matcha Bear'
                          : '☁️ Fluffy Cloud'}
                      </button>
                    )
                  )}
                </div>
              </div>

              {/* 3. Body Color Palette */}
              <div>
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-1.5">
                  3. Body Color Palette
                </label>
                <div className="flex flex-wrap gap-2">
                  {BODY_COLOR_PALETTES.map((color) => {
                    const isSelected = tempConfig.bodyColor.toLowerCase() === color.hex.toLowerCase();
                    return (
                      <button
                        key={color.name}
                        onClick={() => handleSelectBodyColor(color.hex)}
                        title={color.name}
                        className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full border text-xs font-medium cursor-pointer transition-all ${
                          isSelected
                            ? 'border-[#3A2C29] ring-2 ring-[#FFA8BA] bg-white'
                            : 'border-[#E2D8CC] bg-white hover:border-[#B5A59E]'
                        }`}
                      >
                        <span
                          className="w-3.5 h-3.5 rounded-full border border-black/15 shadow-2xs"
                          style={{ backgroundColor: color.hex }}
                        />
                        <span className="text-[#524340] text-[11px]">{color.name}</span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* 4. Headphone / Accent Color */}
              <div>
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-1.5">
                  4. Accent / Headphone Color
                </label>
                <div className="flex flex-wrap gap-2">
                  {HEADPHONE_PALETTES.map((color) => {
                    const isSelected =
                      tempConfig.headphoneColor.toLowerCase() === color.hex.toLowerCase();
                    return (
                      <button
                        key={color.name}
                        onClick={() => handleSelectHeadphoneColor(color.hex)}
                        title={color.name}
                        className={`flex items-center gap-1.5 px-2.5 py-1 rounded-full border text-xs font-medium cursor-pointer transition-all ${
                          isSelected
                            ? 'border-[#3A2C29] ring-2 ring-[#FFA8BA] bg-white'
                            : 'border-[#E2D8CC] bg-white hover:border-[#B5A59E]'
                        }`}
                      >
                        <span
                          className="w-3.5 h-3.5 rounded-full border border-black/15 shadow-2xs"
                          style={{ backgroundColor: color.hex }}
                        />
                        <span className="text-[#524340] text-[11px]">{color.name}</span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* 5. Accessories */}
              <div>
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-1.5">
                  5. Cute Accessory
                </label>
                <div className="grid grid-cols-2 sm:grid-cols-4 gap-2">
                  {ACCESSORY_OPTIONS.map((acc) => {
                    const isSelected = tempConfig.accessory === acc.id;
                    return (
                      <button
                        key={acc.id}
                        onClick={() => handleSelectAccessory(acc.id)}
                        className={`py-1.5 px-2.5 rounded-xl border text-xs font-bold flex items-center justify-center gap-1.5 cursor-pointer transition-all ${
                          isSelected
                            ? 'bg-[#FFF0F3] border-[#FFA8BA] text-[#E8556D]'
                            : 'bg-white border-[#EAE2D8] text-[#6B5A56] hover:bg-[#FAF4EE]'
                        }`}
                      >
                        <span>{acc.icon}</span>
                        <span>{acc.label}</span>
                      </button>
                    );
                  })}
                </div>
              </div>
            </>
          ) : (
            /* =================================================== */
            /* SOUND EFFECTS SETTINGS TAB                          */
            /* =================================================== */
            <div className="space-y-4">
              {/* Master Mute / Unmute */}
              <div className="bg-white border border-[#EAE2D8] rounded-2xl p-4 flex items-center justify-between">
                <div>
                  <div className="font-bold text-sm text-[#3A2C29] flex items-center gap-1.5">
                    <span>{soundEnabled ? '🔔' : '🔕'}</span> Sound Effects
                  </div>
                  <div className="text-xs text-[#8A7A76] mt-0.5">
                    Play gentle, non-intrusive chimes during breaks
                  </div>
                </div>
                <button
                  onClick={() => {
                    const newVal = !soundEnabled;
                    onToggleSound(newVal);
                    sound.setEnabled(newVal);
                    if (newVal) sound.playStartChime();
                  }}
                  className={`px-4 py-2 rounded-xl text-xs font-bold cursor-pointer transition-all ${
                    soundEnabled
                      ? 'bg-[#E9F3FC] text-[#2563EB] border border-[#BCE0FD]'
                      : 'bg-[#F5EFEA] text-[#8C7A76] border border-[#E3D9CF]'
                  }`}
                >
                  {soundEnabled ? 'Enabled' : 'Muted'}
                </button>
              </div>

              {/* Test Audio Preview Buttons */}
              <div className="bg-[#FAF7F2] border border-[#EBE3D8] rounded-2xl p-4">
                <label className="block text-xs font-bold text-[#3A2C29] uppercase tracking-wider mb-2">
                  Test Minimalist Sound Effects
                </label>
                <div className="space-y-2.5">
                  {/* Start break sound */}
                  <div className="flex items-center justify-between bg-white p-2.5 rounded-xl border border-[#EDE5DC]">
                    <div>
                      <div className="text-xs font-bold text-[#3A2C29]">
                        1. Start Break Sound
                      </div>
                      <div className="text-[11px] text-[#8A7A76]">
                        Gentle ascending kalimba chime
                      </div>
                    </div>
                    <button
                      onClick={() => sound.playStartChime()}
                      className="px-3 py-1 bg-[#FFF0F3] hover:bg-[#FFE1E7] text-[#E8556D] border border-[#FFA8BA] rounded-lg text-xs font-bold cursor-pointer transition-colors"
                    >
                      ▶ Play
                    </button>
                  </div>

                  {/* Ending soon subtle sound */}
                  <div className="flex items-center justify-between bg-white p-2.5 rounded-xl border border-[#EDE5DC]">
                    <div>
                      <div className="text-xs font-bold text-[#3A2C29]">
                        2. Break Ending Soon (Notification)
                      </div>
                      <div className="text-[11px] text-[#8A7A76]">
                        Subtle, soft dual glass-bell reminder
                      </div>
                    </div>
                    <button
                      onClick={() => sound.playEndingSoonReminder()}
                      className="px-3 py-1 bg-[#E9F3FC] hover:bg-[#D8ECFB] text-[#2563EB] border border-[#BCE0FD] rounded-lg text-xs font-bold cursor-pointer transition-colors"
                    >
                      ▶ Play
                    </button>
                  </div>

                  {/* End break sound */}
                  <div className="flex items-center justify-between bg-white p-2.5 rounded-xl border border-[#EDE5DC]">
                    <div>
                      <div className="text-xs font-bold text-[#3A2C29]">
                        3. End Break Sound
                      </div>
                      <div className="text-[11px] text-[#8A7A76]">
                        Distinct, welcoming warm bell chord
                      </div>
                    </div>
                    <button
                      onClick={() => sound.playEndChime()}
                      className="px-3 py-1 bg-[#EDFBEF] hover:bg-[#DDF8E1] text-[#16A34A] border border-[#BBF7D0] rounded-lg text-xs font-bold cursor-pointer transition-colors"
                    >
                      ▶ Play
                    </button>
                  </div>

                  {/* Giggle poke sound */}
                  <div className="flex items-center justify-between bg-white p-2.5 rounded-xl border border-[#EDE5DC]">
                    <div>
                      <div className="text-xs font-bold text-[#3A2C29]">
                        4. Poke & Giggle Sound
                      </div>
                      <div className="text-[11px] text-[#8A7A76]">
                        Cute squeak when clicking the character
                      </div>
                    </div>
                    <button
                      onClick={() => sound.playGiggle()}
                      className="px-3 py-1 bg-[#FFF7ED] hover:bg-[#FFEDD5] text-[#D97706] border border-[#FDE68A] rounded-lg text-xs font-bold cursor-pointer transition-colors"
                    >
                      ▶ Play
                    </button>
                  </div>
                </div>
              </div>

              {/* Volume Slider */}
              <div className="bg-white border border-[#EAE2D8] rounded-2xl p-4">
                <div className="flex justify-between items-center mb-1.5">
                  <span className="text-xs font-bold text-[#3A2C29]">Volume Level</span>
                  <span className="text-xs font-medium text-[#8A7A76]">
                    {Math.round(sound.volume * 100)}%
                  </span>
                </div>
                <input
                  type="range"
                  min="0.1"
                  max="1.0"
                  step="0.05"
                  defaultValue={sound.volume}
                  onChange={(e) => {
                    sound.volume = parseFloat(e.target.value);
                  }}
                  className="w-full accent-[#FF6E85] cursor-pointer"
                />
                <div className="flex justify-between text-[10px] text-[#A89894] mt-1 font-doodle">
                  <span>whisper quiet</span>
                  <span>balanced</span>
                  <span>cheerful</span>
                </div>
              </div>
            </div>
          )}
        </div>

        {/* Footer actions */}
        <div className="pt-3 border-t border-[#F0E8DF] flex gap-2 justify-end mt-2">
          <button
            onClick={() => {
              sound.playPop();
              setTempConfig(CHARACTER_PRESETS[0]);
            }}
            className="px-3.5 py-2 rounded-xl text-xs font-semibold text-[#8C7A76] hover:bg-[#FAF4EE] transition-colors cursor-pointer"
          >
            Reset Default
          </button>
          <button
            onClick={handleSave}
            className="px-6 py-2 rounded-xl text-xs md:text-sm font-bold bg-[#FF6E85] hover:bg-[#FA5B74] text-white shadow-xs transition-all cursor-pointer font-display"
          >
            Save Companion ✨
          </button>
        </div>
      </div>
    </div>
  );
};
