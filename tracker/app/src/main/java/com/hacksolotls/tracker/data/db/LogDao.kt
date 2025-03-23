package com.hacksolotls.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LogDao {
    @Query("SELECT * FROM log ORDER BY timestamp")
    fun getAllLogs(): LiveData<List<Log>>

    @Upsert
    suspend fun upsertLog(log: Log)
}