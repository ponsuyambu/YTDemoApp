package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.YTDemoApp
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, ApplicationModule::class, ActivityModule::class]
)
interface AppComponent : AndroidInjector<YTDemoApp> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}