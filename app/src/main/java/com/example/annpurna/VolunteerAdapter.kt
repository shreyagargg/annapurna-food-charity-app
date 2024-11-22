
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.annpurna.R

data class DeliveryItem(
    val sourceAddress: String,
    val destinationAddress: String,
    val imageUrl: Int // Assuming a drawable resource for simplicity
)

class VolunteerAdapter(private val items: List<DeliveryItem>) :
    RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder>() {

    inner class VolunteerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val sourceAddress: TextView = itemView.findViewById(R.id.source_address)
        val destinationAddress: TextView = itemView.findViewById(R.id.destination_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VolunteerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.volunteer_item_card, parent, false)
        return VolunteerViewHolder(view)
    }

    override fun onBindViewHolder(holder: VolunteerViewHolder, position: Int) {
        val item = items[position]
        holder.itemImage.setImageResource(item.imageUrl)
        holder.sourceAddress.text = "Source: ${item.sourceAddress}"
        holder.destinationAddress.text = "Destination: ${item.destinationAddress}"
    }

    override fun getItemCount(): Int = items.size
}
