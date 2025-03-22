package com.hacksolotls.tracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log")
data class Log(
    @PrimaryKey() val id: Int = 0
)
