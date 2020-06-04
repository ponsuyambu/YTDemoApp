package `in`.ponshere.ytdemoapp.search

import `in`.ponshere.ytdemoapp.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import dagger.android.support.DaggerAppCompatActivity


class SearchScreen : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_screen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    companion object {
        fun launch(caller: Activity) {
            val intent = Intent(caller, SearchScreen::class.java)
            caller.startActivity(intent)
        }
    }
}