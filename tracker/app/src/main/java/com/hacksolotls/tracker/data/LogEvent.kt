package com.hacksolotls.tracker.data

import com.josiwhitlock.estresso.Ester

/**
 * Interface defining [LogEvent]s.
 */
sealed interface LogEvent {
    /** Save the currently entered [com.hacksolotls.tracker.data.db.Log]. */
    data object SaveLog: LogEvent
    /** Set the [Ester] currently stored. */
    data class SetEster(val ester: Ester): LogEvent
    /** Set the dosage currently stored. */
    data class SetDosage(val dosage: String): LogEvent
    /** Set the timestamp currently stored. */
    data class SetTime(val time: Long): LogEvent
    /** Set the number of days until next currently stored. */
    data class SetDaysTilNext(val days: String): LogEvent
    /** Show the [com.hacksolotls.tracker.ui.composables.UpsertLogDialog]. */
    data object ShowDialog: LogEvent
    /** Hide the [com.hacksolotls.tracker.ui.composables.UpsertLogDialog]. */
    data object HideDialog: LogEvent
    /** Currently not implemented. */
    data class DeleteMedication(val medication: Ester): LogEvent
}