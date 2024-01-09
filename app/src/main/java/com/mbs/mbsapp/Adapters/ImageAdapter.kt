// ImageAdapter.kt
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mbs.mbsapp.Model.CampaignMediaModel
import com.mbs.mbsapp.R
import com.mbs.mbsapp.Utils.Constants

class ImageAdapter(
    var context: Context,
    private val images: List<CampaignMediaModel>,
    private val onItemClick: (CampaignMediaModel) -> Unit
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(images[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = images[position]
        //holder.imageView.setImageResource(currentImage.activityMediaPath)
        var image =
            Constants.baseURLforPicture + "/storage" + currentImage.activityMediaPath + currentImage.activityMediaName
        Glide.with(context)
            .load(image)
            .into(holder.imageView)
    }

    override fun getItemCount() = images.size
}
