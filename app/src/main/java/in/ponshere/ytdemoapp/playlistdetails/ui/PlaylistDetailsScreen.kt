package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModel
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModelFactory
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_playlist_details_screen.*
import javax.inject.Inject

private const val KEY_PLAYLIST = "playlist"


class PlaylistDetailsScreen : DaggerAppCompatActivity() {
    private lateinit var playlistDetailsViewModel: PlaylistDetailsViewModel
    private lateinit var playlistDetailsAdapter: PlaylistDetailsAdapter

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    private val playlistVideos = mutableListOf<YTVideo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_details_screen)
        val playlist = intent.getParcelableExtra<YTPlaylist>(KEY_PLAYLIST)

        playlistDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            PlaylistDetailsViewModel::class.java)

        addObservers()
        setupRecyclerView()

        if (playlist != null) {
            tvTitle.text = playlist.title
            tvCount.text = playlist.videosCount.toString()
            Picasso.get().load(playlist.icon).into(imgPlaylistIcon)
            playlistDetailsViewModel.fetchPlaylistVideos(playlist.id)
        }

    }

    private fun addObservers() {
        playlistDetailsViewModel.showProgress().observe(this,
            Observer {

            })

        playlistDetailsViewModel.playlistVideos().observe(this,
            Observer {
                it?.let {
                    playlistVideos.addAll(it)
                    playlistDetailsAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun setupRecyclerView() {
        playlistDetailsAdapter = PlaylistDetailsAdapter(playlistVideos)
        playlistDetailsAdapter.onEndReachedListener = object : PlaylistDetailsAdapter.OnEndReachedListener {
            override fun onRecyclerEndReached(position: Int) {

            }

        }
        playlistVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistDetailsAdapter
        }
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