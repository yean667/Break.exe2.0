package com.example.breakexe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breakexe.model.BlobExpression
import com.example.breakexe.model.CharacterAccessory
import com.example.breakexe.model.CharacterConfig
import com.example.breakexe.model.CharacterModel
import com.example.breakexe.model.CharacterPalettes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CharacterCustomizerSheet(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    currentConfig: CharacterConfig,
    onSaveConfig: (CharacterConfig) -> Unit,
    soundEnabled: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onTestSound: (String) -> Unit
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var tempConfig by remember(currentConfig) { mutableStateOf(currentConfig) }
    var activeTab by remember { mutableStateOf(0) } // 0: Character, 1: Sounds

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFFFFFDFB),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .testTag("character_customizer_sheet")
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "✨ companion & sound setup",
                        color = Color(0xFF3A2C29),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "customize your desk buddy and soothing audio cues",
                        color = Color(0xFF8F7F7B),
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF7E6E6A))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF5EFE9))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (activeTab == 0) Color.White else Color.Transparent)
                        .clickable { activeTab = 0 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎨 Companion Character",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 0) Color(0xFF3A2C29) else Color(0xFF8C7B76)
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (activeTab == 1) Color.White else Color.Transparent)
                        .clickable { activeTab = 1 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🎵 Sound Effects",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 1) Color(0xFF3A2C29) else Color(0xFF8C7B76)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (activeTab == 0) {
                    // LIVE PREVIEW BOX
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFF0F2))
                            .border(1.dp, Color(0xFFFFE1E6), RoundedCornerShape(20.dp))
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "live preview: (tap to squish! :3)",
                                color = Color(0xFFE8556D),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            DancingBlobComposable(
                                isDancing = true,
                                expression = BlobExpression.DANCING,
                                config = tempConfig,
                                onPoke = { onTestSound("giggle") },
                                modifier = Modifier.size(110.dp)
                            )
                        }
                    }

                    // 1. PRESETS
                    Text(
                        text = "1. CHOOSE A PRE-DESIGNED COMPANION",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CharacterPalettes.PRESETS.forEach { preset ->
                            val isSelected = tempConfig.model == preset.model && tempConfig.name == preset.name
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSelected) Color(0xFFFFF0F3) else Color.White)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFFFA8BA) else Color(0xFFEAE2D8),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        onTestSound("pop")
                                        tempConfig = preset
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = preset.model.emoji, fontSize = 16.sp)
                                    Column {
                                        Text(text = preset.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A2C29))
                                        Text(text = preset.model.label, fontSize = 10.sp, color = Color(0xFF8C7C78))
                                    }
                                }
                            }
                        }
                    }

                    // 2. MODEL SHAPE
                    Text(
                        text = "2. COMPANION SHAPE",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CharacterModel.entries.forEach { model ->
                            val isSelected = tempConfig.model == model
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFE9F3FC) else Color.White)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFB5D7F8) else Color(0xFFEAE2D8),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        onTestSound("pop")
                                        tempConfig = tempConfig.copy(model = model)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${model.emoji} ${model.label}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFF2563EB) else Color(0xFF6B5B57)
                                )
                            }
                        }
                    }

                    // 3. BODY COLOR
                    Text(
                        text = "3. BODY COLOR PALETTE",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CharacterPalettes.BODY_COLORS.forEach { colorOpt ->
                            val isSelected = tempConfig.bodyColor.equals(colorOpt.hex, ignoreCase = true)
                            val swatchColor = try { Color(android.graphics.Color.parseColor(colorOpt.hex)) } catch (e: Exception) { Color.White }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF3A2C29) else Color(0xFFE2D8CC),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        onTestSound("pop")
                                        tempConfig = tempConfig.copy(bodyColor = colorOpt.hex)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(swatchColor)
                                        .border(1.dp, Color(0x33000000), CircleShape)
                                )
                                Text(text = colorOpt.name, fontSize = 11.sp, color = Color(0xFF524340))
                            }
                        }
                    }

                    // 4. ACCENT / HEADPHONE COLOR
                    Text(
                        text = "4. ACCENT / HEADPHONE COLOR",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CharacterPalettes.HEADPHONE_COLORS.forEach { colorOpt ->
                            val isSelected = tempConfig.headphoneColor.equals(colorOpt.hex, ignoreCase = true)
                            val swatchColor = try { Color(android.graphics.Color.parseColor(colorOpt.hex)) } catch (e: Exception) { Color(0xFFFFAEC0) }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .border(
                                        width = if (isSelected) 2.dp else 1.dp,
                                        color = if (isSelected) Color(0xFF3A2C29) else Color(0xFFE2D8CC),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable {
                                        onTestSound("pop")
                                        tempConfig = tempConfig.copy(headphoneColor = colorOpt.hex)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(swatchColor)
                                        .border(1.dp, Color(0x33000000), CircleShape)
                                )
                                Text(text = colorOpt.name, fontSize = 11.sp, color = Color(0xFF524340))
                            }
                        }
                    }

                    // 5. CUTE ACCESSORIES
                    Text(
                        text = "5. CUTE ACCESSORY",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CharacterAccessory.entries.forEach { acc ->
                            val isSelected = tempConfig.accessory == acc
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
                                    .clickable {
                                        onTestSound("pop")
                                        tempConfig = tempConfig.copy(accessory = acc)
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = acc.icon, fontSize = 18.sp)
                                    Text(
                                        text = acc.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color(0xFFE8556D) else Color(0xFF6B5A56)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // SOUNDS TAB
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFEAE2D8), RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (soundEnabled) "🔔 Sound Effects Enabled" else "🔕 Sound Effects Muted",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF3A2C29)
                                )
                                Text(
                                    text = "Play pleasant gentle audio cues during breaks",
                                    fontSize = 11.sp,
                                    color = Color(0xFF8A7A76)
                                )
                            }
                            Switch(
                                checked = soundEnabled,
                                onCheckedChange = onToggleSound,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFFFF6E85)
                                )
                            )
                        }
                    }

                    Text(
                        text = "TEST AUDIO CUES & SAMPLES",
                        color = Color(0xFF3A2C29),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    // Audio cue test buttons
                    SoundTestRow(
                        title = "1. Start Break Chime",
                        desc = "Ascending 4-note kalimba arpeggio",
                        onPlay = { onTestSound("start") }
                    )
                    SoundTestRow(
                        title = "2. 30s Ending Soon Reminder",
                        desc = "Serene dual glass-bell tone",
                        onPlay = { onTestSound("soon") }
                    )
                    SoundTestRow(
                        title = "3. End Break Chime",
                        desc = "Warm welcoming celebratory bell chord",
                        onPlay = { onTestSound("end") }
                    )
                    SoundTestRow(
                        title = "4. Poke Giggle Sound",
                        desc = "Playful squeak when squishing companion",
                        onPlay = { onTestSound("giggle") }
                    )
                    SoundTestRow(
                        title = "5. Spoken Voice Alarm",
                        desc = "“Break khatam ho gaya!” alert phrase",
                        onPlay = { onTestSound("voice") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onTestSound("pop")
                        tempConfig = CharacterPalettes.PRESETS.first()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Reset Default", color = Color(0xFF8C7A76), fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        onTestSound("end")
                        onSaveConfig(tempConfig)
                        onDismiss()
                    },
                    modifier = Modifier
                        .testTag("save_companion_button")
                        .weight(1.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6E85)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Save Companion ✨", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun SoundTestRow(
    title: String,
    desc: String,
    onPlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEDE5DC), RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3A2C29))
                Text(text = desc, fontSize = 10.sp, color = Color(0xFF8A7A76))
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFF0F3))
                    .border(1.dp, Color(0xFFFFA8BA), RoundedCornerShape(8.dp))
                    .clickable { onPlay() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color(0xFFE8556D), modifier = Modifier.size(14.dp))
                    Text(text = "Play", color = Color(0xFFE8556D), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
