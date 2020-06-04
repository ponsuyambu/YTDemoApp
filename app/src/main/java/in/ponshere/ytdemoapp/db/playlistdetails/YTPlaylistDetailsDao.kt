package `in`.ponshere.ytdemoapp.db.playlistdetails

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistDetailsDao {
    @Query("SELECT * FROM playlist_details_cache")
    suspend fun getAll(): List<YTPlaylistDetailsEntity>

    @Query("SELECT * FROM playlist_details_cache WHERE page_token LIKE :pageToken")
    suspend fun findByPageToken(pageToken: String): YTPlaylistDetailsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: YTPlaylistDetailsEntity)

    @Query("DELETE FROM playlist_details_cache")
    suspend fun deleteAll()
}