package com.hacksolotls.estrotracker.data.util

import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd")
private val rangeDisplayDateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yy")

public val HorizontalAxisValueFormatter = CartesianValueFormatter { context, value, _ ->
    // 'value' is the Double representing your x-coordinate (epoch ms)
    val dateTime = Instant.ofEpochMilli(value.toLong())
        .atZone(ZoneId.systemDefault())

    dateTime.format(dateTimeFormatter)
}

fun longToDateTime(long: Long): String? {
    val dateTime = Instant.ofEpochMilli(long)
        .atZone(ZoneId.systemDefault())

    return dateTime.format(rangeDisplayDateTimeFormatter)
}