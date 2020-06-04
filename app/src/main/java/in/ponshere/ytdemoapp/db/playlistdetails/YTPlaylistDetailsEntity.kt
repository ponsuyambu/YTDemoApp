package `in`.ponshere.ytdemoapp.db.playlistdetails

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_details_cache")
data class YTPlaylistDetailsEntity(
    @ColumnInfo(name = "result_response") val resultResponse: String,
    @ColumnInfo(name = "page_token") val pageToken: String,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null
)