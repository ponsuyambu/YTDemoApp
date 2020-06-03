package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.authentcation.ui.AuthenticationScreen
import `in`.ponshere.ytdemoapp.player.YoutubePlayerScreen
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import `in`.ponshere.ytdemoapp.playlistdetails.PlaylistDetailsScreen
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeAuthenticationScreen(): AuthenticationScreen

    @ContributesAndroidInjector
    abstract fun contributePlaylistScreen(): PlaylistScreen

    @ContributesAndroidInjector
    abstract fun contributePlaylistDetailsScreen(): PlaylistDetailsScreen

    @ContributesAndroidInjector
    abstract fun youtubePlayerScreen(): YoutubePlayerScreen
}