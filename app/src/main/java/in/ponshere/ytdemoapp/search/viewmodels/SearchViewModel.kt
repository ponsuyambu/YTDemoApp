package `in`.ponshere.ytdemoapp.search.viewmodels

import `in`.ponshere.ytdemoapp.common.viewmodels.InfiniteScrollableViewModel
import `in`.ponshere.ytdemoapp.repository.YTRepository

class SearchViewModel(private val repository: YTRepository) : InfiniteScrollableViewModel(repository) {
    fun fetchVideosFor(searchTerm: String) {
        fetch {
            repository.getVideosFor(searchTerm, nextPageToken)
        }
    }
}