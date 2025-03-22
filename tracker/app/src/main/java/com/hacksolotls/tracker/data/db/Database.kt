package com.hacksolotls.tracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Log::class], version = 0, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun logDao(): LogDao
}