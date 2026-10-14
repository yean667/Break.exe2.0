package com.example.breakexe.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlobAvatarComposable(
    size: Dp = 32.dp,
    bodyColor: Color = Color.White,
    blushColor: Color = Color(0xFFFFAEC0),
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeColor = Color(0xFF38292C)

        // Blob body
        val body = Path().apply {
            moveTo(w * 0.20f, h * 0.65f)
            cubicTo(w * 0.15f, h * 0.42f, w * 0.35f, h * 0.25f, w * 0.50f, h * 0.25f)
            cubicTo(w * 0.65f, h * 0.25f, w * 0.85f, h * 0.42f, w * 0.80f, h * 0.65f)
            cubicTo(w * 0.77f, h * 0.77f, w * 0.67f, h * 0.82f, w * 0.50f, h * 0.82f)
            cubicTo(w * 0.33f, h * 0.82f, w * 0.23f, h * 0.77f, w * 0.20f, h * 0.65f)
            close()
        }
        drawPath(body, bodyColor)
        drawPath(body, strokeColor, style = Stroke(width = 2.4f))

        // Tiny ears
        val leftEar = Path().apply {
            moveTo(w * 0.27f, h * 0.35f)
            quadraticTo(w * 0.25f, h * 0.22f, w * 0.35f, h * 0.30f)
        }
        drawPath(leftEar, strokeColor, style = Stroke(2.2f, cap = StrokeCap.Round))

        val rightEar = Path().apply {
            moveTo(w * 0.73f, h * 0.35f)
            quadraticTo(w * 0.75f, h * 0.22f, w * 0.65f, h * 0.30f)
        }
        drawPath(rightEar, strokeColor, style = Stroke(2.2f, cap = StrokeCap.Round))

        // Blushes
        drawOval(blushColor, topLeft = Offset(w * 0.30f, h * 0.60f), size = Size(w * 0.12f, h * 0.08f))
        drawOval(blushColor, topLeft = Offset(w * 0.70f - w * 0.12f, h * 0.60f), size = Size(w * 0.12f, h * 0.08f))

        // Eyes
        val leftEye = Path().apply {
            moveTo(w * 0.35f, h * 0.50f)
            quadraticTo(w * 0.40f, h * 0.43f, w * 0.45f, h * 0.50f)
        }
        drawPath(leftEye, strokeColor, style = Stroke(2f, cap = StrokeCap.Round))

        val rightEye = Path().apply {
            moveTo(w * 0.55f, h * 0.50f)
            quadraticTo(w * 0.60f, h * 0.43f, w * 0.65f, h * 0.50f)
        }
        drawPath(rightEye, strokeColor, style = Stroke(2f, cap = StrokeCap.Round))

        // Smile
        val smile = Path().apply {
            moveTo(w * 0.45f, h * 0.60f)
            quadraticTo(w * 0.50f, h * 0.67f, w * 0.55f, h * 0.60f)
        }
        drawPath(smile, strokeColor, style = Stroke(1.8f, cap = StrokeCap.Round))
    }
}

