@file:OptIn(ExperimentalAnimationApi::class)

package com.example.challangesetfeb

import android.Manifest
import android.content.Context
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.challangesetfeb.ui.theme.ChallengeSetFebTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
        setContent {
            ChallengeSetFebTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //ThousandSeparatorPicker(modifier = Modifier.padding(innerPadding))
                    //BatteryIndicator(modifier = Modifier.padding(innerPadding))
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SMSConfetti()
                    }
                }
            }
        }
    }
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
        //BatteryIndicator()
        SMSConfetti()
        //AudioWaveform()
    }
}