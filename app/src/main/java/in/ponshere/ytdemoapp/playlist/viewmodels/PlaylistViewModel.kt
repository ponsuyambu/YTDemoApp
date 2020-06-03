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
    private val playlists = MutableLiveData<List<YTPlaylist>>()
    private var nextPageToken: String? = null

    fun fetchPlaylist() {
        viewModelScope.launch {
            if(isNextPlaylistResultsAvailable(nextPageToken)) {
                showProgress.postValue(true)
                val playlistResult = repository.getPlaylists(nextPageToken)
                nextPageToken = playlistResult.nextPageToken
                playlists.postValue(playlistResult.playlists)
            }
        }
    }

    private fun isNextPlaylistResultsAvailable(pageToken : String?) : Boolean{
        if(pageToken == null) return true
        if(pageToken.isEmpty()) return false
        return true
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun playlists(): LiveData<List<YTPlaylist>> = playlists

}