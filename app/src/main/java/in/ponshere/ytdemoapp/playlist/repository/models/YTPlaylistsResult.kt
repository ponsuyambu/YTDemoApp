package `in`.ponshere.ytdemoapp.playlist.repository.models

import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo


class YTVideosResult(
    val videos: List<YTVideo>,
    val nextPageToken: String
)