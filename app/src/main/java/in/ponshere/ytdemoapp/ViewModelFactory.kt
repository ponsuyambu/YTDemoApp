package `in`.ponshere.ytdemoapp

import `in`.ponshere.ytdemoapp.repository.YTRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(val repository: YTRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(YTRepository::class.java).newInstance(repository)
    }
}