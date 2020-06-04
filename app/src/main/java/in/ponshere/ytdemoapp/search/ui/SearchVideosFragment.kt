package `in`.ponshere.ytdemoapp.search.ui

import `in`.ponshere.ytdemoapp.playlistdetails.ui.VideosFragment
import `in`.ponshere.ytdemoapp.search.viewmodels.SearchViewModel
import `in`.ponshere.ytdemoapp.search.viewmodels.SearchViewModelFactory
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

private const val KEY_QUERY_TERM = "queryTerm"

class SearchVideosFragment : VideosFragment() {
    private lateinit var searchViewModel: SearchViewModel
    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    companion object {
        fun newInstance(queryTerm: String): VideosFragment {
            return SearchVideosFragment()
                .apply {
                    val bundle = Bundle().apply {
                        putString(KEY_QUERY_TERM, queryTerm)
                    }
                    arguments = bundle
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel = ViewModelProvider(this, viewModelFactory).get(
            SearchViewModel::class.java
        )
        addObservers()
        val playlistId = arguments?.getString(KEY_QUERY_TERM)
        playlistId?.let {
            searchViewModel.fetchVideosFor(it)
        }
    }

    private fun addObservers() {
        searchViewModel.showProgress().observe(this,
            Observer {

            })

        searchViewModel.searchVideos().observe(this,
            Observer {
                it?.let {
                    videos.addAll(it)
                    videosAdapter.notifyDataSetChanged()
                }
            })
    }
}