package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.authentcation.ui.AuthenticationScreen
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeAuthenticationScreen(): AuthenticationScreen?

    @ContributesAndroidInjector
    abstract fun contributePlaylistScreen(): PlaylistScreen?
}