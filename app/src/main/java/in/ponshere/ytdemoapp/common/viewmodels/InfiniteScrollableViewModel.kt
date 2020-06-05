package `in`.ponshere.ytdemoapp.common.viewmodels

import `in`.ponshere.ytdemoapp.common.models.ListModel
import `in`.ponshere.ytdemoapp.common.models.ListResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class InfiniteScrollableViewModel<R: ListResult<T>, T: ListModel>(private val repository: YTRepository) : ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val status = MutableLiveData<String>(null)
    private val listModels = MutableLiveData<List<T>>()
    protected var nextPageToken: String? = null

    fun fetch(callback: suspend () -> R) {
        showProgress.postValue(true)
        viewModelScope.launch {
            if (repository.isNextPlaylistDataAvailable(nextPageToken)) {
                if (nextPageToken == null)
                    showProgress.postValue(true)
                else
                    status.postValue("Loading ...")
                val playlistResult = callback()
                nextPageToken = playlistResult.nextPageToken
                listModels.postValue(playlistResult.listModels)
                showProgress.postValue(false)
                status.postValue(null)
            }
        }
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun status(): LiveData<String> = status
    fun listModels(): LiveData<List<T>> = listModels
}