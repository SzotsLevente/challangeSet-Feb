package com.example.challangesetfeb

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.example.challangesetfeb.data.model.Confetti
import com.example.challangesetfeb.data.model.ConfettiShape
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SMSConfetti(modifier: Modifier = Modifier) {
    var showConfetti by remember { mutableStateOf(false) }
    var confetti by remember { mutableStateOf(emptyList<Confetti>()) }
    var animationProgress by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        SmsReceiver.setOnValidSmsReceived {
            showConfetti = true
        }

        onDispose {
            SmsReceiver.clearCallback()
        }
    }

    LaunchedEffect(showConfetti) {
        if (showConfetti) {
            confetti = generateConfetti()
            animationProgress = 0f

            val animationDuration = 20000L
            val startTime = System.currentTimeMillis()

            while (animationProgress < 1f) {
                val currentTime = System.currentTimeMillis()
                animationProgress =
                    ((currentTime - startTime).toFloat() / animationDuration).coerceAtMost(1f)
                delay(16) // 60 FPS
            }

            showConfetti = false
            confetti = emptyList()
        }
    }

    if (showConfetti && confetti.isNotEmpty()) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            confetti.forEach { confetti ->
                drawConfetti(confetti, animationProgress)
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Waiting for SMS...",
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

private fun generateConfetti(): List<Confetti> {
    val colors = listOf(
        Color(0xFFFF6B6B),
        Color(0xFF4ECDC4),
        Color(0xFF45B7D1),
        Color(0xFF96CEB4),
        Color(0xFFE91E63),
        Color(0xFFFFC107),
        Color(0xFF9C27B0),
        Color(0xFFFF9800)
    )

    val shapes = ConfettiShape.values()
    val particleCount = 800

    return (0 until particleCount).map { index ->
        Confetti(
            id = index,
            startX = Random.nextFloat(),
            startY = -0.1f - (Random.nextFloat() * 0.5f),
            color = colors.random(),
            shape = shapes.random(),
            size = Random.nextFloat() * 20f + 10f,
            rotation = Random.nextFloat() * 360f,
            fallSpeed = Random.nextFloat() * 1.5f + 1.0f,
            horizontalDrift = (Random.nextFloat() - 0.5f) * 0.3f
        )
    }
}

private fun DrawScope.drawConfetti(confetti: Confetti, progress: Float) {
    val screenWidth = size.width
    val screenHeight = size.height

    val currentX =
        (confetti.startX * screenWidth) + (confetti.horizontalDrift * screenWidth * progress)
    val currentY =
        confetti.startY * screenHeight + (screenHeight * 1.2f * progress * confetti.fallSpeed)

    if (currentY > screenHeight + 50) return

    val currentRotation = confetti.rotation + (progress * 720f)
    val particleSize = confetti.size * (1f - progress * 0.2f)

    rotate(currentRotation, Offset(currentX, currentY)) {
        when (confetti.shape) {
            ConfettiShape.CIRCLE -> {
                drawCircle(
                    color = confetti.color,
                    radius = particleSize,
                    center = Offset(currentX, currentY)
                )
            }

            ConfettiShape.SQUARE -> {
                drawRect(
                    color = confetti.color,
                    topLeft = Offset(currentX - particleSize, currentY - particleSize),
                    size = Size(particleSize * 2, particleSize * 2)
                )
            }

            ConfettiShape.TRIANGLE -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(currentX, currentY - particleSize)
                    lineTo(currentX - particleSize, currentY + particleSize)
                    lineTo(currentX + particleSize, currentY + particleSize)
                    close()
                }
                drawPath(path, confetti.color)
            }
        }
    }
}