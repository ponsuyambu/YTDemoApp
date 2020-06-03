package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult
import javax.inject.Inject

class YTLocalDataSource @Inject constructor(
    private val database: AppDatabase
) : YTDataSource {

    private val playlistResultDao = database.playlistResultDao()

    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        TODO("Not yet implemented")
    }

    fun isAlreadyCached(pageToken: String) = playlistResultDao.findByPageToken(pageToken) != null

    fun addPlaylistResult(playlistResult: YTPlaylistsResult) {

    }
}