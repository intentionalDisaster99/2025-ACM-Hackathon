package com.hacksolotls.tracker.data

import android.util.Log
import com.josiwhitlock.estresso.Ester

data class LogState(
// logs to display, maybe through a calendar and displaying per month
    val logs: List<Log> = emptyList(),
    // Which med was taken
    val ester: Ester = Ester.VALERATE,
    val dosage: String = "1.0",
    val timestamp: String = "0",
    val daysTilNext: String ="5",
    val isAddingLog: Boolean = false,
)
