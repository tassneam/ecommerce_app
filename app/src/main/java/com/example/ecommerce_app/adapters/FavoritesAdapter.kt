import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce_app.R
import com.example.ecommerce_app.models.FavouriteData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FavoritesAdapter(
    private val favoriteItems: MutableList<FavouriteData>
) : RecyclerView.Adapter<FavoritesAdapter.favoriteItemsViewHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("favoriteItems")

    inner class favoriteItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favImg: ImageView = itemView.findViewById(R.id.image)
        val favTitle: TextView = itemView.findViewById(R.id.title)
        val favPrice: TextView = itemView.findViewById(R.id.priceTxt)
        val favRatBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val favRatTxt: TextView = itemView.findViewById(R.id.ratingTxt)
        val favorite: ImageView = itemView.findViewById(R.id.favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favoriteItemsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout, parent, false)
        return favoriteItemsViewHolder(view)
    }

    override fun onBindViewHolder(holder: favoriteItemsViewHolder, position: Int) {
        val currentItem = favoriteItems[position]
        holder.favTitle.text = currentItem.title
        //holder.favPrice.text = currentItem.title
        //holder.favRatTxt.text = currentItem.title
        holder.favRatBar.rating = currentItem.rating

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .placeholder(R.drawable.avatar)
            .into(holder.favImg)

        // Set the favorite icon based on the item's favorite status
        updateFavoriteIcon(holder.favorite, currentItem.isFavorite)

        // Handle favorite icon click
        holder.favorite.setOnClickListener {
            currentItem.isFavorite = !currentItem.isFavorite // Toggle favorite status
            updateFavoriteIcon(holder.favorite, currentItem.isFavorite)

            if (currentItem.isFavorite) {
                // Add item back to favorites in Firebase
            } else {
                // Remove item from favorites in Firebase
                val removedItemId = currentItem.id
                favoriteItems.removeAt(position)
                removeItemFromFirebase(removedItemId, holder.itemView.context)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, favoriteItems.size)
            }
        }
    }

    override fun getItemCount() = favoriteItems.size

    // Function to update the favorite icon based on the favorite status
    private fun updateFavoriteIcon(imageView: ImageView, isFavorite: Boolean) {
        imageView.setImageResource(
            if (isFavorite) R.drawable.ic_favorite // Your filled favorite icon
            else R.drawable.baseline_favorite_border_24 // Your non-favorite icon
        )
    }

    // Function to remove an item from Firebase
    private fun removeItemFromFirebase(itemKey: String, context: Context) {
        databaseReference.child(itemKey).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Item removed from Favorite", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Failed to remove item: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
