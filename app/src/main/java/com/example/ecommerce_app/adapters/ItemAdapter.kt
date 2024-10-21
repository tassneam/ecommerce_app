package com.example.ecommerce_app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.activities.DetailsActivity
import com.example.ecommerce_app.databinding.CardLayoutBinding
import com.example.ecommerce_app.models.Item

class ItemAdapter(
    private val context: Context,
    private val itemList: ArrayList<Item>,
    private val onCartClick: (Item) -> Unit, // Callback for cart icon click
    private val onFavoriteClick: (Item) -> Unit // Callback for cart icon click

) : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    inner class ItemHolder(val binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item1: Item) {
            binding.item1 = item1

            // Update favorite icon based on item's favorite status
            binding.favorite.setImageResource(
                if (item1.isFavorite) R.drawable.ic_favorite // replace with your favorite icon
                else R.drawable.baseline_favorite_border_24 // replace with your non-favorite icon
            )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val newItem = itemList[position]
        holder.bindData(newItem)

        // Navigate to DetailsActivity with the selected item's data
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("itemDetails", newItem)
            context.startActivity(intent)
        }

        // Handle cart icon click
        holder.binding.cart.setOnClickListener {
            onCartClick(newItem)
        }

        // Handle favorite icon click
        holder.binding.favorite.setOnClickListener {
            newItem.isFavorite = !newItem.isFavorite // Toggle favorite status
            onFavoriteClick(newItem)
            notifyItemChanged(position) // Update the UI for this item
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}