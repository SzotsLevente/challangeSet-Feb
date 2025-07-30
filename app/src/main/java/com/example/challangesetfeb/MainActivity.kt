package com.example.challangesetfeb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.challangesetfeb.ui.theme.ChallengeSetFebTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeSetFebTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ThousandSeparatorPicker(modifier = Modifier.padding(innerPadding))
                }
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
        animationSpec = tween(durationMillis = 1000 , delayMillis = 200),
        label = "bg"
    )
    val animatedTextColor = animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 1000,delayMillis = 200),
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

@Preview(showBackground = true)
@Composable
fun ThousandSeparatorPickerPreview() {
    ChallengeSetFebTheme(darkTheme = false, dynamicColor = false) {
        ThousandSeparatorPicker()
    }
}