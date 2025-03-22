package com.hacksolotls.tracker.hilt

import android.content.Context
import androidx.room.Room
import com.hacksolotls.tracker.data.db.Database
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
    fun provideAppDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLogDao(database: Database) : LogDao {
        return database.logDao()
    }
}