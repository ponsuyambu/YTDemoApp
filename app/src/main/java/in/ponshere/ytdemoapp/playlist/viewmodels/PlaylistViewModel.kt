package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val status = MutableLiveData<String>(null)
    private val playlists = MutableLiveData<List<YTPlaylist>>()
    private var nextPageToken: String? = null

    fun fetchPlaylist() {
        viewModelScope.launch {
            if(repository.isNextPlaylistDataAvailable(nextPageToken)) {
                if(nextPageToken == null)
                    showProgress.postValue(true)
                else
                    status.postValue("Loading more videos...")
                val playlistResult = repository.getPlaylists(nextPageToken)
                nextPageToken = playlistResult.nextPageToken
                playlists.postValue(playlistResult.playlists)
                showProgress.postValue(false)
                status.postValue(null)
            }
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun status(): LiveData<String> = status
    fun playlists(): LiveData<List<YTPlaylist>> = playlists

}