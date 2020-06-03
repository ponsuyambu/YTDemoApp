package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.db.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.extensions.toJson
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

private const val FIRST_PAGE_TOKEN = "!!FIRST_PAGE!!"

class YTLocalDataSource @Inject constructor(
    database: AppDatabase
) : YTDataSource {

    private val playlistResultDao = database.playlistResultDao()

    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        val playlistEntity = playlistResultDao.findByPageToken(pageToken ?: FIRST_PAGE_TOKEN)
        Timber.d("getPlaylists ::: ${playlistEntity}")
        return Gson().fromJson(playlistEntity?.resultResponse,YTPlaylistsResult::class.java)
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?): YTVideosResult {
        TODO("Not yet implemented")
    }

    suspend fun isAlreadyCached(pageToken: String) = playlistResultDao.findByPageToken(pageToken) != null

    suspend fun addPlaylistResult(playlistResult: YTPlaylistsResult, pageToken: String?) {
        playlistResultDao.insert(YTPlaylistEntity(playlistResult.toJson(), pageToken ?: FIRST_PAGE_TOKEN))
    }

    suspend fun deletePlaylistResults() {
        playlistResultDao.deleteAll()
    }
}