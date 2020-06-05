package `in`.ponshere.ytdemoapp.search.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.common.ui.BaseActivity
import `in`.ponshere.ytdemoapp.network.NetworkState
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View.GONE
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search_screen.*
import javax.inject.Inject


class SearchScreen : BaseActivity() {

    @Inject
    lateinit var networkState: NetworkState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_screen)
        title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_screen_menu, menu)
        val menuItem = menu!!.findItem(R.id.searchOption)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Enter the search term"
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    lblSearch.visibility = GONE
                    if(networkState.isConnected) {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.flContainer, SearchVideosFragment.newInstance(it))
                                .commit()
                    } else {
                        Toast.makeText(this@SearchScreen, "No data connection available", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun launch(caller: Activity) {
            val intent = Intent(caller, SearchScreen::class.java)
            caller.startActivity(intent)
        }
    }
}