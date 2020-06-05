package `in`.ponshere.ytdemoapp.db.videos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface YTVideoInfoDao {
    @Query("SELECT * FROM video_info_cache")
    suspend fun getAll(): List<YTVideoInfoEntity>

    @Query("SELECT * FROM video_info_cache WHERE video_id = :videoId")
    suspend fun find(videoId: String): YTVideoInfoEntity?

    @Query("SELECT * FROM video_info_cache WHERE video_id IN(:videoIds)")
    suspend fun find(videoIds: List<String>): List<YTVideoInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(videoInfoEntity: YTVideoInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videoInfoEntity: List<YTVideoInfoEntity>)
}