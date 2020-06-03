package `in`.ponshere.ytdemoapp.playlistdetails.viewmodels

import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistDetailsViewModelFactory @Inject constructor(val repository: YTRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaylistDetailsViewModel(repository) as T
    }
}