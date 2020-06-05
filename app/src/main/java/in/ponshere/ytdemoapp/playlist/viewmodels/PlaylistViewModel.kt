package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.common.viewmodels.InfiniteScrollableViewModel
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PlaylistViewModel(private val repository: YTRepository) : InfiniteScrollableViewModel<YTPlaylistsResult, YTPlaylist>(repository) {

    private val channelIdNotAvailableError = MutableLiveData<Boolean>(false)

    fun fetchPlaylist() {
        viewModelScope.launch {
            if(repository.isChannelIdAvailable()) {
                fetch {
                    repository.getPlaylists(nextPageToken)
                }
            } else {
                channelIdNotAvailableError.postValue(true)
            }
        }
    }

    fun channelIdNotAvailableError() : LiveData<Boolean> = channelIdNotAvailableError
}