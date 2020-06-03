package `in`.ponshere.ytdemoapp.playlist.repository.models


class YTPlaylistsResult(
    val playlists: List<YTPlaylist>,
    val nextPageToken: String
)