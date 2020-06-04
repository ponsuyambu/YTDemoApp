package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistVideosFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun playlistVideosFragment(): PlaylistVideosFragment
}