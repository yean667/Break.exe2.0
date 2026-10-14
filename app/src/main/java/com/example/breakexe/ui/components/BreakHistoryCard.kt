package com.example.breakexe.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breakexe.data.BreakPreferencesRepository
import com.example.breakexe.model.BreakRecord

@Composable
fun BreakHistoryCard(
    records: List<BreakRecord>,
    onDeleteRecord: (String) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .testTag("break_history_card")
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFFFFDFB))
            .border(1.dp, Color(0xFFECE4DB), RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "break history",
                    color = Color(0xFF3A2C29),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                if (records.isNotEmpty()) {
                    Box {
                        IconButton(
                            onClick = { menuOpen = true },
                            modifier = Modifier
                                .testTag("history_menu_button")
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "History options",
                                tint = Color(0xFF9E8E8A)
                            )
                        }

                        DropdownMenu(
                            expanded = menuOpen,
                            onDismissRequest = { menuOpen = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Clear all history",
                                        color = Color(0xFFE8556D),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                },
                                onClick = {
                                    menuOpen = false
                                    onClearAll()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFE8556D)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (records.isEmpty()) {
                // Empty state with cute sleepy daisy illustration
                EmptyHistoryView()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(records, key = { it.id }) { record ->
                        BreakRecordItem(
                            record = record,
                            onDelete = { onDeleteRecord(record.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BreakRecordItem(
    record: BreakRecord,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFAF7F3))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Start Time
        Text(
            text = BreakPreferencesRepository.formatTimeString(record.startTime),
            color = Color(0xFF8C7A76),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(62.dp)
        )

        // Pink Duration Badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFEBF0))
                .border(1.dp, Color(0xFFFFCCD7), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = BreakPreferencesRepository.formatDurationBadge(record.durationSeconds),
                color = Color(0xFFE8556D),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Reason Icon in small badge
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFEAE2D8), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            ReasonIconComposable(reason = record.reason, size = 18.dp)
        }

        // Reason Name & Note
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.reason.label,
                color = Color(0xFF3A2C29),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = record.note.ifBlank { "vibing..." },
                color = Color(0xFF7E6E6A),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Delete button
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete entry",
                tint = Color(0xFFB5A5A1),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun EmptyHistoryView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sleepy daisy illustration Canvas
        Canvas(modifier = Modifier.size(80.dp)) {
            val w = size.width
            val h = size.height
            val strokeColor = Color(0xFF3A2C29)

            // Ground shadow
            drawOval(Color(0xFFEAE0D6), Offset(w * 0.25f, h * 0.85f), Size(w * 0.50f, h * 0.10f))

            // Stem
            val stem = Path().apply {
                moveTo(w * 0.50f, h * 0.85f)
                cubicTo(w * 0.48f, h * 0.70f, w * 0.53f, h * 0.58f, w * 0.50f, h * 0.48f)
            }
            drawPath(stem, strokeColor, style = Stroke(3f, cap = StrokeCap.Round))

            // Leaves
            drawOval(Color(0xFFBBF7D0), Offset(w * 0.35f, h * 0.60f), Size(w * 0.14f, h * 0.08f))
            drawOval(Color(0xFFBBF7D0), Offset(w * 0.52f, h * 0.56f), Size(w * 0.14f, h * 0.08f))

            // Petals
            val petalColor1 = Color(0xFFFFCCD7)
            val petalColor2 = Color(0xFFBCE0FD)
            drawOval(petalColor1, Offset(w * 0.42f, h * 0.22f), Size(w * 0.16f, h * 0.22f))
            drawOval(petalColor2, Offset(w * 0.58f, h * 0.28f), Size(w * 0.20f, h * 0.16f))
            drawOval(petalColor1, Offset(w * 0.54f, h * 0.42f), Size(w * 0.20f, h * 0.16f))
            drawOval(petalColor2, Offset(w * 0.28f, h * 0.42f), Size(w * 0.20f, h * 0.16f))
            drawOval(petalColor1, Offset(w * 0.22f, h * 0.28f), Size(w * 0.20f, h * 0.16f))

            // Center yellow core
            drawCircle(Color(0xFFFEF08A), radius = w * 0.14f, center = Offset(w * 0.50f, h * 0.40f))
            drawCircle(strokeColor, radius = w * 0.14f, center = Offset(w * 0.50f, h * 0.40f), style = Stroke(2.4f))

            // Cheeks
            drawCircle(Color(0xFFFFAEC0), radius = 3f, center = Offset(w * 0.42f, h * 0.42f))
            drawCircle(Color(0xFFFFAEC0), radius = 3f, center = Offset(w * 0.58f, h * 0.42f))

            // Sleepy eyes: ⌒ ⌒
            val eye1 = Path().apply {
                moveTo(w * 0.43f, h * 0.38f)
                quadraticTo(w * 0.46f, h * 0.35f, w * 0.49f, h * 0.38f)
            }
            drawPath(eye1, strokeColor, style = Stroke(2f, cap = StrokeCap.Round))

            val eye2 = Path().apply {
                moveTo(w * 0.51f, h * 0.38f)
                quadraticTo(w * 0.54f, h * 0.35f, w * 0.57f, h * 0.38f)
            }
            drawPath(eye2, strokeColor, style = Stroke(2f, cap = StrokeCap.Round))

            // Smile
            val smile = Path().apply {
                moveTo(w * 0.48f, h * 0.44f)
                quadraticTo(w * 0.50f, h * 0.46f, w * 0.52f, h * 0.44f)
            }
            drawPath(smile, strokeColor, style = Stroke(1.8f, cap = StrokeCap.Round))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "“no breaks yet… your procrastination lore starts here 🌼”",
            color = Color(0xFF5A4945),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "ready when you need water, fresh air, or an academic pause ✨",
            color = Color(0xFFA08F8B),
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
