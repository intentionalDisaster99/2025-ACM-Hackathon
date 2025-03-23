package com.hacksolotls.tracker.data

import com.josiwhitlock.estresso.Ester

sealed interface LogEvent {
    data object SaveLog: LogEvent
    data class SetEster(val ester: Ester): LogEvent
    data class SetDosage(val dosage: String): LogEvent
    data class SetTime(val time: Long): LogEvent
    data class SetDaysTilNext(val days: String): LogEvent
    data object ShowDialog: LogEvent
    data object HideDialog: LogEvent
    data class DeleteMedication(val medication: Ester): LogEvent
}