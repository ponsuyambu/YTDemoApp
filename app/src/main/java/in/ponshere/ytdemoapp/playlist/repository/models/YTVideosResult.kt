package `in`.ponshere.ytdemoapp.playlist.repository.models


data class YTPlaylistsResult(
    val playlists: List<YTPlaylist>,
    val nextPageToken: String
)