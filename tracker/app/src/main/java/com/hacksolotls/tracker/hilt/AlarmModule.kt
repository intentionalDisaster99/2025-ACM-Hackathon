package com.hacksolotls.tracker.hilt

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext appContext: Context): android.app.AlarmManager {
        return appContext.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
    }
}