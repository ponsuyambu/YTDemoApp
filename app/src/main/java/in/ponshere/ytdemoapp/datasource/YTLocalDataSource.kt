package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult
import javax.inject.Inject

class YTLocalDataSource @Inject constructor() : YTDataSource {
    override suspend fun getPlaylists(pageToken: String?): YTPlaylistsResult {
        TODO("Not yet implemented")
    }
}