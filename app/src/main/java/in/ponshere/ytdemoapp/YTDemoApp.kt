package `in`.ponshere.ytdemoapp

import android.app.Application
import timber.log.Timber

class YTDemoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //TODO: Add condition once the major development is done
        Timber.plant(Timber.DebugTree());
    }
}