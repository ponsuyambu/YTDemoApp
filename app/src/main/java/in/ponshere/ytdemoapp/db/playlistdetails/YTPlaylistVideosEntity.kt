package `in`.ponshere.ytdemoapp.db.playlistdetails

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_videos_cache")
data class YTPlaylistVideosEntity(
    @ColumnInfo(name = "result_response") val resultResponse: String,
    @ColumnInfo(name = "playlist_id") val playlistId: String,
    @ColumnInfo(name = "page_token") val pageToken: String,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null
)