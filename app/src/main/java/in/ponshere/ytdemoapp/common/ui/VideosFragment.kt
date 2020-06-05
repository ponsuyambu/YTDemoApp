package `in`.ponshere.ytdemoapp.common.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.ViewModelFactory
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.common.viewmodels.InfiniteScrollableViewModel
import `in`.ponshere.ytdemoapp.player.SharedPlayerViewModel
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlistdetails.ui.VideosAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.layout_infinite_scrollable_list.*
import javax.inject.Inject

abstract class VideosFragment<T : InfiniteScrollableViewModel<YTVideosResult, YTVideo>>(private val type: Class<T>) : DaggerFragment(), VideosAdapter.OnVideoClickListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    protected lateinit var viewModel: T
    private lateinit var videosAdapter: VideosAdapter

    private val videos = mutableListOf<YTVideo>()

    private lateinit var sharedPlayerViewModel: SharedPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(type)
        activity?.let {
            sharedPlayerViewModel = ViewModelProvider(it).get(SharedPlayerViewModel::class.java)
        }
        addObservers()
        getVideos()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_infinite_scrollable_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    protected abstract fun getVideos()

    private fun addObservers() {
        viewModel.showProgress().observe(this,
            Observer {

            })

        sharedPlayerViewModel.playlist().observe(this, Observer { playlistId ->
            val videos = viewModel.listModels().value
            if(videos?.get(0) != null) {
                VideoPlayerScreen.launch(activity, playlistId, videos[0])
            }
        })

        sharedPlayerViewModel.endedVideo().observe(this, Observer { endedVideo ->
            val videos = viewModel.listModels().value
            if(videos != null && endedVideo != null) {
                val endedVideoIndex = videos.indexOf(endedVideo)
                if(endedVideoIndex != -1 && endedVideoIndex != videos.size - 1) {
                    sharedPlayerViewModel.playVideo(videos[endedVideoIndex+1])
                }

            }

        })

        viewModel.status().observe(this, Observer {
            if (it == null) {
                tvStatus.visibility = View.GONE
            } else {
                tvStatus.text = it
                tvStatus.visibility = View.VISIBLE
            }
        })

        viewModel.listModels().observe(this,
            Observer {
                it?.let {
                    videos.addAll(it)
                    videosAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun setupRecyclerView() {
        videosAdapter =
            VideosAdapter(
                videos
            )
        videosAdapter.onVideoClickListener = this
        videosAdapter.onEndReachedListener =
            object :
                VideosAdapter.OnEndReachedListener {
                override fun onRecyclerEndReached(position: Int) {
                    getVideos()
                }

            }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = videosAdapter
        }
    }

    override fun onVideoClicked(video: YTVideo) {
        sharedPlayerViewModel.playVideo(video)
    }
}