package `in`.ponshere.ytdemoapp.playlist.repository.models


class YTVideosResult(
    val videos: List<YTVideo>,
    val nextPageToken: String
)