package `in`.ponshere.ytdemoapp.playlist.models

import `in`.ponshere.ytdemoapp.common.models.ListResult

class YTPlaylistsResult(
    override val listModels: List<YTPlaylist>?,
    override val nextPageToken: String
) : ListResult<YTPlaylist>