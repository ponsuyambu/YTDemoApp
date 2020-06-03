package `in`.ponshere.ytdemoapp.playlistdetails.viewmodels

import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val playlistVideos = MutableLiveData<List<YTVideo>>()
    private var nextPageToken: String? = null

    fun fetchPlaylistVideos(playListId: String) {
        showProgress.postValue(true)
        viewModelScope.launch {
            val playlistResult = repository.getPlaylistVideos(playListId, nextPageToken)
            nextPageToken = playlistResult.nextPageToken
            playlistVideos.postValue(playlistResult.videos)
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun playlistVideos(): LiveData<List<YTVideo>> = playlistVideos
}