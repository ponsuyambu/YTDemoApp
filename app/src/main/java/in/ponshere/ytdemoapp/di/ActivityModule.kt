package `in`.ponshere.ytdemoapp.di

import `in`.ponshere.ytdemoapp.authentication.ui.AuthenticationScreen
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistDetailsScreen
import `in`.ponshere.ytdemoapp.search.ui.SearchScreen
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
    abstract fun contributeYoutubePlayerScreen(): VideoPlayerScreen

    @ContributesAndroidInjector
    abstract fun contributeSearchScreen(): SearchScreen
}