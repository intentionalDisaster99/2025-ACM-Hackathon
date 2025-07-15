package com.hacksolotls.tracker.notifs

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hacksolotls.tracker.hilt.NotificationModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduledNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        println("onreceive")
        context?.let {
            val message = intent?.getStringExtra(NOTIFICATION_MESSAGE_KEY) ?: "Scheduled Notification"
            val title = intent?.getStringExtra(NOTIFICATION_TITLE_KEY) ?: "Reminder"
            showNotification(it, title, message)
        }
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(notificationManager)

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Scheduled Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications triggered by the AlarmManager"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "scheduled_notifications"
        const val NOTIFICATION_ID = 1001 // Unique ID for the notification
        const val NOTIFICATION_TITLE_KEY = "notification_title"
        const val NOTIFICATION_MESSAGE_KEY = "notification_message"
    }
}