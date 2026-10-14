package com.example.breakexe.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.breakexe.model.BreakReason

@Composable
fun ReasonIconComposable(
    reason: BreakReason,
    size: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        when (reason) {
            BreakReason.WATER -> {
                val drop = Path().apply {
                    moveTo(w * 0.50f, h * 0.10f)
                    cubicTo(w * 0.50f, h * 0.10f, w * 0.20f, h * 0.45f, w * 0.20f, h * 0.68f)
                    cubicTo(w * 0.20f, h * 0.85f, w * 0.33f, h * 0.95f, w * 0.50f, h * 0.95f)
                    cubicTo(w * 0.67f, h * 0.95f, w * 0.80f, h * 0.85f, w * 0.80f, h * 0.68f)
                    cubicTo(w * 0.80f, h * 0.45f, w * 0.50f, h * 0.10f, w * 0.50f, h * 0.10f)
                    close()
                }
                drawPath(drop, Color(0xFFE0F2FE))
                drawPath(drop, Color(0xFF0284C7), style = Stroke(2f))

                val shine = Path().apply {
                    moveTo(w * 0.62f, h * 0.60f)
                    quadraticTo(w * 0.62f, h * 0.76f, w * 0.48f, h * 0.80f)
                }
                drawPath(shine, Color(0xFF38BDF8), style = Stroke(1.5f, cap = StrokeCap.Round))
            }
            BreakReason.FOOD -> {
                // Toast snack with smiley
                drawRoundRect(
                    color = Color(0xFFFEF3C7),
                    topLeft = Offset(w * 0.15f, h * 0.18f),
                    size = Size(w * 0.70f, h * 0.64f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = Color(0xFFD97706),
                    topLeft = Offset(w * 0.15f, h * 0.18f),
                    size = Size(w * 0.70f, h * 0.64f),
                    cornerRadius = CornerRadius(6f, 6f),
                    style = Stroke(2f)
                )
                // Eyes
                drawCircle(Color(0xFF78350F), radius = 2f, center = Offset(w * 0.38f, h * 0.48f))
                drawCircle(Color(0xFF78350F), radius = 2f, center = Offset(w * 0.62f, h * 0.48f))
                // Smile
                val mouth = Path().apply {
                    moveTo(w * 0.44f, h * 0.58f)
                    quadraticTo(w * 0.50f, h * 0.64f, w * 0.56f, h * 0.58f)
                }
                drawPath(mouth, Color(0xFF78350F), style = Stroke(1.5f, cap = StrokeCap.Round))
            }
            BreakReason.PHONE -> {
                drawRoundRect(
                    color = Color(0xFFF1F5F9),
                    topLeft = Offset(w * 0.25f, h * 0.10f),
                    size = Size(w * 0.50f, h * 0.80f),
                    cornerRadius = CornerRadius(5f, 5f)
                )
                drawRoundRect(
                    color = Color(0xFF475569),
                    topLeft = Offset(w * 0.25f, h * 0.10f),
                    size = Size(w * 0.50f, h * 0.80f),
                    cornerRadius = CornerRadius(5f, 5f),
                    style = Stroke(2f)
                )
                drawRoundRect(
                    color = Color(0xFFE2E8F0),
                    topLeft = Offset(w * 0.32f, h * 0.22f),
                    size = Size(w * 0.36f, h * 0.52f),
                    cornerRadius = CornerRadius(2f, 2f)
                )
                drawCircle(Color(0xFF64748B), radius = 2f, center = Offset(w * 0.50f, h * 0.82f))
            }
            BreakReason.BATHROOM -> {
                drawRoundRect(
                    color = Color(0xFFE0F2FE),
                    topLeft = Offset(w * 0.25f, h * 0.20f),
                    size = Size(w * 0.50f, h * 0.50f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = Color(0xFF0284C7),
                    topLeft = Offset(w * 0.25f, h * 0.20f),
                    size = Size(w * 0.50f, h * 0.50f),
                    cornerRadius = CornerRadius(8f, 8f),
                    style = Stroke(2f)
                )
                drawLine(Color(0xFF0284C7), Offset(w * 0.38f, h * 0.70f), Offset(w * 0.38f, h * 0.88f), strokeWidth = 2f)
                drawLine(Color(0xFF0284C7), Offset(w * 0.62f, h * 0.70f), Offset(w * 0.62f, h * 0.88f), strokeWidth = 2f)
                drawLine(Color(0xFF0284C7), Offset(w * 0.30f, h * 0.88f), Offset(w * 0.70f, h * 0.88f), strokeWidth = 2f, cap = StrokeCap.Round)
            }
            BreakReason.BRAIN -> {
                val brain = Path().apply {
                    moveTo(w * 0.38f, h * 0.20f)
                    cubicTo(w * 0.20f, h * 0.20f, w * 0.15f, h * 0.40f, w * 0.20f, h * 0.52f)
                    cubicTo(w * 0.16f, h * 0.65f, w * 0.25f, h * 0.85f, w * 0.42f, h * 0.85f)
                    cubicTo(w * 0.50f, h * 0.85f, w * 0.50f, h * 0.80f, w * 0.50f, h * 0.80f)
                    cubicTo(w * 0.50f, h * 0.80f, w * 0.50f, h * 0.85f, w * 0.58f, h * 0.85f)
                    cubicTo(w * 0.75f, h * 0.85f, w * 0.84f, h * 0.65f, w * 0.80f, h * 0.52f)
                    cubicTo(w * 0.85f, h * 0.40f, w * 0.80f, h * 0.20f, w * 0.62f, h * 0.20f)
                    close()
                }
                drawPath(brain, Color(0xFFFFE4E6))
                drawPath(brain, Color(0xFFE11D48), style = Stroke(2f))
                drawLine(Color(0xFFFDA4AF), Offset(w * 0.50f, h * 0.28f), Offset(w * 0.50f, h * 0.75f), strokeWidth = 1.8f, cap = StrokeCap.Round)
            }
            BreakReason.GRASS -> {
                // Sprout
                drawLine(Color(0xFF16A34A), Offset(w * 0.50f, h * 0.90f), Offset(w * 0.50f, h * 0.48f), strokeWidth = 2f, cap = StrokeCap.Round)
                val leftLeaf = Path().apply {
                    moveTo(w * 0.50f, h * 0.48f)
                    cubicTo(w * 0.28f, h * 0.28f, w * 0.28f, h * 0.50f, w * 0.50f, h * 0.50f)
                }
                drawPath(leftLeaf, Color(0xFFDCFCE7))
                drawPath(leftLeaf, Color(0xFF16A34A), style = Stroke(1.8f))

                val rightLeaf = Path().apply {
                    moveTo(w * 0.50f, h * 0.60f)
                    cubicTo(w * 0.72f, h * 0.40f, w * 0.75f, h * 0.62f, w * 0.50f, h * 0.62f)
                }
                drawPath(rightLeaf, Color(0xFFBBF7D0))
                drawPath(rightLeaf, Color(0xFF16A34A), style = Stroke(1.8f))
            }
            BreakReason.EXISTING -> {
                // Sleepy cute sleeping blob
                val cat = Path().apply {
                    moveTo(w * 0.20f, h * 0.65f)
                    cubicTo(w * 0.20f, h * 0.35f, w * 0.40f, h * 0.28f, w * 0.50f, h * 0.28f)
                    cubicTo(w * 0.60f, h * 0.28f, w * 0.80f, h * 0.35f, w * 0.80f, h * 0.65f)
                    close()
                }
                drawPath(cat, Color.White)
                drawPath(cat, Color(0xFF475569), style = Stroke(2f))
                // Ears
                drawLine(Color(0xFF475569), Offset(w * 0.28f, h * 0.38f), Offset(w * 0.36f, h * 0.48f), strokeWidth = 1.8f, cap = StrokeCap.Round)
                drawLine(Color(0xFF475569), Offset(w * 0.72f, h * 0.38f), Offset(w * 0.64f, h * 0.48f), strokeWidth = 1.8f, cap = StrokeCap.Round)
                // Sleepy eyes
                val left = Path().apply {
                    moveTo(w * 0.36f, h * 0.58f)
                    quadraticTo(w * 0.42f, h * 0.52f, w * 0.48f, h * 0.58f)
                }
                drawPath(left, Color(0xFF475569), style = Stroke(1.5f, cap = StrokeCap.Round))
                val right = Path().apply {
                    moveTo(w * 0.52f, h * 0.58f)
                    quadraticTo(w * 0.58f, h * 0.52f, w * 0.64f, h * 0.58f)
                }
                drawPath(right, Color(0xFF475569), style = Stroke(1.5f, cap = StrokeCap.Round))
            }
            BreakReason.OTHER -> {
                // Sparkle
                val star = Path().apply {
                    moveTo(w * 0.50f, h * 0.10f)
                    lineTo(w * 0.56f, h * 0.38f)
                    lineTo(w * 0.85f, h * 0.50f)
                    lineTo(w * 0.56f, h * 0.62f)
                    lineTo(w * 0.50f, h * 0.90f)
                    lineTo(w * 0.44f, h * 0.62f)
                    lineTo(w * 0.15f, h * 0.50f)
                    lineTo(w * 0.44f, h * 0.38f)
                    close()
                }
                drawPath(star, Color(0xFFFEF08A))
                drawPath(star, Color(0xFFCA8A04), style = Stroke(1.8f))
            }
        }
    }
}
