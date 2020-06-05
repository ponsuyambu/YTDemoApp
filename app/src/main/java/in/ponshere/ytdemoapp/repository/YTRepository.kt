package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy.CACHE_FIRST
import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy.CACHE_ONLY
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.datasource.FIRST_PAGE_TOKEN
import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.playlist.models.YTVideoInfoResult
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
        val playlistsResult = remoteDataSource.getPlaylists(pageToken) ?: return null
        updatePlaylistResultsCache(pageToken, playlistsResult)
        return playlistsResult
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult? {
        val localPlaylistVideosResult = localDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)
        if (networkState.isConnected.not() || (cacheRetrievalPolicy == CACHE_FIRST && localPlaylistVideosResult != null))
            return if (localPlaylistVideosResult != null) enhanceVideoResults(localPlaylistVideosResult, CACHE_ONLY) else null
        val playlistVideosResult = remoteDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy) ?: return null
        updatePlaylistVideosResultCache(pageToken, playlistVideosResult, playlistId)
        return enhanceVideoResults(playlistVideosResult)
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String?): YTVideosResult {
        val videos = remoteDataSource.getVideosFor(searchTerm, pageToken)
        return enhanceVideoResults(videos)
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

    private suspend fun enhanceVideoResults(videoResults: YTVideosResult, cacheRetrievalPolicy: CacheRetrievalPolicy = CACHE_FIRST): YTVideosResult {
        val videoIds = videoResults.listModels.map { m -> m.videoId }
        val allVideoInfos = mutableMapOf<String, YTVideoInfoResult>()
        val cachedVideoInfos = localDataSource.getVideosInfo(videoIds)
        allVideoInfos.putAll(cachedVideoInfos)
        if (cacheRetrievalPolicy != CACHE_ONLY) {
            val remoteVideoIds = videoIds.filter { id -> cachedVideoInfos.keys.contains(id).not() }
            val remoteVideoInfos = remoteDataSource.getVideosInfo(remoteVideoIds)
            localDataSource.addVideoInfos(remoteVideoInfos.values.toList())
            allVideoInfos.putAll(remoteVideoInfos)
        }
        videoResults.listModels.forEach { video ->
            video.duration = allVideoInfos[video.videoId]?.duration
        }
        return videoResults
    }

    override suspend fun getVideosInfo(videoIds: List<String>): Map<String, YTVideoInfoResult> {
        TODO("Not yet implemented")
    }

    private fun getActiveDataSource(): YTDataSource {
        return if (networkState.isConnected) remoteDataSource else localDataSource
    }
}