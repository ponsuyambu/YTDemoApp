package `in`.ponshere.ytdemoapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistResultDao {
    @Query("SELECT * FROM playlist_cache")
    suspend fun getAll(): List<YTPlaylistEntity>

    @Query("SELECT * FROM playlist_cache WHERE page_token LIKE :pageToken")
    suspend fun findByPageToken(pageToken: String): YTPlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: YTPlaylistEntity)

    @Query("DELETE FROM playlist_cache")
    suspend fun deleteAll()

}