package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.playlist.models.YTVideoInfoResult

const val FIRST_PAGE_TOKEN = "!!FIRST_PAGE!!"

interface YTDataSource {
    suspend fun getPlaylists(pageToken: String = FIRST_PAGE_TOKEN): YTPlaylistsResult?
    suspend fun getPlaylistVideos(playlistId: String, pageToken: String = FIRST_PAGE_TOKEN, cacheRetrievalPolicy: CacheRetrievalPolicy = CacheRetrievalPolicy.NETWORK_FIRST): YTVideosResult?
    suspend fun getVideosFor(searchTerm: String, pageToken: String? = null): YTVideosResult
    suspend fun isNextPlaylistDataAvailable(pageToken: String = FIRST_PAGE_TOKEN) : Boolean
    suspend fun getVideosInfo(videoIds: List<String>): Map<String, YTVideoInfoResult>
}