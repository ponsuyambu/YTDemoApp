package `in`.ponshere.ytdemoapp.common.viewmodels

import `in`.ponshere.ytdemoapp.common.models.ListModel
import `in`.ponshere.ytdemoapp.common.models.ListResult
import `in`.ponshere.ytdemoapp.datasource.FIRST_PAGE_TOKEN
import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class InfiniteScrollableViewModel<R : ListResult<T>, T : ListModel>(private val repository: YTRepository) :
    ViewModel() {
    private val showProgress = MutableLiveData(false)
    private val status = MutableLiveData<String>(null)
    private val listModels = MutableLiveData<List<T>>()
    protected var nextPageToken: String = FIRST_PAGE_TOKEN

    private val showPlayAll: MutableLiveData<Boolean> = MutableLiveData(false)

    fun fetch(callback: suspend () -> R?) {
        viewModelScope.launch {
            if (repository.isNextPlaylistDataAvailable(nextPageToken)) {
                setProgress(true)
                val playlistResult = callback()
                playlistResult?.let {
                    listModels.postValue(it.listModels)
                    nextPageToken = it.nextPageToken
                    showPlayAll.postValue(it.listModels?.isNotEmpty() ?: false)
                }
                setProgress(false)
            }
        }
    }

    private fun setProgress(loading: Boolean) {
        showProgress.postValue(loading && nextPageToken == FIRST_PAGE_TOKEN)
        status.postValue(if (loading) "Loading ..." else null)
    }

    fun showProgress(): LiveData<Boolean> = showProgress
    fun status(): LiveData<String> = status
    fun listModels(): LiveData<List<T>> = listModels
    fun showPlayAll(): LiveData<Boolean> = showPlayAll
}