package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.player.VideoPlayerScreen
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PlaylistDetailsAdapter (private val playlistVideos: List<YTVideo>) :
    RecyclerView.Adapter<PlaylistDetailsAdapter.PlaylistVideoViewHolder>(){

    var onEndReachedListener: OnEndReachedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaylistVideoViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.video_playlist_item,
            parent,
            false
        )
    )

    override fun getItemCount(): Int = playlistVideos.size

    override fun onBindViewHolder(holder: PlaylistVideoViewHolder, position: Int) {
        holder.bind(playlistVideos[position])

        if (onEndReachedListener != null && holder.adapterPosition == playlistVideos.size - 1) {
            onEndReachedListener?.onRecyclerEndReached(holder.adapterPosition)
        }
    }

    class PlaylistVideoViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var playlistVideo: YTVideo? = null
        private val tvTile: TextView = view.findViewById(R.id.tvTitle)
        private val imgIcon: ImageView = view.findViewById(R.id.imgPlaylistIcon)
        private val cardView: CardView = view.findViewById(R.id.cvPlaylist)

        init {
            cardView.setOnClickListener(this)
        }

        fun bind(playlistVideo: YTVideo) {
            this.playlistVideo = playlistVideo
            tvTile.text = playlistVideo.title
            Picasso.get().load(playlistVideo.icon).into(imgIcon)
        }

        override fun onClick(view: View) {
            val context = itemView.context
            playlistVideo?.let {
                VideoPlayerScreen.launch(context as Activity, it)
            }
        }
    }

    interface OnEndReachedListener {
        fun onRecyclerEndReached(position: Int)
    }
}
