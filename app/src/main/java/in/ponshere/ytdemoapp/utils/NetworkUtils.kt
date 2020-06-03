package `in`.ponshere.ytdemoapp.utils

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkUtils @Inject constructor(val context: Context) {
    fun isOnline(): Boolean {
        return false
    }

}