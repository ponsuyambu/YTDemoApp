package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy.CACHE_FIRST
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.datasource.FIRST_PAGE_TOKEN
import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.utils.NetworkState
import javax.inject.Inject

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

class YTRepository @Inject constructor(
    private val localDataSource: YTLocalDataSource,
    private val remoteDataSource: YTRemoteDataSource,
    private val networkState: NetworkState
) : YTDataSource {

    override suspend fun getPlaylists(pageToken: String): YTPlaylistsResult? {
        if (networkState.isConnected.not()) return localDataSource.getPlaylists(pageToken)
        val playlistsResult = remoteDataSource.getPlaylists(pageToken)
        updatePlaylistResultsCache(pageToken, playlistsResult)
        return playlistsResult
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult? {
        val localPlaylistVideosResult = localDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)
        if (networkState.isConnected.not() || (cacheRetrievalPolicy == CACHE_FIRST && localPlaylistVideosResult != null)) return localPlaylistVideosResult
        val playlistVideosResult = remoteDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)
        updatePlaylistVideosResultCache(pageToken, playlistVideosResult, playlistId)
        return playlistVideosResult
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String?): YTVideosResult {
        return remoteDataSource.getVideosFor(searchTerm, pageToken)
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String): Boolean {
        return getActiveDataSource().isNextPlaylistDataAvailable(pageToken)
    }

    private suspend fun updatePlaylistResultsCache(pageToken: String, playlistsResult: YTPlaylistsResult) {
        if (isFirstPageCall(pageToken)) localDataSource.deleteAllPlaylistResults()
        else localDataSource.deletePlaylistResults(pageToken)
        localDataSource.addPlaylistResult(playlistsResult, pageToken)
    }

    private suspend fun updatePlaylistVideosResultCache(pageToken: String, videosResult: YTVideosResult, playlistId: String) {
        if (isFirstPageCall(pageToken)) localDataSource.deletePlaylistVideosResult(playlistId)
        else localDataSource.deletePlaylistVideosResults(playlistId, pageToken)
        localDataSource.addPlaylistVideosResult(playlistId, videosResult, pageToken)
    }

    private fun isFirstPageCall(pageToken: String) = pageToken == FIRST_PAGE_TOKEN

    private suspend fun enhanceVideoResults(videoResults: YTVideosResult): YTVideosResult {
        val videoIds = videoResults.listModels?.map { m -> m.videoId } ?: emptyList()
        val cachedVideos = localDataSource.getVideosInfo(videoIds)
        val remoteVideoIds = videoIds.filter { id -> cachedVideos.keys.contains(id).not() }
        val remoteVideos = remoteDataSource.getVideosInfo(remoteVideoIds)
        val allVideos = mutableMapOf<String, String>()
        allVideos.putAll(cachedVideos)
        allVideos.putAll(remoteVideos)

        videoResults.listModels?.forEach { video ->
            allVideos[video.videoId]?.let { duration ->
                video.duration = duration
            }
        }
        return videoResults
    }

    override suspend fun getVideosInfo(videoIds: List<String>): Map<String, String> {
        TODO("Not yet implemented")
    }

    private fun getActiveDataSource(): YTDataSource {
        return if (networkState.isConnected) remoteDataSource else localDataSource
    }
}