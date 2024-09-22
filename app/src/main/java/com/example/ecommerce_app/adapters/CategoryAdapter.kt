package com.example.ecommerce_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.databinding.CategoryCardLayoutBinding
import com.example.ecommerce_app.models.Category

class CategoryAdapter(
    private val categoryList: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    inner class CategoryHolder(private val binding: CategoryCardLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(category: Category) {
            binding.title.text = category.title
            binding.image.setImageResource(category.imageResId)

            binding.root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = CategoryCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bindData(categoryList[position])
    }

    override fun getItemCount() = categoryList.size
}
