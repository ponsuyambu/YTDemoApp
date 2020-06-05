package `in`.ponshere.ytdemoapp.playlistdetails.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class VideosAdapter (private val videos: List<YTVideo>) :
    RecyclerView.Adapter<VideosAdapter.PlaylistVideoViewHolder>(){

    var onEndReachedListener: OnEndReachedListener? = null
    var onVideoClickListener: OnVideoClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlaylistVideoViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.video_playlist_item,
            parent,
            false
        ), onVideoClickListener
    )

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: PlaylistVideoViewHolder, position: Int) {
        holder.bind(videos[position])

        if (onEndReachedListener != null && holder.adapterPosition == videos.size - 1) {
            onEndReachedListener?.onRecyclerEndReached(holder.adapterPosition)
        }
    }

    class PlaylistVideoViewHolder(view: View, private val onVideoClickListener: OnVideoClickListener?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var playlistVideo: YTVideo? = null
        private val tvTile: TextView = view.findViewById(R.id.tvTitle)
        private val tvAuthor: TextView = view.findViewById(R.id.tvAuthor)
        private val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        private val imgIcon: ImageView = view.findViewById(R.id.imgPlaylistIcon)
        private val cardView: CardView = view.findViewById(R.id.cvPlaylist)

        init {
            cardView.setOnClickListener(this)
        }

        fun bind(playlistVideo: YTVideo) {
            this.playlistVideo = playlistVideo
            tvTile.text = playlistVideo.title
            tvAuthor.text = playlistVideo.author
            tvDuration.text = playlistVideo.duration
            Picasso.get().load(playlistVideo.icon).into(imgIcon)
        }

        override fun onClick(view: View) {
            playlistVideo?.let { video ->
                onVideoClickListener?.let { onVideoClickListener.onVideoClicked(video)  }
            }
        }
    }

    interface OnEndReachedListener {
        fun onRecyclerEndReached(position: Int)
    }
    interface OnVideoClickListener {
        fun onVideoClicked(video: YTVideo)
    }
}
