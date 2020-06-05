package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.utils.NetworkState
import `in`.ponshere.ytdemoapp.utils.NetworkStateHolder
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val APP_NAME = "ytdemo_app"

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
            AppDatabase::class.java, APP_NAME
        ).build()
    }

    @Provides
    fun provideNetworkStateHolder() : NetworkState {
        return NetworkStateHolder
    }

    @Provides
    @Singleton
    fun providePicasso(context: Context) : Picasso {
        return Picasso.Builder(context)
            .downloader(OkHttp3Downloader(context, Long.MAX_VALUE))
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(APP_NAME,Context.MODE_PRIVATE)
    }
}