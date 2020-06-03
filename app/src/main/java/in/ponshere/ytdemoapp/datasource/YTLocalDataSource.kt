package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.db.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.extensions.toJson
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import javax.inject.Inject

private const val FIRST_PAGE_TOKEN = "!!FIRST_PAGE!!"

class YTLocalDataSource @Inject constructor(
    database: AppDatabase
) : YTDataSource {

    private val playlistResultDao = database.playlistResultDao()

    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?): YTVideosResult {
        TODO("Not yet implemented")
    }

    suspend fun isAlreadyCached(pageToken: String) = playlistResultDao.findByPageToken(pageToken) != null

    fun addPlaylistResult(playlistResult: YTPlaylistsResult) {

    }

    fun deletePlaylistResults() {

    }
}