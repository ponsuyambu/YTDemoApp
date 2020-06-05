package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.db.AppDatabase
import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.db.playlistdetails.YTPlaylistVideosEntity
import `in`.ponshere.ytdemoapp.db.videos.YTVideoInfoEntity
import `in`.ponshere.ytdemoapp.extensions.toJson
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.playlist.models.YTVideoInfoResult
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

private const val KEY_IS_CHANNEL_AVAILABLE = "is_channel_available"

class YTLocalDataSource @Inject constructor(database: AppDatabase, private val sharedPreferences: SharedPreferences) : YTDataSource {
    private val playlistDao = database.playlistDao()
    private val playlistVideosDao = database.playlistVideosDao()
    private val videoInfoDao = database.videoInfoDao()

    override suspend fun getPlaylists(pageToken: String): YTPlaylistsResult? {
        val playlistEntity = playlistDao.find(pageToken)
        return if (playlistEntity != null) Gson().fromJson(playlistEntity.resultResponse, YTPlaylistsResult::class.java) else null
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult? {
        val playlistVideos = playlistVideosDao.find(playlistId, pageToken)
        return if (playlistVideos != null) Gson().fromJson(playlistVideos.resultResponse, YTVideosResult::class.java) else null
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String): YTVideosResult {
        TODO("Not valid operation")
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String) = playlistDao.find(pageToken) != null

    override suspend fun getVideosInfo(videoIds: List<String>): Map<String, YTVideoInfoResult> {
        val videoEntities = videoInfoDao.find(videoIds)
        return videoEntities.map { e -> Gson().fromJson(e.resultResponse, YTVideoInfoResult::class.java) }.map { it.videoId to it }.toMap()
    }

    suspend fun addPlaylistResult(playlistResult: YTPlaylistsResult, pageToken: String) {
        playlistDao.insert(YTPlaylistEntity(playlistResult.toJson(), pageToken))
    }

    suspend fun deletePlaylistResults(pageToken: String) = playlistDao.delete(pageToken)

    suspend fun deleteAllPlaylistResults() = playlistDao.deleteAll()

    suspend fun addPlaylistVideosResult(playlistId: String, videosResult: YTVideosResult, pageToken: String) {
        playlistVideosDao.insert(YTPlaylistVideosEntity(videosResult.toJson(), playlistId, pageToken))
    }

    suspend fun deletePlaylistVideosResult(playlistId: String) = playlistVideosDao.delete(playlistId)

    suspend fun deletePlaylistVideosResults(playlistId: String, pageToken: String) = playlistVideosDao.delete(playlistId, pageToken)

    suspend fun addVideoInfos(videoInfos: List<YTVideoInfoResult>) {
        val videoInfoEntities = videoInfos.map {videoInfo ->  YTVideoInfoEntity(videoInfo.toJson(), videoInfo.videoId) }
        videoInfoDao.insertAll(videoInfoEntities)
    }

    fun cacheChannelIdAvailability(isAvailable: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_CHANNEL_AVAILABLE, isAvailable)
            commit()
        }
    }

    override suspend fun isChannelIdAvailable() = sharedPreferences.getBoolean(KEY_IS_CHANNEL_AVAILABLE, false)
}