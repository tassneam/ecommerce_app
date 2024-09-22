package com.example.ecommerce_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.CategoryLayoutBinding
import com.example.ecommerce_app.models.Category

class HomeCategoriesAdapter(
    private val categoryList: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryHolder>() {

    private var selectedPosition = -1

    inner class CategoryHolder(private val binding: CategoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: Category, position: Int) {
            binding.title.text = category.title
            binding.image.setImageResource(category.imageResId)
            binding.image.setBackgroundResource(
                if (position == selectedPosition) R.drawable.highlighted_circle
                else R.drawable.circle_shape
            )

            binding.root.setOnClickListener {
                // Update the selected position and notify the adapter
                selectedPosition = position
                notifyDataSetChanged() // Refresh the entire list
                onCategoryClick(category) // Trigger the click event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindData(categoryList[position], position)
    }

    override fun getItemCount() = categoryList.size
}
