package `in`.ponshere.ytdemoapp.common.models

import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist


data class YTPlaylistsResult(
        val playlists: List<YTPlaylist>?,
        val nextPageToken: String
)