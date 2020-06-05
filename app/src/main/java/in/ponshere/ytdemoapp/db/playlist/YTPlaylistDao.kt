package `in`.ponshere.ytdemoapp.db.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistDao {
    @Query("SELECT * FROM playlist_cache WHERE page_token LIKE :pageToken")
    suspend fun find(pageToken: String): YTPlaylistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistEntity: YTPlaylistEntity)

    @Query("DELETE FROM playlist_cache WHERE page_token LIKE :pageToken")
    suspend fun delete(pageToken: String)

    @Query("DELETE FROM playlist_cache")
    suspend fun deleteAll()
}