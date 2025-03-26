package com.hacksolotls.tracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Custom application to let Hilt take over.
 */
@HiltAndroidApp
class TrackerApp : Application()