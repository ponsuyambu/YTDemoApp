package `in`.ponshere.ytdemoapp.common.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.network.Event
import `in`.ponshere.ytdemoapp.network.NetworkEvents
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.textview.MaterialTextView

class NetworkStatusTextView@JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : MaterialTextView(context, attrs){

    init {
        visibility = View.GONE
        text = context.getString(R.string.offline_status)
        updateView(NetworkEvents.value)
        NetworkEvents.observe(context as AppCompatActivity, Observer {
            updateView(it)

        })
    }

    private fun updateView(it: Event?) {
        if (it is Event.ConnectivityAvailable) {
            visibility = View.GONE
        } else if (it is Event.ConnectivityLost) {
            visibility = View.VISIBLE
        }
    }

}