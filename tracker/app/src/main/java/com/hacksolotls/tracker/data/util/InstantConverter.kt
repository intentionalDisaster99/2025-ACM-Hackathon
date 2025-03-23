package com.hacksolotls.tracker.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? {
        return instant?.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(epochMillis: Long?): Instant? {
        return epochMillis?.let { Instant.ofEpochMilli(it) }
    }
}

fun millisToLocalDate(millis: Long?): LocalDate {
    return Instant.ofEpochMilli(millis ?: Instant.now().toEpochMilli())
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
