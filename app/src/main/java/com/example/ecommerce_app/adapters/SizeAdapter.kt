package com.example.ecommerce_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.SizeLayoutBinding

class SizeAdapter(private var sizeList: List<String>) : RecyclerView.Adapter<SizeAdapter.SizeHolder>() {

    // To track the selected position
    private var selectedPosition = -1

    inner class SizeHolder(val binding: SizeLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(size: String, position: Int) {
            binding.circleText.text = size

            if (position == selectedPosition) {
                binding.circleText.setBackgroundResource(R.drawable.highlighted_circle)
            } else {
                binding.circleText.setBackgroundResource(R.drawable.circle_shape) // Default background
            }

            binding.root.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged() // Notify adapter to refresh views
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeHolder {
        val binding = SizeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeHolder(binding)
    }

    override fun onBindViewHolder(holder: SizeHolder, position: Int) {
        holder.bindData(sizeList[position], position)
    }

    override fun getItemCount() = sizeList.size
}
