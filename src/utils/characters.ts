import { CharacterConfig } from '../types';

export const CHARACTER_PRESETS: CharacterConfig[] = [
  {
    model: 'blob',
    name: 'Mochi Blob',
    bodyColor: '#FFFFFF',
    headphoneColor: '#FFAEC0',
    accessory: 'headphones',
    blushColor: '#FFAEC0',
  },
  {
    model: 'slime',
    name: 'Slimey Pip',
    bodyColor: '#D8F2FE',
    headphoneColor: '#88CEF9',
    accessory: 'headphones',
    blushColor: '#FFAEC0',
  },
  {
    model: 'bunny',
    name: 'Boba Bunny',
    bodyColor: '#FFE5EC',
    headphoneColor: '#FFAEC0',
    accessory: 'bow',
    blushColor: '#FDA4AF',
  },
  {
    model: 'bear',
    name: 'Matcha Bear',
    bodyColor: '#E6F8EE',
    headphoneColor: '#86EFAC',
    accessory: 'headphones',
    blushColor: '#FDBA74',
  },
  {
    model: 'cloud',
    name: 'Cloudy Poko',
    bodyColor: '#F3E8FF',
    headphoneColor: '#D0A9F5',
    accessory: 'star',
    blushColor: '#F472B6',
  },
];

export const BODY_COLOR_PALETTES = [
  { name: 'Fluffy White', hex: '#FFFFFF' },
  { name: 'Sky Slime', hex: '#D8F2FE' },
  { name: 'Sakura Pink', hex: '#FFE5EC' },
  { name: 'Matcha Mint', hex: '#E6F8EE' },
  { name: 'Cloud Lilac', hex: '#F3E8FF' },
  { name: 'Butter Cream', hex: '#FEF8D8' },
  { name: 'Peach Sorbet', hex: '#FFE5D4' },
];

export const HEADPHONE_PALETTES = [
  { name: 'Strawberry', hex: '#FFAEC0' },
  { name: 'Baby Blue', hex: '#88CEF9' },
  { name: 'Lavender', hex: '#D0A9F5' },
  { name: 'Sage Mint', hex: '#86EFAC' },
  { name: 'Warm Peach', hex: '#FDBA74' },
  { name: 'Slate Gray', hex: '#94A3B8' },
];

export const ACCESSORY_OPTIONS = [
  { id: 'headphones' as const, label: 'Headphones', icon: '🎧' },
  { id: 'bow' as const, label: 'Pastel Bow', icon: '🎀' },
  { id: 'sprout' as const, label: 'Tiny Sprout', icon: '🌱' },
  { id: 'star' as const, label: 'Star Pin', icon: '⭐' },
];

const STORAGE_KEY_CHARACTER = 'break_exe_character_config_v1';

export function loadSavedCharacter(): CharacterConfig {
  try {
    const raw = localStorage.getItem(STORAGE_KEY_CHARACTER);
    if (!raw) return CHARACTER_PRESETS[0];
    return JSON.parse(raw);
  } catch {
    return CHARACTER_PRESETS[0];
  }
}

export function saveCharacter(config: CharacterConfig): void {
  try {
    localStorage.setItem(STORAGE_KEY_CHARACTER, JSON.stringify(config));
  } catch (err) {
    console.error('Failed to save character config', err);
  }
}
