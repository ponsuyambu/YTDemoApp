package `in`.ponshere.ytdemoapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTPlaylistResultDao {
    @Query("SELECT * FROM playlist_cache")
    fun getAll(): List<YTPlaylistResult>

    @Query("SELECT * FROM playlist_cache WHERE page_token LIKE :pageToken")
    fun findByPageToken(pageToken: String): YTPlaylistResult?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: YTPlaylistResult)
}