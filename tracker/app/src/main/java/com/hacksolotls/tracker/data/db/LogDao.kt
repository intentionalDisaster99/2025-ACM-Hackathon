package com.hacksolotls.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query("SELECT * FROM log ORDER BY timestamp")
    fun getAllLogs(): LiveData<List<Log>>

    @Query("SELECT * FROM log ORDER BY timestamp DESC LIMIT 1")
    fun getMostRecentLog(): LiveData<Log>

    @Query("SELECT * FROM log ORDER BY timestamp LIMIT :x")
    fun getXMostRecentLogs(x: Int): LiveData<List<Log>>

    @Upsert
    suspend fun upsertLog(log: Log)
}