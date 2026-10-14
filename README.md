# break.exe (Android)

> A cute, minimal, humorous student break timer with an adorable dancing blob companion.

Rewritten as a native Android application using Kotlin and Jetpack Compose (Material 3).

## Features

- **🌸 Dancing Companion**: Adorable desk buddy with 5 customizable companion styles (Mochi Blob, Slimey Pip, Boba Bunny, Matcha Bear, Cloudy Poko), custom body palettes, accent headphone colors, and cute accessories (Headphones, Pastel Bow, Tiny Sprout, Star Pin).
- **⏱️ Flexible Break Timer**: Preset options (Free Vibe stopwatch, 5m Quick, 10m Coffee, 13m Target 🌸, 15m Stretch, 25m Long) or custom minute duration stepper.
- **🔊 Funny Voice Alarm**: Announces *“Break khatam ho gaya! Break khatam ho gaya!”* continuously when break expires, with soft ascending kalimba chime and gentle haptic vibration. Tap anywhere on screen to silence the alarm.
- **✨ Playful Audio & Haptics**: Non-intrusive sound cues for starting breaks, 30s ending-soon heads up, celebratory chord on completion, and squish giggles on poke.
- **📊 Today's Damage**: Real-time stats dashboard tracking total break time, breaks taken, longest break, and average break.
- **📜 Break History**: Clean log of all study breaks with timestamps, duration badges, and reason categories. Includes a cute sleepy daisy empty state when no breaks have been recorded yet.
- **🎨 Material You & Edge-to-Edge**: Full support for Android edge-to-edge window insets, Material 3 theming, and responsive layouts.

## Architecture

- **Language**: Kotlin 2.1.0
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) with `StateFlow`
- **Audio & TTS**: Android `TextToSpeech` and `AudioTrack` PCM tone synthesis
- **Persistence**: `SharedPreferences` with JSON serialization for break records, active session state, and companion customization
