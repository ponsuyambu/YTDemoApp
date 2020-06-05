package `in`.ponshere.ytdemoapp.search.ui

import `in`.ponshere.ytdemoapp.ViewModelFactory
import `in`.ponshere.ytdemoapp.playlistdetails.ui.VideosFragment
import `in`.ponshere.ytdemoapp.search.viewmodels.SearchViewModel
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.layout_infinite_scrollable_list.*
import javax.inject.Inject

private const val KEY_QUERY_TERM = "queryTerm"

class SearchVideosFragment : VideosFragment() {
    private lateinit var searchViewModel: SearchViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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
        getVideos()
    }

    override fun getVideos() {
        val playlistId = arguments?.getString(KEY_QUERY_TERM)
        playlistId?.let {
            searchViewModel.fetchVideosFor(it)
        }
    }

    private fun addObservers() {
        searchViewModel.showProgress().observe(this,
            Observer {

            })

        searchViewModel.status().observe(this, Observer {
            if(it == null) {
                tvStatus.visibility = View.GONE
            } else {
                tvStatus.text = it
                tvStatus.visibility = View.VISIBLE
            }
        })

        searchViewModel.videos().observe(this,
            Observer {
                it?.let {
                    videos.addAll(it)
                    videosAdapter.notifyDataSetChanged()
                }
            })
    }
}