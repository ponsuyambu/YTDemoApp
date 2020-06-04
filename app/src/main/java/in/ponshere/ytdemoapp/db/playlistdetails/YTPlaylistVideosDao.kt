package `in`.ponshere.ytdemoapp.db.playlistdetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistVideosDao {
    @Query("SELECT * FROM playlist_videos_cache")
    suspend fun getAll(): List<YTPlaylistVideosEntity>

    @Query("SELECT * FROM playlist_videos_cache WHERE page_token LIKE :pageToken AND playlist_id LIKE :playlistId")
    suspend fun findByPageToken(playlistId: String, pageToken: String): YTPlaylistVideosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: YTPlaylistVideosEntity)

    @Query("DELETE FROM playlist_videos_cache WHERE playlist_id LIKE :playlistId")
    suspend fun deleteAll(playlistId: String)
}