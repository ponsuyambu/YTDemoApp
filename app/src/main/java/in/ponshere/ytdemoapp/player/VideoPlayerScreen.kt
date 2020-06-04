package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistVideosFragment
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_youtube_player_screen.*

private const val KEY_PLAYLIST = "playlist"
private const val KEY_PLAYLISTVIDEO = "playlistVideo"

class VideoPlayerScreen : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player_screen)
        val playlistVideo = intent.getParcelableExtra<YTVideo>(KEY_PLAYLISTVIDEO)
        lifecycle.addObserver(youtube_player_view)

        if (playlistVideo != null) {
            youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(playlistVideo.videoId, 0f)
                }
            })
        } else {
            val playlist = intent.getParcelableExtra<YTPlaylist>(KEY_PLAYLIST)
            playlist?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.flContainer, PlaylistVideosFragment.newInstance(it.id))
                    .commit()
            }
        }
    }

    companion object {
        fun launch(caller: Activity, playlistVideo: YTVideo) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLISTVIDEO, playlistVideo)
            }
            caller.startActivity(intent)
        }

        fun launch(caller: Activity, playlist: YTPlaylist) {
            val intent = Intent(caller, VideoPlayerScreen::class.java).apply {
                putExtra(KEY_PLAYLIST, playlist)
            }
            caller.startActivity(intent)
        }
    }
}
