package `in`.ponshere.ytdemoapp.playlistdetails.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YTVideo(
    val videoId: String,
    val title: String,
    val icon: String,
    val author: String,
    val duration: String
) : Parcelable