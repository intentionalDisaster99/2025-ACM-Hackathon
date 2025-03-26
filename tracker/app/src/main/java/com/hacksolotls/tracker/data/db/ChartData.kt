package com.hacksolotls.tracker.data.db

/**
 * Data point for graphing estrogen levels.
 */
data class ChartData(
    /** Time of data point */
    val timestamp: Long,
    /** Estrogen level at data point */
    val eLevel: Double
)
