@file:OptIn(ExperimentalAnimationApi::class)

package com.example.challangesetfeb

import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challangesetfeb.ui.theme.ChallengeSetFebTheme
import com.example.challangesetfeb.ui.theme.Green
import com.example.challangesetfeb.ui.theme.Red
import com.example.challangesetfeb.ui.theme.RedAlt
import com.example.challangesetfeb.ui.theme.Surface
import com.example.challangesetfeb.ui.theme.SurfaceHigh
import com.example.challangesetfeb.ui.theme.SurfaceLow
import com.example.challangesetfeb.ui.theme.Yellow
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.JdkConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeSetFebTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //ThousandSeparatorPicker(modifier = Modifier.padding(innerPadding))
                    BatteryIndicator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun BatteryIndicator(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var batteryLevel by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            batteryLevel = getBatteryLevel(context)
            delay(5000)
            // Simulate battery level from 1% to 100%
           /* for (i in 1..100) {
                batteryLevel = i / 100f
                delay(110)
            }
            delay(4000)*/
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(color = Surface)
            .padding(16.dp)
    ) {
        val isHeartActive = batteryLevel > 0.0f && batteryLevel <= 0.2f
        val isCloverActive = batteryLevel >= 0.8f

        var heartScale by remember { mutableFloatStateOf(0.8f) }
        var heartTint by remember { mutableStateOf(SurfaceLow) }

        LaunchedEffect(isHeartActive) {
            if (isHeartActive) {
                while (isHeartActive) {
                    for (i in 0..50) {
                        heartScale = 0.8f + (0.5f * i / 50f)
                        heartTint = Red
                        delay(10)
                    }

                    for (i in 50 downTo 0) {
                        heartScale = 0.8f + (0.5f * i / 50f)
                        heartTint = RedAlt
                        delay(10)
                    }
                }
            } else {
                heartScale = 0.8f
                heartTint = SurfaceLow
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val cloverScale by animateFloatAsState(
                targetValue = if (isCloverActive) 1.1f else 0.8f,
                animationSpec = tween(durationMillis = 500),
                label = "clover_scale"
            )

            Icon(
                painter = painterResource(id = R.drawable.heart_inactive),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .scale(heartScale)
                    .padding(end = 8.dp),
                tint = heartTint
            )

            BatteryView(
                batteryLevel = batteryLevel,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(223.dp)
                    .height(68.dp)
            )

            Icon(
                painter = painterResource(id = R.drawable.clover_inactive),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .scale(cloverScale),
                tint = if (isCloverActive) Green else SurfaceLow
            )
        }
    }
}

@Composable
fun BatteryView(
    batteryLevel: Float,
    modifier: Modifier = Modifier
) {
    val batteryColor = when {
        batteryLevel >= 0.8f -> Green
        batteryLevel >= 0.2f && batteryLevel < 0.8f -> Yellow
        else -> Red
    }

    val animatedColor by animateColorAsState(
        targetValue = batteryColor,
        animationSpec = tween(durationMillis = 500),
        label = "battery_color"
    )

    Box(
        modifier = modifier
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(0f, 0f),
                size = Size(canvasWidth * 0.9f, canvasHeight),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
            )

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(canvasWidth * 0.89f, canvasHeight * 0.3f),
                size = Size(canvasWidth * 0.035f, canvasHeight * 0.4f),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )

            if (batteryLevel > 0) {
                drawRoundRect(
                    color = animatedColor,
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    size = Size(
                        (canvasWidth * 0.9f - 8.dp.toPx()) * batteryLevel,
                        canvasHeight - 8.dp.toPx()
                    ),
                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                )
            }

            val batteryInnerWidth = canvasWidth * 0.9f - 8.dp.toPx()
            val dividerSpacing = batteryInnerWidth / 5
            val dividerColor = SurfaceHigh
            val dividerWidth = 1.dp.toPx()
            val dividerHeight = canvasHeight - 12.dp.toPx()
            val dividerStartY = 6.dp.toPx()

            for (i in 1..4) {
                val dividerX = 4.dp.toPx() + (dividerSpacing * i) - dividerWidth / 2
                drawRect(
                    color = dividerColor,
                    topLeft = Offset(dividerX, dividerStartY),
                    size = Size(dividerWidth, dividerHeight)
                )
            }
        }
    }
}

@Composable
fun ThousandSeparatorPicker(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        var selectedSeparatorIndex by rememberSaveable { mutableIntStateOf(0) }
        Text(
            text = "Thousands separator",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(4.dp)
        ) {
            val list = listOf("1.000", "1,000", "1 000")
            list.forEachIndexed { index, text ->
                TextForHorizontalPicker(
                    text = text,
                    isSelected = index == selectedSeparatorIndex,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            selectedSeparatorIndex = index
                        }
                )
            }
        }
    }
}

@Composable
fun TextForHorizontalPicker(text: String, isSelected: Boolean, modifier: Modifier = Modifier) {
    val animatedBackgroundColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200),
        label = "bg"
    )
    val animatedTextColor = animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200),
        label = "text"
    )

    Text(
        text = text,
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        color = animatedTextColor.value,
        modifier = modifier
            .background(
                color = animatedBackgroundColor.value,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    )
}

fun getBatteryLevel(context: Context): Float {
    val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) / 100f
}

@Preview(showBackground = true)
@Composable
fun ThousandSeparatorPickerPreview() {
    ChallengeSetFebTheme(darkTheme = false, dynamicColor = false) {
        //ThousandSeparatorPicker()
        BatteryIndicator()
    }
}