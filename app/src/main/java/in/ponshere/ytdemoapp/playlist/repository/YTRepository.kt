package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.utils.NetworkUtils
import javax.inject.Inject

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

class YTRepository @Inject constructor(
    private val localDataSource: YTLocalDataSource,
    private val remoteDataSource: YTRemoteDataSource,
    private val networkUtils: NetworkUtils
) : YTDataSource{
    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
//        if(networkUtils.isOnline().not()) {
//            return localDataSource.getPlaylists(pageToken)
//        }

        val playlistsResult = remoteDataSource.getPlaylists(pageToken)
//        if(pageToken == null) {
//            localDataSource.deletePlaylistResults()
//            localDataSource.addPlaylistResult(playlistsResult)
//        }else if(localDataSource.isAlreadyCached(playlistsResult.nextPageToken).not()) {
//            localDataSource.addPlaylistResult(playlistsResult)
//        }
        return playlistsResult
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?): YTVideosResult {
        return remoteDataSource.getPlaylistVideos(playlistId, pageToken)
    }
}