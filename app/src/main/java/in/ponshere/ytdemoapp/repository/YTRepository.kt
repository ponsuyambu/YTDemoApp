package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
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
        }else if(localDataSource.isAlreadyCached(playlistsResult.nextPageToken).not()) {
            localDataSource.addPlaylistResult(playlistsResult, pageToken)
        }

        return playlistsResult
    }

    private fun isFirstPageCall(pageToken: String?) = pageToken == null

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?): YTVideosResult {
        return remoteDataSource.getPlaylistVideos(playlistId, pageToken)
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String?): Boolean {
        return getActiveDataSource().isNextPlaylistDataAvailable(pageToken)
    }

    private fun getActiveDataSource() : YTDataSource {
        return if(networkState.isConnected) remoteDataSource else localDataSource
    }
}