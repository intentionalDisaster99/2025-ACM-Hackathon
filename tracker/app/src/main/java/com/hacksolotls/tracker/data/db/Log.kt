package com.hacksolotls.tracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hacksolotls.tracker.data.util.InstantConverter
import com.josiwhitlock.estresso.Ester
import java.time.Instant

@Entity(tableName = "log")
@TypeConverters(InstantConverter::class)
data class Log(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medication: Ester,
    val timestamp: Instant,
    val dosage: Double,
    val daysTilNext: Int
)
