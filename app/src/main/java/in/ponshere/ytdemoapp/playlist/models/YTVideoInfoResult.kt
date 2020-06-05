package `in`.ponshere.ytdemoapp.playlist.models

const val NO_DURATION = "!NO_DURATION!"
open class YTVideoInfoResult(val videoId: String, val duration: String)
class EmptyDurationVideoInfoResult(videoId: String) : YTVideoInfoResult(videoId, NO_DURATION)