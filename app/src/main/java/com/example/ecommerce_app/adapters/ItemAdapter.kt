package com.example.ecommerce_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.databinding.CardLayoutBinding
import com.example.ecommerce_app.models.Item

class ItemAdapter(var c:Context, var itemList: ArrayList<Item>) :
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
        holder.bindData(itemList[position])
    }

    override fun getItemCount() = itemList.size


}