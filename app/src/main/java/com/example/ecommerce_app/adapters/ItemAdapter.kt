package com.example.ecommerce_app.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.activities.DetailsActivity
import com.example.ecommerce_app.databinding.CardLayoutBinding
import com.example.ecommerce_app.models.Item

class ItemAdapter(var c: Context, var itemList: ArrayList<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    inner class ItemHolder(val binding: CardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: Item) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding = CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val newList = itemList[position]
        holder.bindData(itemList[position])
        //set data for details activity
        Log.d("ItemAdapter", "Clicked Item - Title: ${newList.title}, Price: ${newList.price}, Rating: ${newList.rating}, Rating Count: ${newList.ratingCount}")


        holder.binding.root.setOnClickListener {
            val intent = Intent(c, DetailsActivity::class.java).apply {
                putExtra("imageUrl", newList.imageUrl)
                putExtra("price", newList.price.toString())
                putExtra("title", newList.title)
                putExtra("rating", newList.rating.toString())
                putExtra("ratingCount", newList.ratingCount.toString())
            }
            c.startActivity(intent)
        }
    }

    override fun getItemCount() = itemList.size


}