package `in`.ponshere.ytdemoapp.search.viewmodels

import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val searchVideos = MutableLiveData<List<YTVideo>>()
    private var nextPageToken: String? = null

    fun fetchVideosFor(searchTerm: String) {
        showProgress.postValue(true)
        viewModelScope.launch {
            val playlistResult = repository.getVideosFor(searchTerm, nextPageToken)
            nextPageToken = playlistResult.nextPageToken
            searchVideos.postValue(playlistResult.videos)
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun searchVideos(): LiveData<List<YTVideo>> = searchVideos
}