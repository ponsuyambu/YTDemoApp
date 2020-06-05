package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.common.models.YTVideo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//Shared across VideoPlayer, PlaylistDetails, VideosFragment
class SharedPlayerViewModel : ViewModel() {
    private val currentVideo = MutableLiveData<YTVideo>()
    private val endedVideo = MutableLiveData<YTVideo>()
    private val playlist = MutableLiveData<String>()

    fun playVideo(video: YTVideo) {
        currentVideo.postValue(video)
    }

    fun playPlaylist(playlistId: String) {
        playlist.postValue(playlistId)
    }

    fun onVideoEnded(video: YTVideo) {
        endedVideo.postValue(video)
    }

    fun currentVideo() : LiveData<YTVideo>  = currentVideo
    fun endedVideo() : LiveData<YTVideo>  = endedVideo
    fun playlist() : LiveData<String>  = playlist
}