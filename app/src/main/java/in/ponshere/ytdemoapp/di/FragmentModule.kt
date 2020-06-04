package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistVideosFragment
import `in`.ponshere.ytdemoapp.search.ui.SearchVideosFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun playlistVideosFragment(): PlaylistVideosFragment

    @ContributesAndroidInjector
    abstract fun searchVideosFragment(): SearchVideosFragment
}