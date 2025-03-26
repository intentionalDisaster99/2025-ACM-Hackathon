package com.hacksolotls.tracker.data

import com.hacksolotls.tracker.data.db.Log
import com.josiwhitlock.estresso.Ester

/**
 * Data class that stores the currently entered information from the
 * [com.hacksolotls.tracker.ui.composables.UpsertLogDialog] as well as
 * other useful [Log]-based information.
 */
data class LogState(
    // logs to display, maybe through a calendar and displaying per month
    val logs: List<Log> = emptyList(),
    // Most recent log in the database, used for calculating next dose
    val mostRecentLog: List<Log> = emptyList(),
    // Which med was taken
    val ester: Ester = Ester.VALERATE,
    // How much was taken
    val dosage: String = "1.0",
    // When it was taken
    val timestamp: Long = 0L,
    // Days until next dose
    val daysTilNext: String = "5",
    // Are we currently adding a log?
    val isAddingLog: Boolean = false,
)
