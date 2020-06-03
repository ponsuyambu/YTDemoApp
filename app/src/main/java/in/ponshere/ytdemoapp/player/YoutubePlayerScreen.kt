package `in`.ponshere.ytdemoapp.player

import `in`.ponshere.ytdemoapp.R
import android.os.Bundle
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_youtube_player_screen.*


class YoutubePlayerScreen : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player_screen)
        lifecycle.addObserver(youtube_player_view)
        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "bK3qQU0r8x0"
                youTubePlayer.loadVideo(videoId, 23f)
            }
        })

    }
}