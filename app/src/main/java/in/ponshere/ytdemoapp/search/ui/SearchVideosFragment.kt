package `in`.ponshere.ytdemoapp.search.ui

import `in`.ponshere.ytdemoapp.common.ui.VideosFragment
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.search.viewmodels.SearchViewModel
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.layout_infinite_scrollable_list.*

private const val KEY_QUERY_TERM = "queryTerm"

class SearchVideosFragment : VideosFragment<SearchViewModel>(SearchViewModel::class.java) {

    companion object {
        fun newInstance(queryTerm: String): VideosFragment<SearchViewModel> {
            return SearchVideosFragment()
                .apply {
                    val bundle = Bundle().apply {
                        putString(KEY_QUERY_TERM, queryTerm)
                    }
                    arguments = bundle
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabPlayAll.setOnClickListener {
            val videos = viewModel.listModels().value
            val queryTerm = arguments?.getString(KEY_QUERY_TERM)
            if(videos?.get(0) != null && queryTerm != null) {
                VideoPlayerScreen.launchWithSearchQuery(activity, queryTerm, videos[0])
            }
        }
    }

    override fun getVideos() {
        val playlistId = arguments?.getString(KEY_QUERY_TERM)
        playlistId?.let {
            viewModel.fetchVideosFor(it)
        }
    }

}