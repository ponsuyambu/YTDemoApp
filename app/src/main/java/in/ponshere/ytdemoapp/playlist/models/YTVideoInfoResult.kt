package `in`.ponshere.ytdemoapp.playlist.models

import com.google.api.services.youtube.model.Video

open class YTVideoInfoResult constructor(
        val videoId: String,
        _duration: String?
) {
    val duration = _duration?.let { Regex("[a-zA-Z]").replace(_duration, " ").trim().replace(" ", ":") }
    constructor(video: Video) : this(video.id, video.contentDetails?.duration)
}