package `in`.ponshere.ytdemoapp.playlistdetails.viewmodels

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy.NETWORK_FIRST
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.common.viewmodels.InfiniteScrollableViewModel
import `in`.ponshere.ytdemoapp.repository.YTRepository

class PlaylistDetailsViewModel(private val repository: YTRepository) : InfiniteScrollableViewModel<YTVideosResult, YTVideo>(repository) {

    fun fetchPlaylistVideos(playListId: String, cacheRetrievalPolicy: CacheRetrievalPolicy = NETWORK_FIRST) {
        fetch {
            repository.getPlaylistVideos(playListId, nextPageToken, cacheRetrievalPolicy)
        }
    }
}