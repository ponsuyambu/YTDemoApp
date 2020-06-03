package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.utils.NetworkState
import `in`.ponshere.ytdemoapp.utils.NetworkStateHolder
import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATABASE_NAME = "ytdemo_app"

@Module
class ApplicationModule() {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDatabase( applicationContext: Context) : AppDatabase{
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideNetworkStateHolder() : NetworkState {
        return NetworkStateHolder
    }
}