package `in`.ponshere.ytdemoapp.search.ui

import `in`.ponshere.ytdemoapp.common.ui.VideosFragment
import `in`.ponshere.ytdemoapp.search.viewmodels.SearchViewModel
import android.os.Bundle

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

    override fun getVideos() {
        val playlistId = arguments?.getString(KEY_QUERY_TERM)
        playlistId?.let {
            viewModel.fetchVideosFor(it)
        }
    }

}