package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.ViewModelFactory
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModel
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

private const val KEY_PLAYLIST_ID = "playlistId"

class PlaylistVideosFragment: VideosFragment() {
    private lateinit var playlistDetailsViewModel: PlaylistDetailsViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun newInstance(playlistId: String): VideosFragment {
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
            playlistDetailsViewModel.fetchPlaylistVideos(it, getCacheRetrievalPolicy())
        }
    }

    private fun getCacheRetrievalPolicy(): CacheRetrievalPolicy {
        if(activity is VideoPlayerScreen) {
            return CacheRetrievalPolicy.CACHE_FIRST
        }
        return CacheRetrievalPolicy.NETWORK_FIRST
    }

    private fun addObservers() {
        playlistDetailsViewModel.showProgress().observe(this,
            Observer {

            })

        playlistDetailsViewModel.playlistVideos().observe(this,
            Observer {
                it?.let {
                    videos.addAll(it)
                    videosAdapter.notifyDataSetChanged()
                }
            })
    }
}