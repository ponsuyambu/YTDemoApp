package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData<Boolean>(false)

    fun fetchPlaylist() {
        showProgress.postValue(true)
        viewModelScope.launch {
            repository.getPlaylists()
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress

}