package `in`.ponshere.ytdemoapp.db.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_cache")
data class YTPlaylistEntity(
    @ColumnInfo(name = "result_response") val resultResponse: String,
    @ColumnInfo(name = "page_token") val pageToken: String,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null
)