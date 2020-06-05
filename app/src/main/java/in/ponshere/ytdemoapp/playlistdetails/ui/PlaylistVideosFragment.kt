package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.common.ui.VideosFragment
import `in`.ponshere.ytdemoapp.network.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlistdetails.viewmodels.PlaylistDetailsViewModel
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.layout_infinite_scrollable_list.*

private const val KEY_PLAYLIST_ID = "playlistId"

class PlaylistVideosFragment() :
    VideosFragment<PlaylistDetailsViewModel>(PlaylistDetailsViewModel::class.java) {

    companion object {
        fun newInstance(playlistId: String): VideosFragment<PlaylistDetailsViewModel> {
            return PlaylistVideosFragment()
                .apply {
                    val bundle = Bundle().apply {
                        putString(KEY_PLAYLIST_ID, playlistId)
                    }
                    arguments = bundle
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabPlayAll.setOnClickListener {
            val videos = viewModel.listModels().value
            val playlistId = arguments?.getString(KEY_PLAYLIST_ID)
            if(videos?.get(0) != null && playlistId != null) {
                VideoPlayerScreen.launch(activity, playlistId, videos[0])
            }
        }
    }

    override fun getVideos() {
        val playlistId = arguments?.getString(KEY_PLAYLIST_ID)
        playlistId?.let {
            viewModel.fetchPlaylistVideos(it, getCacheRetrievalPolicy())
        }
    }

    private fun getCacheRetrievalPolicy(): CacheRetrievalPolicy {
        if (activity is VideoPlayerScreen) {
            return CacheRetrievalPolicy.CACHE_FIRST
        }
        return CacheRetrievalPolicy.NETWORK_FIRST
    }
}