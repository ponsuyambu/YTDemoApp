package `in`.ponshere.ytdemoapp.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest

interface NetworkState {
    val isConnected: Boolean
}

internal class NetworkStateImp : NetworkState {
    override var isConnected: Boolean = false
}
internal class NetworkCallbackImp(private val holder: NetworkStateImp) : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        holder.isConnected = true
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
        holder.isConnected = false
    }

    override fun onLost(network: Network) {
        holder.isConnected = false
    }
}
object NetworkStateHolder : NetworkState {

    private lateinit var holder: NetworkStateImp

    override val isConnected: Boolean
        get() = holder.isConnected

    fun Application.registerConnectivityMonitor() {
        holder =
            NetworkStateImp()
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(),
            NetworkCallbackImp(holder)
        )
    }

}