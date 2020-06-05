package `in`.ponshere.ytdemoapp.common.viewmodels

import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class InfiniteScrollableViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val status = MutableLiveData<String>(null)
    private val videos = MutableLiveData<List<YTVideo>>()
    protected var nextPageToken: String? = null

    fun fetch(callback: suspend () -> YTVideosResult) {
        showProgress.postValue(true)
        viewModelScope.launch {
            if (repository.isNextPlaylistDataAvailable(nextPageToken)) {
                if (nextPageToken == null)
                    showProgress.postValue(true)
                else
                    status.postValue("Loading more videos...")
                val playlistResult = callback()
                nextPageToken = playlistResult.nextPageToken
                videos.postValue(playlistResult.videos)
                showProgress.postValue(false)
                status.postValue(null)
            }
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun status(): LiveData<String> = status
    fun videos(): LiveData<List<YTVideo>> = videos
}