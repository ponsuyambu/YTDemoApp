package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult

interface YTDataSource {
    suspend fun getPlaylists(pageToken: String? = null): YTPlaylistsResult
    suspend fun getPlaylistVideos(playlistId: String, pageToken: String? = null, cacheRetrievalPolicy: CacheRetrievalPolicy = CacheRetrievalPolicy.NETWORK_FIRST): YTVideosResult
    suspend fun getVideosFor(searchTerm: String, pageToken: String? = null): YTVideosResult
    suspend fun isNextPlaylistDataAvailable(pageToken: String?) : Boolean
    suspend fun isNextPlaylistVideosDataAvailable(playlistId: String, pageToken: String?) : Boolean
}