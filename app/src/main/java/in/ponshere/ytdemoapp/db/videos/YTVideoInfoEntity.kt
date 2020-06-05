package `in`.ponshere.ytdemoapp.db.videos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_info_cache")
data class YTVideoInfoEntity(
        @ColumnInfo(name = "video_response") val resultResponse: String,
        @ColumnInfo(name = "video_id") val videoId: String,
        @PrimaryKey(autoGenerate = true) val uid: Int? = null
)