@Composable
fun PeekingBlobComposable(
    text: String = "no judgment... ~ maybe",
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Canvas(modifier = Modifier.size(42.dp, 30.dp)) {
            val w = size.width
            val h = size.height
            val strokeColor = Color(0xFF38292C)

            val head = Path().apply {
                moveTo(w * 0.12f, h)
                cubicTo(w * 0.12f, h * 0.50f, w * 0.32f, h * 0.22f, w * 0.50f, h * 0.22f)
                cubicTo(w * 0.68f, h * 0.22f, w * 0.88f, h * 0.50f, w * 0.88f, h)
            }
            drawPath(head, Color.White)
            drawPath(head, strokeColor, style = Stroke(2.4f))

            // Ears
            val leftEar = Path().apply {
                moveTo(w * 0.20f, h * 0.40f)
                quadraticTo(w * 0.18f, h * 0.22f, w * 0.30f, h * 0.31f)
            }
            drawPath(leftEar, strokeColor, style = Stroke(2f))

            val rightEar = Path().apply {
                moveTo(w * 0.80f, h * 0.40f)
                quadraticTo(w * 0.82f, h * 0.22f, w * 0.70f, h * 0.31f)
            }
            drawPath(rightEar, strokeColor, style = Stroke(2f))

            // Blush
            drawOval(Color(0xFFFFAEC0), Offset(w * 0.28f, h * 0.62f), Size(w * 0.14f, h * 0.12f))
            drawOval(Color(0xFFFFAEC0), Offset(w * 0.72f - w * 0.14f, h * 0.62f), Size(w * 0.14f, h * 0.12f))

            // Eyes
            drawCircle(strokeColor, radius = w * 0.045f, center = Offset(w * 0.38f, h * 0.52f))
            drawCircle(strokeColor, radius = w * 0.045f, center = Offset(w * 0.62f, h * 0.52f))

            // Smile
            val smile = Path().apply {
                moveTo(w * 0.44f, h * 0.66f)
                quadraticTo(w * 0.50f, h * 0.74f, w * 0.56f, h * 0.66f)
            }
            drawPath(smile, strokeColor, style = Stroke(1.8f, cap = StrokeCap.Round))
        }

        Column(modifier = Modifier.offset(x = 4.dp)) {
            val parts = text.split("~")
            Text(
                text = parts.firstOrNull()?.trim() ?: "",
                color = Color(0xFF7D6B66),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            if (parts.size > 1) {
                Text(
                    text = "~ " + parts[1].trim(),
                    color = Color(0xFF9E8E8A),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun BooksStackComposable(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(54.dp, 40.dp)) {
        val w = size.width
        val h = size.height
        val strokeColor = Color(0xFF38292C)

        // Bottom Book (baby blue)
        drawRoundRect(
            color = Color(0xFFDCEBFA),
            topLeft = Offset(w * 0.12f, h * 0.66f),
            size = Size(w * 0.76f, h * 0.25f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(w * 0.12f, h * 0.66f),
            size = Size(w * 0.76f, h * 0.25f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(2.2f)
        )

        // Middle Book (pastel lavender)
        drawRoundRect(
            color = Color(0xFFF5E4F8),
            topLeft = Offset(w * 0.18f, h * 0.44f),
            size = Size(w * 0.66f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(w * 0.18f, h * 0.44f),
            size = Size(w * 0.66f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(2.2f)
        )

        // Top Book (pastel pink)
        drawRoundRect(
            color = Color(0xFFFFDFE5),
            topLeft = Offset(w * 0.25f, h * 0.22f),
            size = Size(w * 0.56f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
            color = strokeColor,
            topLeft = Offset(w * 0.25f, h * 0.22f),
            size = Size(w * 0.56f, h * 0.22f),
            cornerRadius = CornerRadius(4f, 4f),
            style = Stroke(2.2f)
        )

        // Bookmark ribbon
        val ribbon = Path().apply {
            moveTo(w * 0.68f, h * 0.22f)
            lineTo(w * 0.68f, h * 0.40f)
            lineTo(w * 0.72f, h * 0.35f)
            lineTo(w * 0.76f, h * 0.40f)
            lineTo(w * 0.76f, h * 0.22f)
            close()
        }
        drawPath(ribbon, Color(0xFFFFA8BA))
        drawPath(ribbon, strokeColor, style = Stroke(1.2f))
    }
}

@Composable
fun RadiatingTicksComposable(isLeft: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp, 28.dp)) {
        val w = size.width
        val h = size.height
        val tickColor = Color(0xFFFFA8BA)

        if (isLeft) {
            drawLine(tickColor, Offset(w * 0.8f, h * 0.3f), Offset(w * 0.2f, h * 0.15f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(tickColor, Offset(w * 0.9f, h * 0.5f), Offset(w * 0.1f, h * 0.5f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(tickColor, Offset(w * 0.8f, h * 0.7f), Offset(w * 0.2f, h * 0.85f), strokeWidth = 3f, cap = StrokeCap.Round)
        } else {
            drawLine(tickColor, Offset(w * 0.2f, h * 0.3f), Offset(w * 0.8f, h * 0.15f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(tickColor, Offset(w * 0.1f, h * 0.5f), Offset(w * 0.9f, h * 0.5f), strokeWidth = 3f, cap = StrokeCap.Round)
            drawLine(tickColor, Offset(w * 0.2f, h * 0.7f), Offset(w * 0.8f, h * 0.85f), strokeWidth = 3f, cap = StrokeCap.Round)
        }
    }
}
