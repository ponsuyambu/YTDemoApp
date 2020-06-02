package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

interface YTRepository {
    suspend fun getPlaylists(pageToken: String? = null): YTPlaylistsResult
}