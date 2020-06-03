package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.ui.PlaylistDetailsScreen
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var playlist: YTPlaylist? = null
        private val tvTile: TextView = view.findViewById(R.id.tvTitle)
        private val tvCount: TextView = view.findViewById(R.id.tvCount)
        private val imgIcon: ImageView = view.findViewById(R.id.imgPlaylistIcon)
        private val cardView: CardView = view.findViewById(R.id.cvPlaylist)

        init {
            cardView.setOnClickListener(this)
        }

        fun bind(playlist: YTPlaylist) {
            this.playlist = playlist
            tvTile.text = playlist.title
            tvCount.text = playlist.videosCount.toString()
            Picasso.get().load(playlist.icon).into(imgIcon)
        }

        override fun onClick(view: View) {
            val context = itemView.context
            playlist?.let {
                PlaylistDetailsScreen.launch(context as Activity, it)
            }
        }
    }

    interface OnEndReachedListener {
        fun onRecyclerEndReached(position: Int)
    }
}
