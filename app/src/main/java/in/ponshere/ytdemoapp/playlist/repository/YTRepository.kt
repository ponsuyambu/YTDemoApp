package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.datasource.YTDataSource
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult
import javax.inject.Inject

const val GOOGLE_SIGN_IN_YOUTUBE_SCOPE = "https://www.googleapis.com/auth/youtube"

class YTRepository @Inject constructor(
    private val localDataSource: YTLocalDataSource,
    private val remoteDataSource: YTRemoteDataSource
) : YTDataSource{
    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        val playlistsResult = remoteDataSource.getPlaylists(pageToken)
        if(localDataSource.isAlreadyCached(playlistsResult.nextPageToken).not()) {
            localDataSource.addPlaylistResult(playlistsResult)
        }
        return playlistsResult
    }
}