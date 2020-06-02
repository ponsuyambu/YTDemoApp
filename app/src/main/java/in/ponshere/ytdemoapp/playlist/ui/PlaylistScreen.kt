package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.viewmodels.PlaylistViewModel
import `in`.ponshere.ytdemoapp.playlist.viewmodels.PlaylistViewModelFactory
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_playlist_screen.*


class PlaylistScreen : AppCompatActivity() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistAdapter: PlaylistAdapter
    val playlist = mutableListOf<YTPlaylist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_screen)
        playlistViewModel =
            ViewModelProvider(this, PlaylistViewModelFactory(this)).get(PlaylistViewModel::class.java)

        addObservers()
        setupRecyclerView()

        playlistViewModel.fetchPlaylist()

    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter(playlist)
        playlistAdapter.onEndReachedListener = object : PlaylistAdapter.OnEndReachedListener {
            override fun onRecyclerEndReached(position: Int) {
                playlistViewModel.fetchPlaylist()
            }

        }
        playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
//            layoutManager = GridLayoutManager(this@PlaylistScreen, 2)
            adapter = playlistAdapter
        }
    }

    private fun addObservers() {
        playlistViewModel.showProgress().observe(this,
            Observer {

            })

        playlistViewModel.playlists().observe(this,
            Observer {
                it?.let {
                    playlist.addAll(it)
                    playlistAdapter.notifyDataSetChanged()
                }
            })
    }

    companion object {
        fun launch(caller: Activity) {
            val intent = Intent(caller, PlaylistScreen::class.java)
            caller.startActivity(intent)
        }
    }
}