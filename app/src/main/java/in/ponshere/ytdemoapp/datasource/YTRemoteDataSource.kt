package `in`.ponshere.ytdemoapp.datasource

import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.network.CacheRetrievalPolicy
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

private const val MAX_RESULTS = 15L

class YTRemoteDataSource @Inject constructor(context: Context) : YTDataSource {
    private val youTube: YouTube

    init {
        val credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(GOOGLE_SIGN_IN_YOUTUBE_SCOPE))
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account
        youTube = YouTube.Builder(NetHttpTransport(), JacksonFactory(), credential).setApplicationName("YTDemoApp").build()
    }

     override suspend fun isChannelIdAvailable() : Boolean {
         var hasChannel = false
         coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.channels()?.list("snippet")?.apply {
                    mine = true
                }
                val result = task?.execute()

                if (result?.items?.size ?: 0 > 0) {
                    hasChannel = true
                }
            }
         }
         return hasChannel
    }

    override suspend fun getPlaylists(pageToken: String): YTPlaylistsResult? {
        val playLists = arrayListOf<YTPlaylist>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlists()?.list("snippet,contentDetails")
                task?.mine = true
                task?.maxResults = MAX_RESULTS
                task?.pageToken = pageToken
                task?.userIp

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playLists.add(YTPlaylist(it)) }
            }
        }
        return YTPlaylistsResult(playLists, nextPageToken)
    }

    override suspend fun getPlaylistVideos(playlistId: String, pageToken: String, cacheRetrievalPolicy: CacheRetrievalPolicy): YTVideosResult? {
        val videos = arrayListOf<YTVideo>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.playlistItems()?.list("snippet")
                task?.playlistId = playlistId
                task?.maxResults = MAX_RESULTS
                task?.pageToken = pageToken

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlistItem -> videos.add(YTVideo(playlistItem)) }
            }
        }
        return YTVideosResult(videos, nextPageToken)
    }

    override suspend fun getVideosFor(searchTerm: String, pageToken: String): YTVideosResult {
        val videos = arrayListOf<YTVideo>()
        var nextPageToken = ""
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.search()?.list("snippet")
                task?.q = searchTerm
                task?.maxResults = MAX_RESULTS
                pageToken?.let { task?.pageToken = it }

                val result = task?.execute()

                nextPageToken = result?.nextPageToken ?: ""
                result?.items?.forEach { playlistItem -> if(playlistItem.id.videoId != null) videos.add(YTVideo(playlistItem)) }
            }
        }
        return YTVideosResult(videos, nextPageToken)
    }

    override suspend fun isNextPlaylistDataAvailable(pageToken: String) = pageToken.isEmpty().not()

    override suspend fun getVideosInfo(videoIds: List<String>): Map<String, YTVideoInfoResult> {
        val videoInfoResultList = mutableMapOf<String, YTVideoInfoResult>()
        coroutineScope {
            withContext(Dispatchers.IO) {
                val task = youTube.videos()?.list("contentDetails")
                task?.id = videoIds.joinToString(",")
                val result = task?.execute()
                result?.items?.forEach { video -> videoInfoResultList[video.id] = YTVideoInfoResult(video) }
            }
        }
        return videoInfoResultList
    }

}