package `in`.ponshere.ytdemoapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_cache")
data class YTPlaylistResult(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "result_response") val resultResponse: String?,
    @ColumnInfo(name = "page_token") val pageToken: String?
)