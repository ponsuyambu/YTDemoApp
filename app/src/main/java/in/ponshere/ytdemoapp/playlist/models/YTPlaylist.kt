package `in`.ponshere.ytdemoapp.playlist.models

import `in`.ponshere.ytdemoapp.common.models.ListModel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YTPlaylist(
    val id: String,
    val title: String,
    val videosCount: Long,
    val icon: String
) : Parcelable, ListModel