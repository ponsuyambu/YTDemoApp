package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.common.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.utils.NetworkState
import javax.inject.Inject

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

class YTRepository @Inject constructor(
    private val localDataSource: YTLocalDataSource,
    private val remoteDataSource: YTRemoteDataSource,
    private val networkState: NetworkState
) : YTDataSource{
    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        if(networkState.isConnected.not()) {
            return localDataSource.getPlaylists(pageToken)
        }

        val playlistsResult = remoteDataSource.getPlaylists(pageToken)

        if(isFirstPageCall(pageToken)) {
            localDataSource.deletePlaylistResults()
            localDataSource.addPlaylistResult(playlistsResult, pageToken)
        }else if(localDataSource.isPlaylistAlreadyCached(playlistsResult.nextPageToken).not()) {
            localDataSource.addPlaylistResult(playlistsResult, pageToken)
        }

        return playlistsResult
    }

    private fun isFirstPageCall(pageToken: String?) = pageToken == null

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult {
        if(networkState.isConnected.not()) {
            return localDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)
        } else if(cacheRetrievalPolicy == CacheRetrievalPolicy.CACHE_FIRST &&
                localDataSource.isNextPlaylistVideosDataAvailable(playlistId, pageToken)) { // user is online
                return localDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)
        }

        val playlistVideosResult = remoteDataSource.getPlaylistVideos(playlistId, pageToken, cacheRetrievalPolicy)

        if(isFirstPageCall(pageToken)) {
            localDataSource.deletePlaylistVideosResults(playlistId)
            localDataSource.addPlaylistVideosResult(playlistId, playlistVideosResult, pageToken)
        }else if(localDataSource.isPlaylistVideosAlreadyCached(playlistId, playlistVideosResult.nextPageToken).not()) {
            localDataSource.addPlaylistVideosResult(playlistId, playlistVideosResult, pageToken)
        }
        return playlistVideosResult
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String?): YTVideosResult {
        return remoteDataSource.getVideosFor(searchTerm, pageToken)
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String?): Boolean {
        return getActiveDataSource().isNextPlaylistDataAvailable(pageToken)
    }

    override suspend fun isNextPlaylistVideosDataAvailable(playlistId: String, pageToken: String?): Boolean {
        return getActiveDataSource().isNextPlaylistVideosDataAvailable(playlistId, pageToken)
    }

    private fun getActiveDataSource() : YTDataSource {
        return if(networkState.isConnected) remoteDataSource else localDataSource
    }
}