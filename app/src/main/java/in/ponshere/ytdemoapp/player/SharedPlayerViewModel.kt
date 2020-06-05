package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.common.models.YTVideo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//Shared across VideoPlayer, VideosFragment
class SharedPlayerViewModel : ViewModel() {
    private val currentVideo = MutableLiveData<YTVideo>()
    private val endedVideo = MutableLiveData<YTVideo>()

    fun playVideo(video: YTVideo)  = currentVideo.postValue(video)
    fun onVideoEnded(video: YTVideo) = endedVideo.postValue(video)
    fun currentVideo() : LiveData<YTVideo>  = currentVideo
    fun endedVideo() : LiveData<YTVideo>  = endedVideo
}