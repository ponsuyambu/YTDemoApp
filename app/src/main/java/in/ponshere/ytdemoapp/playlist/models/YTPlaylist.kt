package `in`.ponshere.ytdemoapp.playlist.models

import `in`.ponshere.ytdemoapp.common.models.ListModel
import android.os.Parcelable
import com.google.api.services.youtube.model.Playlist
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YTPlaylist(
    val id: String,
    val title: String?,
    val videosCount: Long,
    val icon: String?
) : Parcelable, ListModel {
    constructor(playlist: Playlist) : this(
        playlist.id,
        playlist.snippet?.title,
        playlist.contentDetails?.itemCount ?: 0,
        playlist.snippet?.thumbnails?.high?.url
    )
}