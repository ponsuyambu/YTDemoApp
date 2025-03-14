package `in`.ponshere.ytdemoapp.db.playlistdetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistVideosDao {
    @Query("SELECT * FROM playlist_videos_cache WHERE page_token = :pageToken AND playlist_id = :playlistId")
    suspend fun find(playlistId: String, pageToken: String): YTPlaylistVideosEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: YTPlaylistVideosEntity)

    @Query("DELETE FROM playlist_videos_cache WHERE playlist_id = :playlistId")
    suspend fun delete(playlistId: String)

    @Query("DELETE FROM playlist_videos_cache WHERE playlist_id = :playlistId AND page_token = :pageToken")
    suspend fun delete(playlistId: String, pageToken: String)
}