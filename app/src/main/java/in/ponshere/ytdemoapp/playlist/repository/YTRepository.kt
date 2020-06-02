package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

interface YTRepository {
    suspend fun getPlaylists(): List<YTPlaylist>
}