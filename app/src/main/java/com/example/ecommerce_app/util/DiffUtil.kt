package com.example.ecommerce_app.util

import androidx.recyclerview.widget.DiffUtil
import com.example.ecommerce_app.models.Category
import com.example.ecommerce_app.models.Review

class DiffUtil(
    val oldList: List<Review>,
    val newList: List<Review>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}