package com.hacksolotls.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

/**
 * Interface defining DAO functions for the [Log] table in the room database.
 */
@Dao
interface LogDao {
    /**
     * Retrieve all logs in the [Log] table sorted in order of earliest to most recent.
     *
     * @return [LiveData]-wrapped [List] of sorted [Log]s
     */
    @Query("SELECT * FROM log ORDER BY timestamp")
    fun getAllLogs(): LiveData<List<Log>>

    /**
     * Retrieve the most recent log in the [Log] table.
     *
     * @return [Log] within a [LiveData]
     */
    @Query("SELECT * FROM log ORDER BY timestamp DESC LIMIT 1")
    fun getMostRecentLog(): LiveData<Log>

    /**
     * Retrieve the [x] most recent [Log]s in the [Log] table, sorted by recent first.
     *
     * @return [LiveData]-wrapped [List] of sorted [Log]s
     */
    @Query("SELECT * FROM log ORDER BY timestamp DESC LIMIT :x")
    fun getXMostRecentLogs(x: Int): LiveData<List<Log>>

    /**
     * Update or insert the provided [Log] into the database.
     */
    @Upsert
    suspend fun upsertLog(log: Log)
}