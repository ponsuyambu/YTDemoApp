package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.player.SharedPlayerViewModel
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_playlist_details_screen.*

private const val KEY_PLAYLIST = "playlist"


class PlaylistDetailsScreen : DaggerAppCompatActivity() {
    private var playlist: YTPlaylist? = null

    private val sharedPlayerViewModel: SharedPlayerViewModel by lazy {
        ViewModelProvider(this).get(SharedPlayerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details_screen)
        playlist = intent.getParcelableExtra(KEY_PLAYLIST)

        playlist?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, PlaylistVideosFragment.newInstance(it.id))
                .commit()
            tvTitle.text = it.title
            tvCount.text = it.videosCount.toString()
            Picasso.get().load(it.icon).into(imgPlaylistIcon)
        }

        sharedPlayerViewModel.currentVideo().observe(this, Observer {
            VideoPlayerScreen.launch(this, it)
        })
    }

    companion object {
        fun launch(caller: Activity, playlist: YTPlaylist) {
            val intent = Intent(caller, PlaylistDetailsScreen::class.java).apply {
                putExtra(KEY_PLAYLIST, playlist)
            }
            caller.startActivity(intent)
        }
    }
}