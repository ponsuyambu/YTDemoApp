package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.common.viewmodels.InfiniteScrollableViewModel
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.repository.YTRepository

class PlaylistViewModel(private val repository: YTRepository) : InfiniteScrollableViewModel<YTPlaylistsResult, YTPlaylist>(repository) {

    fun fetchPlaylist() {
        fetch {
            repository.getPlaylists(nextPageToken)
        }
    }
}