package com.example.ecommerce_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.Data.CartItem
import com.example.ecommerce_app.R

class CartAdapter(private val cartItems: MutableList<CartItem>):
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productPhoto: ImageView = itemView.findViewById(R.id.productPhoto)
        val productTitle: TextView = itemView.findViewById(R.id.productTitle)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val increaseQuantity: ImageView = itemView.findViewById(R.id.increaseQuantity)
        val decreaseQuantity: ImageView = itemView.findViewById(R.id.decreaseQuantity)
        val deleteItem: ImageView = itemView.findViewById(R.id.deleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]

        // Bind data to the views
        holder.productPhoto.setImageResource(currentItem.photoResId)
        holder.productTitle.text = currentItem.title
        holder.productPrice.text = "${currentItem.price} $"
        holder.quantity.text = currentItem.quantity.toString()

        // Handle increase quantity
        holder.increaseQuantity.setOnClickListener {
            currentItem.quantity++
            holder.quantity.text = currentItem.quantity.toString()
            notifyItemChanged(position)  // Update item
        }

        // Handle decrease quantity
        holder.decreaseQuantity.setOnClickListener {
            if (currentItem.quantity > 1) {
                currentItem.quantity--
                holder.quantity.text = currentItem.quantity.toString()
                notifyItemChanged(position)
            }
        }
        // Handle delete item
        holder.deleteItem.setOnClickListener {
            cartItems.removeAt(position)  // Removes the item from the list
            notifyItemRemoved(position)  // Notify the adapter that the item was removed
            notifyItemRangeChanged(position, cartItems.size)  // Update the remaining items
        }
    }

    override fun getItemCount() = cartItems.size
}