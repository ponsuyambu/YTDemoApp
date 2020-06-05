package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.db.playlistdetails.YTPlaylistVideosEntity
import `in`.ponshere.ytdemoapp.extensions.toJson
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import com.google.gson.Gson
import javax.inject.Inject

private const val FIRST_PAGE_TOKEN = "!!FIRST_PAGE!!"

class YTLocalDataSource @Inject constructor(
    database: AppDatabase
) : YTDataSource {

    private val playlistDao = database.playlistDao()
    private val playlistVideosDao = database.playlistVideosDao()

    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        val playlistEntity = playlistDao.findByPageToken(pageToken ?: FIRST_PAGE_TOKEN)
            ?: return YTPlaylistsResult(null, "")
        return Gson().fromJson(playlistEntity.resultResponse, YTPlaylistsResult::class.java)
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String?, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult {
        val playlistVideos = playlistVideosDao.findByPageToken(playlistId, pageToken ?: FIRST_PAGE_TOKEN)
                ?: return YTVideosResult(null, "")
        return Gson().fromJson(playlistVideos.resultResponse, YTVideosResult::class.java)
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String?): YTVideosResult {
        TODO("Not yet implemented")
    }

    suspend fun addPlaylistResult(playlistResult: YTPlaylistsResult, pageToken: String?) {
        playlistDao.insert(
            YTPlaylistEntity(
                playlistResult.toJson(),
                pageToken ?: FIRST_PAGE_TOKEN
            )
        )
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String?): Boolean {
        return playlistDao.findByPageToken(pageToken ?: FIRST_PAGE_TOKEN) != null
    }

    suspend fun isPlaylistAlreadyCached(pageToken: String) = playlistDao.findByPageToken(pageToken) != null

    suspend fun deletePlaylistResults()  = playlistDao.deleteAll()


    suspend fun addPlaylistVideosResult(playlistId: String, videosResult: YTVideosResult, pageToken: String?) {
        playlistVideosDao.insert(
            YTPlaylistVideosEntity(
                videosResult.toJson(),
                playlistId,
                pageToken ?: FIRST_PAGE_TOKEN
            )
        )
    }

    override suspend fun isNextPlaylistVideosDataAvailable(playlistId: String, pageToken: String?): Boolean {
        return playlistVideosDao.findByPageToken(playlistId, pageToken ?: FIRST_PAGE_TOKEN) != null
    }

    suspend fun isPlaylistVideosAlreadyCached(playlistId: String, pageToken: String) = playlistVideosDao.findByPageToken(playlistId, pageToken) != null

    suspend fun deletePlaylistVideosResults(playlistId: String)  = playlistVideosDao.deleteAll(playlistId)

}