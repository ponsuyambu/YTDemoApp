package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaylistAdapter (private val playlists: List<YTPlaylist>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaylistViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_playlist_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) =
        holder.bind(playlists[position])

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTile: TextView = view.findViewById(R.id.tvTitle)
        fun bind(playlist: YTPlaylist) {
            tvTile.text = playlist.title
        }
    }
}