package `in`.ponshere.ytdemoapp.playlist.models

import `in`.ponshere.ytdemoapp.common.models.YTVideo


class YTVideosResult(
    val videos: List<YTVideo>?,
    val nextPageToken: String
)