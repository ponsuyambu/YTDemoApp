package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PlaylistAdapter (private val playlists: List<YTPlaylist>) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>(){

    var onEndReachedListener: OnEndReachedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaylistViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_playlist_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])

        if (onEndReachedListener != null && holder.adapterPosition == playlists.size - 1) {
            onEndReachedListener?.onRecyclerEndReached(holder.adapterPosition)
        }
    }

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTile: TextView = view.findViewById(R.id.tvTitle)
        private val tvCount: TextView = view.findViewById(R.id.tvCount)
        private val imgIcon: ImageView = view.findViewById(R.id.imgPlaylistIcon)
        fun bind(playlist: YTPlaylist) {
            tvTile.text = playlist.title
            tvCount.text = playlist.videosCount.toString()
            Picasso.get().load(playlist.icon).into(imgIcon)
        }
    }

    interface OnEndReachedListener {
        fun onRecyclerEndReached(position: Int)
    }
}


