package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.ViewModelFactory
import `in`.ponshere.ytdemoapp.common.ui.BaseActivity
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.viewmodels.PlaylistViewModel
import `in`.ponshere.ytdemoapp.search.ui.SearchScreen
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_infinite_scrollable_list.*
import javax.inject.Inject


class PlaylistScreen : BaseActivity() {

    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistAdapter: PlaylistAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var picasso: Picasso

    val playlist = mutableListOf<YTPlaylist>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_infinite_scrollable_list)
        playlistViewModel =
            ViewModelProvider(this, viewModelFactory).get(PlaylistViewModel::class.java)

        addObservers()
        setupRecyclerView()

        playlistViewModel.fetchPlaylist()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.playlist_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.searchOption) {
            SearchScreen.launch(this)
        }
        return true
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter(playlist, picasso)
        playlistAdapter.onEndReachedListener = object : PlaylistAdapter.OnEndReachedListener {
            override fun onRecyclerEndReached(position: Int) {
                playlistViewModel.fetchPlaylist()
            }

        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }
    }

    private fun addObservers() {
        playlistViewModel.showProgress().observe(this,
            Observer {
                it?.let {
                    if(it) {
                        progressBar.visibility = View.VISIBLE
                    } else {
                        progressBar.visibility = View.GONE
                    }
                }

            })

        playlistViewModel.status().observe(this, Observer {
            if(it == null) {
                tvStatus.visibility = View.GONE
            } else {
                tvStatus.text = it
                tvStatus.visibility = View.VISIBLE
            }
        })

        playlistViewModel.listModels().observe(this,
            Observer {
                it?.let {
                    playlist.addAll(it)
                    playlistAdapter.notifyDataSetChanged()
                }
            })

        playlistViewModel.channelIdNotAvailableError().observe(this, Observer {
            it?.let {
                if(it) tvError.visibility = View.VISIBLE else tvError.visibility = View.GONE
            }
        })
    }

    companion object {
        fun launch(caller: Activity) {
            val intent = Intent(caller, PlaylistScreen::class.java)
            caller.startActivity(intent)
        }
    }

    override fun isRoot(): Boolean {
        return true
    }
}