package `in`.ponshere.ytdemoapp.playlistdetails

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_playlist_details_screen.*

private const val KEY_PLAYLIST = "playlist"


class PlaylistDetailsScreen : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details_screen)
        val playlist = intent.getParcelableExtra<YTPlaylist>(KEY_PLAYLIST)

        if(playlist != null) {
            Picasso.get().load(playlist.icon).into(imgPlaylistIcon)
        }

    }

    companion object {
        fun launch(caller : Activity, playlist: YTPlaylist) {
            val intent = Intent(caller, PlaylistDetailsScreen::class.java).apply {
                putExtra(KEY_PLAYLIST, playlist)
            }
            caller.startActivity(intent)
        }
    }
}