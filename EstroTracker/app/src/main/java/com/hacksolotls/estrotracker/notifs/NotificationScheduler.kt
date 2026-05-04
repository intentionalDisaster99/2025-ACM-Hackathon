package com.hacksolotls.estrotracker.notifs

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class NotificationScheduler @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val alarmManager: AlarmManager
) {
    @SuppressLint("ObsoleteSdkInt")
    fun scheduleNotification(
        title: String,
        message: String,
        id: Int,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int = 0
    ) {
        val requestCode = id // generateUniqueRequestCode()

        val intent = Intent(appContext, ScheduledNotificationReceiver::class.java).apply {
            putExtra(ScheduledNotificationReceiver.Companion.NOTIFICATION_TITLE_KEY, title)
            putExtra(ScheduledNotificationReceiver.Companion.NOTIFICATION_MESSAGE_KEY, message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            appContext,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        var targetDateTime = LocalDateTime.of(year, month, day, hour, minute, second)

        val triggerMillis = targetDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        Log.d(
            "AlarmSchedulerDebug",
            "Scheduling ONE-TIME notification (approximate) for: $targetDateTime ($triggerMillis ms)"
        )
        Log.d("AlarmSchedulerDebug", "Current time: ${LocalDateTime.now()} (${System.currentTimeMillis()} ms)")
        Log.d("AlarmSchedulerDebug", "Request Code (Notification ID): $requestCode")


        // Use setAndAllowWhileIdle for reliable, but not exact, alarms that respect Doze mode.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        } else {
            // For older APIs, setExact is the best reliable option if not using setAndAllowWhileIdle
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerMillis,
                    pendingIntent
                )
            }
        }
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

    companion object {
        // lol boobs
        const val REMINDER_CODE = 80085
        const val SCHEDULED_CODE = 8008
    }
}