package com.example.breakexe.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breakexe.data.BreakPreferencesRepository
import com.example.breakexe.model.BreakQuotes
import com.example.breakexe.model.BreakReason
import com.example.breakexe.model.TargetPreset
import com.example.breakexe.ui.components.BlobAvatarComposable
import com.example.breakexe.ui.components.BooksStackComposable
import com.example.breakexe.ui.components.BreakHistoryCard
import com.example.breakexe.ui.components.CharacterCustomizerSheet
import com.example.breakexe.ui.components.DancingBlobComposable
import com.example.breakexe.ui.components.PeekingBlobComposable
import com.example.breakexe.ui.components.RadiatingTicksComposable
import com.example.breakexe.ui.components.ReasonIconComposable
import com.example.breakexe.ui.components.TodayDamageCard
import com.example.breakexe.ui.theme.CreamBackground
import com.example.breakexe.ui.theme.PinkBrand
import com.example.breakexe.ui.theme.PinkBrandDark
import com.example.breakexe.viewmodel.BreakViewModel

val PRESET_OPTIONS = listOf(
    TargetPreset(0, "Free Vibe"),
    TargetPreset(5, "5m Quick"),
    TargetPreset(10, "10m Coffee"),
    TargetPreset(13, "13m Target 🌸"),
    TargetPreset(15, "15m Stretch"),
    TargetPreset(25, "25m Long")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreakScreen(
    viewModel: BreakViewModel,
    modifier: Modifier = Modifier
) {
    val activeBreak by viewModel.activeBreak.collectAsState()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsState()
    val remainingSeconds by viewModel.remainingSeconds.collectAsState()
    val overdueSeconds by viewModel.overdueSeconds.collectAsState()
    val isOverdue by viewModel.isOverdue.collectAsState()
    val targetMinutes by viewModel.targetMinutes.collectAsState()
    val selectedReason by viewModel.selectedReason.collectAsState()
    val explanationNote by viewModel.explanationNote.collectAsState()
    val characterConfig by viewModel.characterConfig.collectAsState()
    val history by viewModel.history.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val isAlarmRinging by viewModel.isAlarmRinging.collectAsState()
    val welcomeBackToast by viewModel.welcomeBackToast.collectAsState()
    val celebrationToast by viewModel.celebrationToast.collectAsState()
    val endingSoonNotice by viewModel.endingSoonNotice.collectAsState()

    var isCustomizerOpen by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    val blobSpeech = remember(activeBreak, elapsedSeconds, isOverdue, selectedReason) {
        if (activeBreak != null) {
            BreakQuotes.getSpeech(elapsedSeconds, activeBreak!!.reason, isOverdue)
        } else {
            BreakQuotes.getSpeech(0L, selectedReason, false)
        }
    }

    val displayDigits = remember(activeBreak, targetMinutes, remainingSeconds, elapsedSeconds) {
        if (activeBreak != null) {
            if (activeBreak!!.targetMinutes > 0) {
                BreakPreferencesRepository.formatTimerDigits(remainingSeconds)
            } else {
                BreakPreferencesRepository.formatTimerDigits(elapsedSeconds)
            }
        } else {
            if (targetMinutes > 0) {
                BreakPreferencesRepository.formatTimerDigits(targetMinutes * 60L)
            } else {
                "00:00"
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        containerColor = CreamBackground,
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                viewModel.onGlobalTap()
            }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main Break Card
                MainBreakCard(
                    activeBreak = activeBreak,
                    isOverdue = isOverdue,
                    displayDigits = displayDigits,
                    elapsedSeconds = elapsedSeconds,
                    remainingSeconds = remainingSeconds,
                    overdueSeconds = overdueSeconds,
                    targetMinutes = targetMinutes,
                    selectedReason = selectedReason,
                    explanationNote = explanationNote,
                    characterConfig = characterConfig,
                    soundEnabled = soundEnabled,
                    isAlarmRinging = isAlarmRinging,
                    blobSpeech = blobSpeech,
                    onStartBreak = { viewModel.startBreak() },
                    onEndBreak = { viewModel.endBreak() },
                    onSetTargetMinutes = { viewModel.setTargetMinutes(it) },
                    onSelectReason = { viewModel.setSelectedReason(it) },
                    onNoteChange = { viewModel.setExplanationNote(it) },
                    onOpenCustomizer = { isCustomizerOpen = true },
                    onToggleSound = { viewModel.toggleSound(!soundEnabled) },
                    onTestVoice = { viewModel.testSound("voice") },
                    onPoke = { viewModel.testSound("giggle") }
                )

                // Today's Damage Card
                TodayDamageCard(stats = stats)

                // Break History Card
                BreakHistoryCard(
                    records = history,
                    onDeleteRecord = { viewModel.deleteRecord(it) },
                    onClearAll = { viewModel.clearAllHistory() }
                )

                // Footer
                BreakFooter(
                    onInstagramClick = {
                        try {
                            uriHandler.openUri("https://www.instagram.com/mrprofessorirl/")
                        } catch (e: Exception) {
                            // ignore
                        }
                    },
                    onDiscordClick = {
                        try {
                            uriHandler.openUri("https://discord.gg/zBjefVmTN")
                        } catch (e: Exception) {
                            // ignore
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Top Persistent Voice Alarm Alert Banner
            AnimatedVisibility(
                visible = isAlarmRinging,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .testTag("alarm_alert_banner")
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFFFF0F4))
                        .border(2.dp, Color(0xFFFFA8BA), RoundedCornerShape(24.dp))
                        .clickable { viewModel.dismissAlarm() }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFE4E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🔔", fontSize = 20.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "“Break khatam ho gaya!”",
                                    color = Color(0xFFE8556D),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFFD6DF))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Time's Up!",
                                        color = Color(0xFFD83B55),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = "Tap anywhere on screen to silence the alarm ✨",
                                color = Color(0xFF7D6B66),
                                fontSize = 11.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PinkBrand)
                                .clickable { viewModel.dismissAlarm() }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Silence 🌸",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Welcome Back Toast
            AnimatedVisibility(
                visible = welcomeBackToast != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFFFFF5F7))
                        .border(2.dp, Color(0xFFFFA8BA), RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BlobAvatarComposable(size = 24.dp)
                        Text(
                            text = welcomeBackToast ?: "",
                            color = Color(0xFFE8556D),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Celebration Toast
            AnimatedVisibility(
                visible = celebrationToast != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .border(2.dp, Color(0xFFFFA8BA), RoundedCornerShape(24.dp))
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BlobAvatarComposable(size = 24.dp)
                        Text(
                            text = celebrationToast ?: "",
                            color = Color(0xFFE8556D),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Ending Soon 30s Notice
            AnimatedVisibility(
                visible = endingSoonNotice != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 70.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFFF7ED))
                        .border(1.dp, Color(0xFFFDBA74), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "🔔 " + (endingSoonNotice ?: ""),
                        color = Color(0xFFC2410C),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Companion Customizer Sheet
    CharacterCustomizerSheet(
        isOpen = isCustomizerOpen,
        onDismiss = { isCustomizerOpen = false },
        currentConfig = characterConfig,
        onSaveConfig = { viewModel.saveCharacterConfig(it) },
        soundEnabled = soundEnabled,
        onToggleSound = { viewModel.toggleSound(it) },
        onTestSound = { viewModel.testSound(it) }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MainBreakCard(
    activeBreak: com.example.breakexe.model.ActiveBreak?,
    isOverdue: Boolean,
    displayDigits: String,
    elapsedSeconds: Long,
    remainingSeconds: Long,
    overdueSeconds: Long,
    targetMinutes: Int,
    selectedReason: BreakReason,
    explanationNote: String,
    characterConfig: com.example.breakexe.model.CharacterConfig,
    soundEnabled: Boolean,
    isAlarmRinging: Boolean,
    blobSpeech: com.example.breakexe.model.BlobSpeech,
    onStartBreak: () -> Unit,
    onEndBreak: () -> Unit,
    onSetTargetMinutes: (Int) -> Unit,
    onSelectReason: (BreakReason) -> Unit,
    onNoteChange: (String) -> Unit,
    onOpenCustomizer: () -> Unit,
    onToggleSound: () -> Unit,
    onTestVoice: () -> Unit,
    onPoke: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFFFFDFB))
            .border(1.dp, Color(0xFFECE4DB), RoundedCornerShape(32.dp))
            .padding(18.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BlobAvatarComposable(size = 36.dp)
                    Column {
                        Text(
                            text = "break.exe",
                            color = Color(0xFF3A2C29),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        )
                        Text(
                            text = "because even robots need breaks",
                            color = Color(0xFF9E8E8A),
                            fontSize = 11.sp
                        )
                    }
                }

                // Header Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Test Voice button
                    Box(
                        modifier = Modifier
                            .testTag("test_voice_button")
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isAlarmRinging) Color(0xFFFFE6EC) else Color(0xFFFAF4ED))
                            .border(1.dp, if (isAlarmRinging) Color(0xFFFFA8BA) else Color(0xFFEFE5DB), RoundedCornerShape(14.dp))
                            .clickable { onTestVoice() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (isAlarmRinging) "🛑 Stop" else "🔊 Voice",
                            color = if (isAlarmRinging) Color(0xFFE8556D) else Color(0xFF6B5A56),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Customize Companion button
                    Box(
                        modifier = Modifier
                            .testTag("customize_companion_button")
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFAF4ED))
                            .border(1.dp, Color(0xFFEFE5DB), RoundedCornerShape(14.dp))
                            .clickable { onOpenCustomizer() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "✨ ${characterConfig.name}",
                            color = Color(0xFF6B5A56),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Mute / Unmute button
                    Box(
                        modifier = Modifier
                            .testTag("toggle_sound_button")
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (soundEnabled) Color(0xFFFAF4ED) else Color(0xFFF2ECE6))
                            .border(1.dp, Color(0xFFEFE5DB), CircleShape)
                            .clickable { onToggleSound() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = if (soundEnabled) "🔔" else "🔕", fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Inner Centerpiece Card (Timer + Blob + Quotes)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(if (isOverdue) Color(0xFFFFF0F3) else Color(0xFFFFF0F2))
                    .border(1.dp, if (isOverdue) Color(0xFFFFB6C4) else Color(0xFFFFE1E6), RoundedCornerShape(26.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Overdue alert inside card
                    if (isOverdue) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFE6EC))
                                .border(1.dp, Color(0xFFFFA8BA), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⏰ Break expired! +${BreakPreferencesRepository.formatTimerDigits(overdueSeconds)} overdue",
                                    color = Color(0xFFE8556D),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isAlarmRinging) {
                                    Text(
                                        text = "tap to silence 🌸",
                                        color = Color(0xFFA84557),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Digital Timer Display
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RadiatingTicksComposable(isLeft = true)
                        Text(
                            text = displayDigits,
                            color = if (isOverdue) Color(0xFFE8556D) else Color(0xFFEB5E6F),
                            fontSize = 46.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp
                        )
                        RadiatingTicksComposable(isLeft = false)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Left Quote + Center Dancing Blob + Right Books
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left Quote
                        Column(
                            modifier = Modifier.width(78.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = blobSpeech.main,
                                color = Color(0xFF7D6B66),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 14.sp
                            )
                        }

                        // Dancing Blob
                        DancingBlobComposable(
                            isDancing = activeBreak != null,
                            expression = blobSpeech.expression,
                            config = characterConfig,
                            onPoke = onPoke,
                            modifier = Modifier.size(130.dp)
                        )

                        // Right Subtitle + Books
                        Column(
                            modifier = Modifier.width(78.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = blobSpeech.subtitle,
                                color = Color(0xFF7D6B66),
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            BooksStackComposable(modifier = Modifier.size(46.dp, 32.dp))
                        }
                    }

                    // Status pill if active break
                    if (activeBreak != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        if (activeBreak.targetMinutes > 0) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isOverdue) Color(0xFFFFE6EC) else Color.White.copy(alpha = 0.85f))
                                        .border(1.dp, Color(0xFFFFD5DC), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = if (isOverdue) {
                                            "target (${activeBreak.targetMinutes}m) reached • overdue: +${BreakPreferencesRepository.formatTimerDigits(overdueSeconds)}"
                                        } else {
                                            "countdown: ${BreakPreferencesRepository.formatTimerDigits(remainingSeconds)} left"
                                        },
                                        color = Color(0xFFC85567),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.65f))
                                        .border(1.dp, Color(0xFFEAE2D8), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "total: ${BreakPreferencesRepository.formatTimerDigits(elapsedSeconds)}",
                                        color = Color(0xFF8F7F7B),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.85f))
                                    .border(1.dp, Color(0xFFFFD5DC), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "free vibe stopwatch • ${BreakPreferencesRepository.formatTimerDigits(elapsedSeconds)} elapsed",
                                    color = Color(0xFFC85567),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Break Length Selector (When inactive)
            if (activeBreak == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFFAF7F3))
                        .border(1.dp, Color(0xFFEFE7DE), RoundedCornerShape(18.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⏱️ SET BREAK TIMER LENGTH:",
                                color = Color(0xFF6D5D59),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = if (targetMinutes == 0) "free-form stopwatch" else "timer: ${targetMinutes}m countdown 🔔",
                                color = PinkBrandDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick Presets Row
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PRESET_OPTIONS.forEach { preset ->
                                val isSelected = targetMinutes == preset.minutes
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(0xFFFFE6EC) else Color.White)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFFFA8BA) else Color(0xFFEAE2D8),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { onSetTargetMinutes(preset.minutes) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = preset.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFE8556D) else Color(0xFF6E5E5A)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Stepper for custom minutes
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Or custom duration:",
                                color = Color(0xFF7A6B67),
                                fontSize = 11.sp
                            )

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFEAE2D8), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFAF4ED))
                                        .clickable { onSetTargetMinutes(maxOf(1, targetMinutes - 1)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("-", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF554643))
                                }

                                Text(
                                    text = "$targetMinutes min",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PinkBrandDark
                                )

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFFAF4ED))
                                        .clickable { onSetTargetMinutes(minOf(180, targetMinutes + 1)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF554643))
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Why are you taking a break?
            Text(
                text = "why are you taking a break?",
                color = Color(0xFF3A2C29),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Reason Pills Grid (2x4)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BreakReason.entries.forEach { reason ->
                    val isSelected = selectedReason == reason
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) Color(0xFFFFF0F3) else Color.White)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Color(0xFFFFA8BA) else Color(0xFFEAE2D8),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable(enabled = activeBreak == null) { onSelectReason(reason) }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ReasonIconComposable(reason = reason, size = 16.dp)
                            Text(
                                text = reason.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color(0xFFE8556D) else Color(0xFF554643)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation Input Row with Peeking Blob
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = explanationNote,
                    onValueChange = onNoteChange,
                    enabled = activeBreak == null,
                    placeholder = { Text("any explanation? (optional)", fontSize = 12.sp, color = Color(0xFFA69793)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFA8BA),
                        unfocusedBorderColor = Color(0xFFEAE2D8),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true
                )

                PeekingBlobComposable(text = "no judgment... ~ maybe")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Button: Start or End Break
            if (activeBreak == null) {
                Button(
                    onClick = onStartBreak,
                    modifier = Modifier
                        .testTag("start_break_button")
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PinkBrand),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (targetMinutes > 0) "Start ${targetMinutes}m Break" else "Start Free Break",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = onEndBreak,
                    modifier = Modifier
                        .testTag("end_break_button")
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isOverdue) Color(0xFFE8556D) else PinkBrand),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = if (isOverdue) "End Break (You survived! 🎓)" else "End Break",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Sarcastic Caption
            Text(
                text = if (isOverdue) {
                    "Break khatam ho gaya! Time to get back to studying 🌸"
                } else {
                    "come back soon... your future self is watching 👀"
                },
                color = Color(0xFF8F7F7B),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BreakFooter(
    onInstagramClick: () -> Unit,
    onDiscordClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "built with", fontSize = 13.sp, color = Color(0xFF6E5D59))
            Text(text = "♡", fontSize = 14.sp, color = Color(0xFFFF6E85), fontWeight = FontWeight.Bold)
            Text(text = "by", fontSize = 13.sp, color = Color(0xFF6E5D59))
            Text(text = "Aru ✨", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A2C29))
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFECE2D8), RoundedCornerShape(14.dp))
                    .clickable { onInstagramClick() }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "📷 Instagram", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF665450))
            }

            Text(text = "•", fontSize = 10.sp, color = Color(0xFFD5C9BE))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFECE2D8), RoundedCornerShape(14.dp))
                    .clickable { onDiscordClick() }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "💬 Discord Server", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF665450))
            }
        }
    }
}
