package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val playlists = MutableLiveData<List<YTPlaylist>>()
    private var nextPageToken: String? = null

    fun fetchPlaylist() {
        showProgress.postValue(true)
        viewModelScope.launch {
            val playlistResult = repository.getPlaylists(nextPageToken)
            nextPageToken = playlistResult.nextPageToken
            playlists.postValue(playlistResult.playlists)
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun playlists(): LiveData<List<YTPlaylist>> = playlists

}