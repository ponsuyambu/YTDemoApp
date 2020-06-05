package `in`.ponshere.ytdemoapp.common.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class YTVideo(
    val videoId: String,
    val title: String,
    val icon: String,
    val author: String,
    var duration: String
) : Parcelable, ListModel