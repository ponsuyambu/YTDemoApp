package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistVideosFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_youtube_player_screen.*

private const val KEY_PLAYLIST = "playlist"
private const val KEY_PLAYLIST_ID = "playlist_id"
private const val KEY_PLAYLISTVIDEO = "playlistVideo"

class VideoPlayerScreen : DaggerAppCompatActivity() {
    private lateinit var player : YouTubePlayer
    private var currentVideo : YTVideo? = null
    private val sharedPlayerViewModel: SharedPlayerViewModel by lazy {
        ViewModelProvider(this).get(SharedPlayerViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player_screen)
        currentVideo = intent.getParcelableExtra(KEY_PLAYLISTVIDEO)
        val playlistId = intent.getStringExtra(KEY_PLAYLIST_ID)

        lifecycle.addObserver(youtubePlayerView)
        sharedPlayerViewModel.currentVideo().observe(this, Observer {
            currentVideo = it
            player.loadVideo(it.videoId, 0f)
        })
        currentVideo?.let {
            playInitialVideo(it)

            if(playlistId != null) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.flContainer, PlaylistVideosFragment.newInstance(playlistId))
                        .commit()
            }
        }
    }

    private fun playInitialVideo(video : YTVideo) {
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                player = youTubePlayer
                youTubePlayer.loadVideo(video.videoId, 0f)
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

    companion object {
        fun launch(caller: Activity?, playlistVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLISTVIDEO, playlistVideo)
            }
            caller?.startActivity(intent)
        }

        fun launch(caller: Activity?, playlistId: String, initialVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLISTVIDEO, initialVideo)
                putExtra(KEY_PLAYLIST_ID, playlistId)
            }
            caller?.startActivity(intent)
        }
    }
}
