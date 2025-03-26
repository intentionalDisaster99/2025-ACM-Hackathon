package com.hacksolotls.tracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hacksolotls.tracker.data.util.InstantConverter
import com.josiwhitlock.estresso.Ester
import java.time.Instant

/**
 * Entity for the [Log] table in the database.
 *
 * Columns:
 *  - medication:       Which ester of estradiol was taken
 *  - timestamp:        Instant at which the medication was taken
 *  - dosage:           Amount of medication, in mg, that was taken
 *  - daysTilNext:      Number of days until the user should be notified to take their next dose
 */
@Entity(tableName = "log")
@TypeConverters(InstantConverter::class)
data class Log(
    /** Auto-generated identifier for this log. */
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    /** Which ester of estradiol was taken. */
    val medication: Ester,
    /** Instant at which the medication was taken. */
    val timestamp: Instant,
    /** Amount of medication, in mg, that was taken. */
    val dosage: Double,
    /** Number of days until the user should be notified to take their next dose. */
    val daysTilNext: Int
)
