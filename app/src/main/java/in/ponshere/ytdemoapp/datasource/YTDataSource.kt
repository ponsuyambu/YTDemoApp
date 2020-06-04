package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult

interface YTDataSource {
    suspend fun getPlaylists(pageToken: String? = null): YTPlaylistsResult
    suspend fun getPlaylistVideos(playlistId: String, pageToken: String? = null): YTVideosResult
    suspend fun getVideosFor(searchTerm: String, pageToken: String? = null): YTVideosResult
    suspend fun isNextPlaylistDataAvailable(pageToken: String?) : Boolean
}