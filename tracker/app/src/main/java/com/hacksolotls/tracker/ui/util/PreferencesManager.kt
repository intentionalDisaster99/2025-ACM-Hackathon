package com.hacksolotls.tracker.ui.util

import android.content.Context

class PreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Keys
    private val NAME_KEY = "name"
    private val DARK_MODE_KEY = "dark_mode"

    // Save name
    fun saveName(name: String) {
        sharedPreferences.edit().putString(NAME_KEY, name).apply()
    }

    // Retrieve name
    fun getName(): String? {
        return sharedPreferences.getString(NAME_KEY, "name")
    }

    // Save dark mode preference
    fun saveDarkMode(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_MODE_KEY, isDarkMode).apply()
    }

    // Retrieve dark mode preference
    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean(DARK_MODE_KEY, false)
    }
}
