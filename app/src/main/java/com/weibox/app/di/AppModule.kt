package com.weibox.app.di

import android.content.Context
import androidx.room.Room
import com.weibox.app.data.db.AppDatabase
import com.weibox.app.data.prefs.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "weibox.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePreferences(@ApplicationContext ctx: Context): AppPreferences =
        AppPreferences(ctx)
}
