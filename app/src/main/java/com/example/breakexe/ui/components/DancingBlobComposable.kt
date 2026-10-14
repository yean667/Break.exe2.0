package com.example.breakexe.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breakexe.model.BlobExpression
import com.example.breakexe.model.CharacterAccessory
import com.example.breakexe.model.CharacterConfig
import com.example.breakexe.model.CharacterModel
import kotlinx.coroutines.delay

@Composable
fun DancingBlobComposable(
    isDancing: Boolean,
    expression: BlobExpression = BlobExpression.DANCING,
    config: CharacterConfig = CharacterConfig(),
    onPoke: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isPoked by remember { mutableStateOf(false) }
    var pokeCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPoked) {
        if (isPoked) {
            delay(500)
            isPoked = false
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "blob_motion")

    // Bounce animation
    val bounceY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isDancing) -14f else -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isDancing) 600 else 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    // Sway / wiggle rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = if (isDancing) -4f else -1.5f,
        targetValue = if (isDancing) 4f else 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(if (isDancing) 600 else 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    // Floating note 1 animation
    val note1Y by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = -35f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "note1"
    )

    // Floating note 2 animation
    val note2Y by infiniteTransition.animateFloat(
        initialValue = 10f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "note2"
    )

    val currentExpression = if (isPoked) BlobExpression.HAPPY else expression

    Box(
        modifier = modifier
            .testTag("dancing_blob_container")
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPoked = true
                pokeCount++
                onPoke()
            },
        contentAlignment = Alignment.Center
    ) {
        // Floating ZZZ when sleepy
        if (expression == BlobExpression.SLEEPY && !isDancing) {
            Text(
                text = "z z Z",
                color = Color(0xFFE8556D),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = (-12).dp)
            )
        }

        // Floating Musical Notes when dancing
        if (isDancing) {
            Text(
                text = "♫",
                color = Color(0xFF38292C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-6).dp, y = note1Y.dp)
            )
            Text(
                text = "♪",
                color = Color(0xFF38292C),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 6.dp, y = note2Y.dp)
            )
        }

        // Motion marks (( on left and )) on right
        if (isDancing) {
            Text(
                text = "((",
                color = Color(0x9938292C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = (-16).dp)
            )
            Text(
                text = "))",
                color = Color(0x9938292C),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 16.dp)
            )
        }

        // Squish popup text
        if (isPoked) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-36).dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                val pokeMsg = when (pokeCount % 4) {
                    0 -> "giggle! :3"
                    1 -> "you poked me!"
                    2 -> "best study buddy ever!"
                    else -> "dance dance! ♫"
                }
                Text(
                    text = pokeMsg,
                    color = Color(0xFFE8556D),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // The Character Canvas
        val bodyColor = parseHexColor(config.bodyColor, Color.White)
        val blushColor = parseHexColor(config.blushColor, Color(0xFFFFAEC0))
        val headphoneColor = parseHexColor(config.headphoneColor, Color(0xFFFFAEC0))
        val strokeColor = Color(0xFF38292C)

        val pokeScale = if (isPoked) 1.15f else 1f

        Canvas(
            modifier = Modifier
                .size(140.dp)
                .offset(y = bounceY.dp)
                .rotate(rotation)
                .scale(pokeScale)
        ) {
            val w = size.width
            val h = size.height

            // 1. Ground Shadow
            drawOval(
                color = Color(0x66EADCDA),
                topLeft = Offset(w * 0.22f, h * 0.88f),
                size = Size(w * 0.56f, h * 0.08f)
            )

            // 2. Feet / Paws
            drawOval(
                color = bodyColor,
                topLeft = Offset(w * 0.30f, h * 0.82f),
                size = Size(w * 0.16f, h * 0.10f)
            )
            drawOval(
                color = strokeColor,
                topLeft = Offset(w * 0.30f, h * 0.82f),
                size = Size(w * 0.16f, h * 0.10f),
                style = Stroke(width = 3f)
            )

            drawOval(
                color = bodyColor,
                topLeft = Offset(w * 0.54f, h * 0.82f),
                size = Size(w * 0.16f, h * 0.10f)
            )
            drawOval(
                color = strokeColor,
                topLeft = Offset(w * 0.54f, h * 0.82f),
                size = Size(w * 0.16f, h * 0.10f),
                style = Stroke(width = 3f)
            )

            // 3. Bunny Ears if Bunny
            if (config.model == CharacterModel.BUNNY) {
                // Left Ear
                val leftEar = Path().apply {
                    moveTo(w * 0.36f, h * 0.36f)
                    cubicTo(w * 0.28f, h * 0.12f, w * 0.32f, h * 0.06f, w * 0.38f, h * 0.06f)
                    cubicTo(w * 0.45f, h * 0.06f, w * 0.44f, h * 0.18f, w * 0.41f, h * 0.36f)
                    close()
                }
                drawPath(leftEar, bodyColor)
                drawPath(leftEar, strokeColor, style = Stroke(width = 3.2f))

                // Left Inner Blush
                val leftInner = Path().apply {
                    moveTo(w * 0.37f, h * 0.32f)
                    cubicTo(w * 0.32f, h * 0.16f, w * 0.34f, h * 0.10f, w * 0.38f, h * 0.10f)
                    cubicTo(w * 0.42f, h * 0.10f, w * 0.42f, h * 0.18f, w * 0.40f, h * 0.32f)
                    close()
                }
                drawPath(leftInner, blushColor.copy(alpha = 0.65f))

                // Right Ear
                val rightEar = Path().apply {
                    moveTo(w * 0.59f, h * 0.36f)
                    cubicTo(w * 0.56f, h * 0.18f, w * 0.55f, h * 0.06f, w * 0.62f, h * 0.06f)
                    cubicTo(w * 0.69f, h * 0.06f, w * 0.72f, h * 0.12f, w * 0.64f, h * 0.36f)
                    close()
                }
                drawPath(rightEar, bodyColor)
                drawPath(rightEar, strokeColor, style = Stroke(width = 3.2f))

                // Right Inner Blush
                val rightInner = Path().apply {
                    moveTo(w * 0.60f, h * 0.32f)
                    cubicTo(w * 0.58f, h * 0.18f, w * 0.58f, h * 0.10f, w * 0.62f, h * 0.10f)
                    cubicTo(w * 0.66f, h * 0.10f, w * 0.68f, h * 0.16f, w * 0.63f, h * 0.32f)
                    close()
                }
                drawPath(rightInner, blushColor.copy(alpha = 0.65f))
            }

            // 4. Bear Ears if Bear
            if (config.model == CharacterModel.BEAR) {
                // Left Ear
                drawCircle(bodyColor, radius = w * 0.11f, center = Offset(w * 0.32f, h * 0.34f))
                drawCircle(strokeColor, radius = w * 0.11f, center = Offset(w * 0.32f, h * 0.34f), style = Stroke(3.2f))
                drawCircle(blushColor.copy(alpha = 0.65f), radius = w * 0.055f, center = Offset(w * 0.32f, h * 0.34f))

                // Right Ear
                drawCircle(bodyColor, radius = w * 0.11f, center = Offset(w * 0.68f, h * 0.34f))
                drawCircle(strokeColor, radius = w * 0.11f, center = Offset(w * 0.68f, h * 0.34f), style = Stroke(3.2f))
                drawCircle(blushColor.copy(alpha = 0.65f), radius = w * 0.055f, center = Offset(w * 0.68f, h * 0.34f))
            }

            // 5. Body based on model
            when (config.model) {
                CharacterModel.SLIME -> {
                    val slimePath = Path().apply {
                        moveTo(w * 0.50f, h * 0.28f)
                        cubicTo(w * 0.66f, h * 0.28f, w * 0.82f, h * 0.44f, w * 0.83f, h * 0.66f)
                        cubicTo(w * 0.84f, h * 0.82f, w * 0.92f, h * 0.86f, w * 0.86f, h * 0.90f)
                        cubicTo(w * 0.78f, h * 0.93f, w * 0.22f, h * 0.93f, w * 0.14f, h * 0.90f)
                        cubicTo(w * 0.08f, h * 0.86f, w * 0.16f, h * 0.82f, w * 0.17f, h * 0.66f)
                        cubicTo(w * 0.18f, h * 0.44f, w * 0.34f, h * 0.28f, w * 0.50f, h * 0.28f)
                        close()
                    }
                    drawPath(slimePath, bodyColor)
                    drawPath(slimePath, strokeColor, style = Stroke(width = 3.4f, join = StrokeJoin.Round))

                    // Slime shine
                    drawArc(
                        color = Color.White,
                        startAngle = 180f,
                        sweepAngle = 70f,
                        useCenter = false,
                        topLeft = Offset(w * 0.30f, h * 0.36f),
                        size = Size(w * 0.20f, h * 0.14f),
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round)
                    )
                    drawCircle(Color.White, radius = 2.5f, center = Offset(w * 0.28f, h * 0.48f))
                }
                CharacterModel.CLOUD -> {
                    val cloudPath = Path().apply {
                        moveTo(w * 0.26f, h * 0.75f)
                        cubicTo(w * 0.16f, h * 0.75f, w * 0.13f, h * 0.64f, w * 0.19f, h * 0.57f)
                        cubicTo(w * 0.14f, h * 0.50f, w * 0.20f, h * 0.39f, w * 0.30f, h * 0.39f)
                        cubicTo(w * 0.33f, h * 0.30f, w * 0.46f, h * 0.29f, w * 0.51f, h * 0.31f)
                        cubicTo(w * 0.59f, h * 0.27f, w * 0.71f, h * 0.30f, w * 0.73f, h * 0.39f)
                        cubicTo(w * 0.83f, h * 0.39f, w * 0.89f, h * 0.50f, w * 0.84f, h * 0.59f)
                        cubicTo(w * 0.90f, h * 0.66f, w * 0.86f, h * 0.75f, w * 0.76f, h * 0.75f)
                        cubicTo(w * 0.70f, h * 0.88f, w * 0.30f, h * 0.88f, w * 0.26f, h * 0.75f)
                        close()
                    }
                    drawPath(cloudPath, bodyColor)
                    drawPath(cloudPath, strokeColor, style = Stroke(width = 3.4f, join = StrokeJoin.Round))
                }
                else -> {
                    // Default chubby blob/bunny/bear body
                    val blobPath = Path().apply {
                        moveTo(w * 0.25f, h * 0.75f)
                        cubicTo(w * 0.18f, h * 0.51f, w * 0.28f, h * 0.30f, w * 0.50f, h * 0.30f)
                        cubicTo(w * 0.72f, h * 0.30f, w * 0.82f, h * 0.51f, w * 0.75f, h * 0.75f)
                        cubicTo(w * 0.72f, h * 0.84f, w * 0.62f, h * 0.87f, w * 0.50f, h * 0.87f)
                        cubicTo(w * 0.38f, h * 0.87f, w * 0.28f, h * 0.84f, w * 0.25f, h * 0.75f)
                        close()
                    }
                    drawPath(blobPath, bodyColor)
                    drawPath(blobPath, strokeColor, style = Stroke(width = 3.4f, join = StrokeJoin.Round))
                }
            }

            // 6. Left Arm waving
            val leftArm = Path().apply {
                moveTo(w * 0.26f, h * 0.59f)
                cubicTo(w * 0.16f, h * 0.54f, w * 0.13f, h * 0.46f, w * 0.20f, h * 0.47f)
                cubicTo(w * 0.25f, h * 0.48f, w * 0.29f, h * 0.54f, w * 0.29f, h * 0.56f)
            }
            drawPath(leftArm, bodyColor)
            drawPath(leftArm, strokeColor, style = Stroke(width = 3.2f, cap = StrokeCap.Round))

            // 7. Right Arm waving
            val rightArm = Path().apply {
                moveTo(w * 0.74f, h * 0.59f)
                cubicTo(w * 0.84f, h * 0.54f, w * 0.87f, h * 0.46f, w * 0.80f, h * 0.47f)
                cubicTo(w * 0.75f, h * 0.48f, w * 0.71f, h * 0.54f, w * 0.71f, h * 0.56f)
            }
            drawPath(rightArm, bodyColor)
            drawPath(rightArm, strokeColor, style = Stroke(width = 3.2f, cap = StrokeCap.Round))

            // 8. Cheeks - cute blush
            drawOval(
                blushColor,
                topLeft = Offset(w * 0.35f, h * 0.60f),
                size = Size(w * 0.08f, h * 0.05f)
            )
            drawOval(
                blushColor,
                topLeft = Offset(w * 0.65f, h * 0.60f),
                size = Size(w * 0.08f, h * 0.05f)
            )

            // Bear snout if Bear
            if (config.model == CharacterModel.BEAR) {
                drawOval(
                    Color.White.copy(alpha = 0.85f),
                    topLeft = Offset(w * 0.43f, h * 0.54f),
                    size = Size(w * 0.14f, h * 0.10f)
                )
            }

            // 9. Facial Expressions
            when (currentExpression) {
                BlobExpression.DANCING -> {
                    // Left crescent eye ^
                    val leftEye = Path().apply {
                        moveTo(w * 0.39f, h * 0.54f)
                        quadraticTo(w * 0.43f, h * 0.49f, w * 0.47f, h * 0.54f)
                    }
                    drawPath(leftEye, strokeColor, style = Stroke(3.2f, cap = StrokeCap.Round))

                    // Right crescent eye ^
                    val rightEye = Path().apply {
                        moveTo(w * 0.53f, h * 0.54f)
                        quadraticTo(w * 0.57f, h * 0.49f, w * 0.61f, h * 0.54f)
                    }
                    drawPath(rightEye, strokeColor, style = Stroke(3.2f, cap = StrokeCap.Round))

                    // Smile
                    val smile = Path().apply {
                        moveTo(w * 0.47f, h * 0.60f)
                        quadraticTo(w * 0.50f, h * 0.64f, w * 0.53f, h * 0.60f)
                    }
                    drawPath(smile, strokeColor, style = Stroke(2.8f, cap = StrokeCap.Round))
                }
                BlobExpression.HAPPY -> {
                    // Open sparkly eyes
                    drawCircle(strokeColor, radius = w * 0.024f, center = Offset(w * 0.43f, h * 0.53f))
                    drawCircle(strokeColor, radius = w * 0.024f, center = Offset(w * 0.57f, h * 0.53f))
                    drawCircle(Color.White, radius = w * 0.008f, center = Offset(w * 0.44f, h * 0.52f))
                    drawCircle(Color.White, radius = w * 0.008f, center = Offset(w * 0.58f, h * 0.52f))

                    // Big smile
                    val smile = Path().apply {
                        moveTo(w * 0.46f, h * 0.59f)
                        quadraticTo(w * 0.50f, h * 0.64f, w * 0.54f, h * 0.59f)
                    }
                    drawPath(smile, strokeColor, style = Stroke(2.8f, cap = StrokeCap.Round))
                }
                BlobExpression.SLEEPY -> {
                    // Sleepy closed eyes ⌒ ⌒
                    val leftEye = Path().apply {
                        moveTo(w * 0.39f, h * 0.54f)
                        quadraticTo(w * 0.43f, h * 0.58f, w * 0.47f, h * 0.54f)
                    }
                    drawPath(leftEye, strokeColor, style = Stroke(3f, cap = StrokeCap.Round))

                    val rightEye = Path().apply {
                        moveTo(w * 0.53f, h * 0.54f)
                        quadraticTo(w * 0.57f, h * 0.58f, w * 0.61f, h * 0.54f)
                    }
                    drawPath(rightEye, strokeColor, style = Stroke(3f, cap = StrokeCap.Round))

                    val mouth = Path().apply {
                        moveTo(w * 0.485f, h * 0.60f)
                        quadraticTo(w * 0.50f, h * 0.62f, w * 0.515f, h * 0.60f)
                    }
                    drawPath(mouth, strokeColor, style = Stroke(2.4f, cap = StrokeCap.Round))
                }
                BlobExpression.CONFUSED -> {
                    // One circle eye, one tilted eye
                    drawCircle(strokeColor, radius = w * 0.023f, center = Offset(w * 0.42f, h * 0.53f))
                    val rightEye = Path().apply {
                        moveTo(w * 0.54f, h * 0.54f)
                        quadraticTo(w * 0.57f, h * 0.51f, w * 0.61f, h * 0.54f)
                    }
                    drawPath(rightEye, strokeColor, style = Stroke(3f, cap = StrokeCap.Round))

                    // Mouth
                    val mouth = Path().apply {
                        moveTo(w * 0.47f, h * 0.60f)
                        quadraticTo(w * 0.50f, h * 0.58f, w * 0.53f, h * 0.61f)
                    }
                    drawPath(mouth, strokeColor, style = Stroke(2.6f, cap = StrokeCap.Round))

                    // Sweat drop
                    drawOval(
                        Color(0xFFBCE0FD),
                        topLeft = Offset(w * 0.68f, h * 0.46f),
                        size = Size(w * 0.04f, h * 0.06f)
                    )
                }
            }

            // 10. Cute Accessories
            when (config.accessory) {
                CharacterAccessory.HEADPHONES -> {
                    // Headband arch
                    drawArc(
                        color = strokeColor,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.23f, h * 0.20f),
                        size = Size(w * 0.54f, h * 0.42f),
                        style = Stroke(width = 5.5f, cap = StrokeCap.Round)
                    )
                    // Top Cushion
                    drawArc(
                        color = headphoneColor,
                        startAngle = 210f,
                        sweepAngle = 120f,
                        useCenter = false,
                        topLeft = Offset(w * 0.32f, h * 0.21f),
                        size = Size(w * 0.36f, h * 0.30f),
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )
                    // Left Ear Cup
                    drawRoundRect(
                        color = headphoneColor,
                        topLeft = Offset(w * 0.18f, h * 0.40f),
                        size = Size(w * 0.09f, h * 0.19f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = strokeColor,
                        topLeft = Offset(w * 0.18f, h * 0.40f),
                        size = Size(w * 0.09f, h * 0.19f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                        style = Stroke(width = 3.2f)
                    )

                    // Right Ear Cup
                    drawRoundRect(
                        color = headphoneColor,
                        topLeft = Offset(w * 0.73f, h * 0.40f),
                        size = Size(w * 0.09f, h * 0.19f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = strokeColor,
                        topLeft = Offset(w * 0.73f, h * 0.40f),
                        size = Size(w * 0.09f, h * 0.19f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                        style = Stroke(width = 3.2f)
                    )
                }
                CharacterAccessory.BOW -> {
                    val cx = w * 0.50f
                    val cy = h * 0.27f
                    // Left loop
                    val leftBow = Path().apply {
                        moveTo(cx - 2f, cy)
                        cubicTo(cx - 15f, cy - 12f, cx - 20f, cy - 2f, cx - 14f, cy + 6f)
                        close()
                    }
                    drawPath(leftBow, headphoneColor)
                    drawPath(leftBow, strokeColor, style = Stroke(2.8f))

                    // Right loop
                    val rightBow = Path().apply {
                        moveTo(cx + 2f, cy)
                        cubicTo(cx + 15f, cy - 12f, cx + 20f, cy - 2f, cx + 14f, cy + 6f)
                        close()
                    }
                    drawPath(rightBow, headphoneColor)
                    drawPath(rightBow, strokeColor, style = Stroke(2.8f))

                    // Center Knot
                    drawCircle(Color.White, radius = 5f, center = Offset(cx, cy))
                    drawCircle(strokeColor, radius = 5f, center = Offset(cx, cy), style = Stroke(2.5f))

                    // Ribbon tails
                    drawLine(strokeColor, Offset(cx - 3f, cy + 4f), Offset(cx - 8f, cy + 16f), strokeWidth = 2.5f, cap = StrokeCap.Round)
                    drawLine(strokeColor, Offset(cx + 3f, cy + 4f), Offset(cx + 8f, cy + 16f), strokeWidth = 2.5f, cap = StrokeCap.Round)
                }
                CharacterAccessory.SPROUT -> {
                    val cx = w * 0.50f
                    val cy = h * 0.28f
                    // Stem
                    drawLine(Color(0xFF16A34A), Offset(cx, cy + 6f), Offset(cx, cy - 8f), strokeWidth = 3f, cap = StrokeCap.Round)
                    // Left Leaf
                    val leftLeaf = Path().apply {
                        moveTo(cx, cy - 8f)
                        cubicTo(cx - 12f, cy - 16f, cx - 14f, cy - 4f, cx, cy - 4f)
                        close()
                    }
                    drawPath(leftLeaf, Color(0xFF86EFAC))
                    drawPath(leftLeaf, Color(0xFF16A34A), style = Stroke(2f))

                    // Right Leaf
                    val rightLeaf = Path().apply {
                        moveTo(cx, cy - 8f)
                        cubicTo(cx + 12f, cy - 16f, cx + 14f, cy - 4f, cx, cy - 4f)
                        close()
                    }
                    drawPath(rightLeaf, Color(0xFF4ADE80))
                    drawPath(rightLeaf, Color(0xFF16A34A), style = Stroke(2f))
                }
                CharacterAccessory.STAR -> {
                    val cx = w * 0.64f
                    val cy = h * 0.35f
                    // Golden star pin
                    val star = Path().apply {
                        moveTo(cx, cy - 10f)
                        lineTo(cx + 3f, cy - 3f)
                        lineTo(cx + 10f, cy)
                        lineTo(cx + 3f, cy + 3f)
                        lineTo(cx, cy + 10f)
                        lineTo(cx - 3f, cy + 3f)
                        lineTo(cx - 10f, cy)
                        lineTo(cx - 3f, cy - 3f)
                        close()
                    }
                    drawPath(star, Color(0xFFFBBF24))
                    drawPath(star, strokeColor, style = Stroke(2f))
                    drawCircle(Color.White, radius = 2f, center = Offset(cx, cy))
                }
            }
        }
    }
}

private fun parseHexColor(hex: String, fallback: Color): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = cleanHex.toLong(16)
        if (cleanHex.length == 6) {
            Color(0xFF000000 or colorInt)
        } else {
            Color(colorInt)
        }
    } catch (e: Exception) {
        fallback
    }
}
