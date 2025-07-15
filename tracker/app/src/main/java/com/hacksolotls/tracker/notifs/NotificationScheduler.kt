package com.hacksolotls.tracker.notifs

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class NotificationScheduler @Inject constructor (
    @ApplicationContext private val appContext: Context,
    private val alarmManager: AlarmManager
) {
    fun scheduleNotification(title: String, message: String, dayOfWeek: DayOfWeek, hour: Int, minute: Int) {
        val intent = Intent(appContext, ScheduledNotificationReceiver::class.java).apply {
            putExtra(ScheduledNotificationReceiver.NOTIFICATION_TITLE_KEY, title)
            putExtra(ScheduledNotificationReceiver.NOTIFICATION_MESSAGE_KEY, message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            generateUniqueRequestCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val targetDateTime = LocalDateTime.now()
            .with(DayOfWeek.from(dayOfWeek))
            .withHour(hour)
            .withMinute(minute)
            .withSecond(0)
            .withNano(0)

        val triggerDateTime = if (targetDateTime.isBefore(LocalDateTime.now())) {
            targetDateTime.plusWeeks(1)
        } else {
            targetDateTime
        }

        val triggerMillis = triggerDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        println("Notification trigger time: " + triggerMillis)


        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            AlarmManager.INTERVAL_DAY * 7,
            pendingIntent
        )
    }

    fun cancelNotification(requestCode: Int) {
        val intent = Intent(appContext, ScheduledNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun generateUniqueRequestCode(): Int {
        return System.currentTimeMillis().toInt() // Simple way to generate a unique code
    }
}