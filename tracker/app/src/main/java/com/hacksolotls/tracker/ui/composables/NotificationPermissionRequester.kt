package com.hacksolotls.tracker.ui.composables


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            // For older Android versions, permission is granted by default or
            // handled differently (e.g., at app install time).
            mutableStateOf(true)
        }
    }

    // This launcher will be triggered to request the permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasNotificationPermission = isGranted
            if (isGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    )

    // Example usage: You might have a button to trigger the request
    // or request it immediately on app launch.
    // For immediate request on launch (only on Android 13+):
    LaunchedEffect(Unit) {
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // You can also add a UI element to request it again if denied,
    // or explain why it's needed.
    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Button(onClick = {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }) {
            Text("Enable Notifications")
        }
        Text("Notifications are important for reminders. Please enable them in app settings if you denied.")
    } else if (hasNotificationPermission) {
        Text("Notification permission granted!")
        // You can now proceed with scheduling/showing notifications
        // For example, call your scheduler here if you want:
        // val notificationScheduler = hiltViewModel<NotificationScheduler>() // Example
        // LaunchedEffect(Unit) {
        //     notificationScheduler.scheduleNotification(...)
        // }
    }
}