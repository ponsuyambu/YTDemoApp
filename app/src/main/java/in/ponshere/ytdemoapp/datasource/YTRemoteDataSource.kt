package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.playlist.models.YTVideoInfoResult
import `in`.ponshere.ytdemoapp.repository.GOOGLE_SIGN_IN_YOUTUBE_SCOPE
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
    private val youTube: YouTube

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

    override suspend fun getPlaylists(pageToken: String): YTPlaylistsResult {
        val playLists = arrayListOf<YTPlaylist>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlists()?.list("snippet,contentDetails")
                task?.mine = true
                task?.maxResults = 15
                task?.pageToken = pageToken

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlist ->
                    val title = playlist?.snippet?.title
                    val icon = playlist?.snippet?.thumbnails?.high?.url
                    val videosCount = playlist?.contentDetails?.itemCount
                    val playlistId = playlist?.id
                    //Make sure all the data are available, only then add to the list
                    if (playlistId != null && title != null && icon != null && videosCount != null) {
                        playLists.add(YTPlaylist(
                            playlistId,
                            title,
                            videosCount,
                            icon
                        ))
                    }
                }
            }
        }
        return YTPlaylistsResult(
            playLists,
            nextPageToken
        )
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult {
        val videos = arrayListOf<YTVideo>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlistItems()?.list("snippet")
                task?.playlistId = playlistId
                task?.maxResults = 15
                task?.pageToken = pageToken

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlistItem ->
                    val videoId = playlistItem?.snippet?.resourceId?.videoId
                    val title = playlistItem?.snippet?.title
                    val author = playlistItem?.snippet?.channelTitle
                    val icon = playlistItem?.snippet?.thumbnails?.high?.url
                    if (videoId != null && title != null && icon != null && author != null) {
                        videos.add(YTVideo(videoId, title, icon, author, ""))
                    }
                }
            }
        }
        return YTVideosResult(videos, nextPageToken)
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String?): YTVideosResult {
        val videos = arrayListOf<YTVideo>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.search()?.list("snippet")
                task?.q = searchTerm
                task?.maxResults = 15
                pageToken?.let { task?.pageToken = it }

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlistItem ->
                    val videoId = playlistItem?.id?.videoId
                    val title = playlistItem?.snippet?.title
                    val icon = playlistItem?.snippet?.thumbnails?.high?.url
                    val author = playlistItem?.snippet?.channelTitle
                    if (videoId != null && title != null && icon != null && author != null) {
                        videos.add(YTVideo(videoId, title, icon, author, ""))
                    }
                }
            }
        }
        return YTVideosResult(videos, nextPageToken)
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String) = pageToken.isEmpty().not()

    private fun isNextResultsAvailable(pageToken: String?): Boolean {
        if (pageToken == null) return true
        if (pageToken.isEmpty()) return false
        return true
    }

    override suspend fun getVideosInfo(videoIds: List<String>): Map<String, String> {
        TODO("Not yet implemented")
    }

     suspend fun getVideosInfo(vararg videoIds: String): List<YTVideoInfoResult> {
        val videoInfoResultList = mutableListOf<YTVideoInfoResult>()
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.videos()?.list("contentDetails")
                task?.id = videoIds.toList().joinToString(separator = ",")
                task?.maxResults = videoIds.size.toLong()
                val result = task?.execute()

                result?.items?.forEach { video ->
                    val duration = video.contentDetails?.duration
                    if (duration != null) {
                        videoInfoResultList.add( YTVideoInfoResult(video.id, duration))

                    }
                }
            }
        }
        return videoInfoResultList
    }
}