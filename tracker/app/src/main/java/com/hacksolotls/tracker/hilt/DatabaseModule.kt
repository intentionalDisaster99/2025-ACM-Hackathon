package com.hacksolotls.tracker.hilt

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hacksolotls.tracker.data.db.TrackingDatabase
import com.hacksolotls.tracker.data.db.LogDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): TrackingDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            TrackingDatabase::class.java,
            "tracking_database"
        ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE).fallbackToDestructiveMigration().build()

        println("Successful database creation!")
        println(db)
        return db
    }

    @Provides
    @Singleton
    fun provideLogDao(trackingDatabase: TrackingDatabase) : LogDao {
        println("Successful DAO creation")
        return trackingDatabase.logDao()
    }
}