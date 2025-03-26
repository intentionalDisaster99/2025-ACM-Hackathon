package com.hacksolotls.tracker.hilt

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hacksolotls.tracker.data.db.LogDao
import com.hacksolotls.tracker.data.db.TrackingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that handles how Hilt should create dependencies,
 * specifically singleton instances of them.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    /**
     * Creates a [TrackingDatabase] instance for the app to use.
     *
     * @param ApplicationContext application context used in db creation
     */
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): TrackingDatabase {
        // Get a database instance
        val db = Room.databaseBuilder(
            context.applicationContext,
            TrackingDatabase::class.java,
            "tracking_database"
        ).build()

        Log.d("Database", "Successful database creation: $db")

        return db
    }

    /**
     * Creates a DAO to be used to make queries and changes
     * to the [TrackingDatabase]'s [Log] table.
     *
     * @param TrackingDatabase to interact with
     */
    @Provides
    @Singleton
    fun provideLogDao(trackingDatabase: TrackingDatabase) : LogDao {
        Log.d("Database", "Successful DAO creation")
        return trackingDatabase.logDao()
    }
}