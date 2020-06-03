package `in`.ponshere.ytdemoapp

import `in`.ponshere.ytdemoapp.di.DaggerAppComponent
import `in`.ponshere.ytdemoapp.utils.NetworkStateHolder.registerConnectivityMonitor
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class YTDemoApp : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        registerConnectivityMonitor()
        //TODO: Add condition once the major development is done
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}