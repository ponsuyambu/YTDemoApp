package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_playlist_videos.*

abstract class VideosFragment : DaggerFragment() {
    protected lateinit var videosAdapter: PlaylistDetailsAdapter

    protected val videos = mutableListOf<YTVideo>()

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

    private fun setupRecyclerView() {
        videosAdapter = PlaylistDetailsAdapter(videos)
        videosAdapter.onEndReachedListener =
            object : PlaylistDetailsAdapter.OnEndReachedListener {
                override fun onRecyclerEndReached(position: Int) {
                }

            }
        playlistVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videosAdapter
        }
    }
}