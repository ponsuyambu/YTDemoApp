package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult

interface YTDataSource {
    suspend fun getPlaylists(pageToken: String? = null): YTPlaylistsResult
}