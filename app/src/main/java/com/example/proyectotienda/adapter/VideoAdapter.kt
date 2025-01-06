import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectotienda.R

class VideoAdapter(private val videos: List<Int>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUri = Uri.parse("android.resource://${holder.itemView.context.packageName}/${videos[position]}")
        val mediaPlayer = MediaPlayer.create(holder.itemView.context, videoUri)

        mediaPlayer.setOnPreparedListener { mp ->
            mp.isLooping = true
            mp.start()
        }

        holder.itemView.setOnClickListener {
            mediaPlayer.start()
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
