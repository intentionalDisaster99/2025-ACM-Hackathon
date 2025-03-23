package com.hacksolotls.tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hacksolotls.tracker.ui.composables.AppNavHost
import com.hacksolotls.tracker.ui.theme.TrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerTheme {
                AppNavHost()
            }
        }
    }
}
