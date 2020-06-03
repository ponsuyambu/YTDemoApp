package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.playlist.repository.GOOGLE_SIGN_IN_YOUTUBE_SCOPE
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTVideo
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class YTRemoteDataSource @Inject constructor(context: Context) : YTDataSource {
    private val youTube : YouTube

    init {
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            Collections.singleton(GOOGLE_SIGN_IN_YOUTUBE_SCOPE)
        )
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account
        youTube = YouTube.Builder(
            NetHttpTransport(),
            JacksonFactory(),
            credential
        ).setApplicationName("YTDemoApp").build()
    }

    suspend fun getVideos(playlistId: String, pageToken: String? = null) {
        val videos = arrayListOf<YTVideo>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlistItems()?.list("snippet, contentDetails")
                task?.maxResults = 25
                pageToken?.let { task?.pageToken = it }

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlistItem ->
                    val title = playlistItem?.snippet?.title
                    val icon = playlistItem?.snippet?.thumbnails?.high
                    val videoId = playlistItem?.snippet?.resourceId?.videoId
                }
            }
        }

    }

    override suspend fun getPlaylists(pageToken: String?) : YTPlaylistsResult {
        val playLists = arrayListOf<YTPlaylist>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlists()?.list("snippet,contentDetails")
                task?.mine = true
                task?.maxResults = 25
                pageToken?.let { task?.pageToken = it }

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlist ->
                    val title = playlist?.snippet?.title
                    val icon = playlist?.snippet?.thumbnails?.high?.url
                    val videosCount = playlist?.contentDetails?.itemCount
                    //Make sure all the data are available, only then add to the list
                    if(title != null && icon != null && videosCount != null) {
                        playLists.add(YTPlaylist(
                            title,
                            videosCount,
                            icon
                        ))
                    }
                }
            }
        }
        return YTPlaylistsResult(playLists, nextPageToken)
    }
}