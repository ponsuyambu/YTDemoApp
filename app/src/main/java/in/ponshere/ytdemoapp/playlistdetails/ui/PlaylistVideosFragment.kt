package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModel
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModelFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_playlist_videos.*
import javax.inject.Inject

private const val KEY_PLAYLIST_ID = "playlistId"

class PlaylistVideosFragment : DaggerFragment() {
    private lateinit var playlistDetailsViewModel: PlaylistDetailsViewModel
    private lateinit var playlistDetailsAdapter: PlaylistDetailsAdapter

    @Inject
    lateinit var viewModelFactory: PlaylistDetailsViewModelFactory

    private val playlistVideos = mutableListOf<YTVideo>()

    companion object {
        fun newInstance(playlistId: String): PlaylistVideosFragment {
            return PlaylistVideosFragment()
                .apply {
                    val bundle = Bundle().apply {
                        putString(KEY_PLAYLIST_ID, playlistId)
                    }
                    arguments = bundle
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlistDetailsViewModel = ViewModelProvider(this, viewModelFactory).get(
            PlaylistDetailsViewModel::class.java
        )
        addObservers()
        val playlistId = arguments?.getString(KEY_PLAYLIST_ID)
        playlistId?.let {
            playlistDetailsViewModel.fetchPlaylistVideos(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
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
        playlistDetailsAdapter.onEndReachedListener =
            object : PlaylistDetailsAdapter.OnEndReachedListener {
                override fun onRecyclerEndReached(position: Int) {

                }

            }
        playlistVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistDetailsAdapter
        }
    }
}