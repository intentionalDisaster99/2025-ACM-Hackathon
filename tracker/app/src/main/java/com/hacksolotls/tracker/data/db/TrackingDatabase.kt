package com.hacksolotls.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database to store estrogen tracking information.
 */
@Database(entities = [Log::class], version = 1, exportSchema = false)
abstract class TrackingDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}