package com.example.challangesetfeb.data.model

import androidx.compose.ui.graphics.Color

data class Confetti(
    val id: Int,
    val startX: Float,
    val startY: Float,
    val color: Color,
    val shape: ConfettiShape,
    val size: Float,
    val rotation: Float,
    val fallSpeed: Float,
    val horizontalDrift: Float
)
