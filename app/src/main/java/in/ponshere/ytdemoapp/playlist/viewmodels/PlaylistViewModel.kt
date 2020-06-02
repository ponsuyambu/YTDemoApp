package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PlaylistViewModel(val repository: YTRepository) {
    private val showProgress = MutableLiveData<Boolean>(false)

    fun fetchPlaylist() {
        showProgress.postValue(true)
    }

    fun showProgress(): LiveData<Boolean> = showProgress

}