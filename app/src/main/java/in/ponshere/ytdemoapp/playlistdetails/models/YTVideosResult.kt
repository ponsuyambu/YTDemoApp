package `in`.ponshere.ytdemoapp.playlistdetails.models

import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist


data class YTPlaylistsResult(
    val playlists: List<YTPlaylist>,
    val nextPageToken: String
)