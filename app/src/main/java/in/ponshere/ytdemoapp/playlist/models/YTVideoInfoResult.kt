package `in`.ponshere.ytdemoapp.playlist.models

open class YTVideoInfoResult constructor(
        val videoId: String,
        _duration: String?
) {
    val duration = _duration?.let { Regex("[a-zA-Z]").replace(_duration, " ").trim().replace(" ", ":") }
}