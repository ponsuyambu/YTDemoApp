package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.ui.BaseActivity
import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistVideosFragment
import `in`.ponshere.ytdemoapp.search.ui.SearchVideosFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_youtube_player_screen.*

private const val KEY_PLAYLIST_ID = "playlist_id"
private const val KEY_PLAYLIST_VIDEO = "playlistVideo"
private const val KEY_QUERY_TERM = "queryTerm"


class VideoPlayerScreen : BaseActivity() {
    private lateinit var player : YouTubePlayer
    private var currentVideo : YTVideo? = null
    private val sharedPlayerViewModel: SharedPlayerViewModel by lazy {
        ViewModelProvider(this).get(SharedPlayerViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player_screen)
        currentVideo = intent.getParcelableExtra(KEY_PLAYLIST_VIDEO)
        val playlistId = intent.getStringExtra(KEY_PLAYLIST_ID)
        val searchQuery = intent.getStringExtra(KEY_QUERY_TERM)

        lifecycle.addObserver(youtubePlayerView)
        sharedPlayerViewModel.currentVideo().observe(this, Observer {
            currentVideo = it
            playVideo(it)
        })
        currentVideo?.let {
            // In offline, youtube player will not ready, hence set here as well
            tvVideoTitle.text = it.title
            title = it.title
            playInitialVideo(it)

            if(playlistId != null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, PlaylistVideosFragment.newInstance(playlistId))
                        .commit()
            }
            if(searchQuery != null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, SearchVideosFragment.newInstance(searchQuery))
                        .commit()
            }
        }
    }

    private fun playInitialVideo(video : YTVideo) {
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                player = youTubePlayer
                playVideo(video)
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if(state == PlayerConstants.PlayerState.ENDED) {
                    currentVideo?.let {
                        sharedPlayerViewModel.onVideoEnded(it)
                    }
                }
            }
        })
    }

    private fun playVideo(video: YTVideo) {
        tvVideoTitle.text = video.title
        title = video.title
        player.loadVideo(video.videoId, 0f)
    }

    companion object {
        fun launch(caller: Activity?, playlistVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLIST_VIDEO, playlistVideo)
            }
            caller?.startActivity(intent)
        }

        fun launch(caller: Activity?, playlistId: String, initialVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLIST_VIDEO, initialVideo)
                putExtra(KEY_PLAYLIST_ID, playlistId)
            }
            caller?.startActivity(intent)
        }

        fun launchWithSearchQuery(caller: Activity?, searchQuery: String, initialVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLIST_VIDEO, initialVideo)
                putExtra(KEY_QUERY_TERM, searchQuery)
            }
            caller?.startActivity(intent)
        }
    }
}
