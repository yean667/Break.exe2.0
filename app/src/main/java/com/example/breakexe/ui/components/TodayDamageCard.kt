package com.example.breakexe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breakexe.data.BreakPreferencesRepository
import com.example.breakexe.model.TodayStats

@Composable
fun TodayDamageCard(
    stats: TodayStats,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("today_damage_card")
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0xFFE9F3FC))
            .border(1.dp, Color(0xFFD5E7F7), RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Column {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "today's damage",
                    color = Color(0xFF3A2C29),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                PeekingBlobComposable(text = "still better ~ than yesterday (i hope)")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2x2 Grid of Metric Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 1: Total break time
                    MetricCard(
                        iconEmoji = "⏱️",
                        badgeBg = Color(0xFFFFEAEF),
                        badgeBorder = Color(0xFFFFCCD7),
                        valueText = BreakPreferencesRepository.formatStatsTime(stats.totalSeconds),
                        label = "total break time",
                        modifier = Modifier.weight(1f)
                    )

                    // Card 2: Breaks taken
                    MetricCard(
                        iconEmoji = "☕",
                        badgeBg = Color(0xFFEBF7FF),
                        badgeBorder = Color(0xFFD0EAFA),
                        valueText = stats.count.toString(),
                        label = "breaks taken",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Card 3: Longest break
                    MetricCard(
                        iconEmoji = "🍡",
                        badgeBg = Color(0xFFFFF5EB),
                        badgeBorder = Color(0xFFFEE1C7),
                        valueText = BreakPreferencesRepository.formatStatsTime(stats.longestSeconds),
                        label = "longest break",
                        modifier = Modifier.weight(1f)
                    )

                    // Card 4: Average break
                    MetricCard(
                        iconEmoji = "🌱",
                        badgeBg = Color(0xFFEDFBEF),
                        badgeBorder = Color(0xFFCFECD4),
                        valueText = BreakPreferencesRepository.formatStatsTime(stats.averageSeconds),
                        label = "average break",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    iconEmoji: String,
    badgeBg: Color,
    badgeBorder: Color,
    valueText: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE3EDF6), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(badgeBg)
                    .border(1.dp, badgeBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = iconEmoji, fontSize = 16.sp)
            }

            Column {
                Text(
                    text = valueText,
                    color = Color(0xFF3A2C29),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )
                Text(
                    text = label,
                    color = Color(0xFF8A7B77),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
