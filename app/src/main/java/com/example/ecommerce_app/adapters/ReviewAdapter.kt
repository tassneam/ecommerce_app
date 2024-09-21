package com.example.ecommerce_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.databinding.ReviewLayoutBinding
import com.example.ecommerce_app.models.Review

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewHolder>() {

    inner class ReviewHolder(private val binding: ReviewLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.review = review
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val binding = ReviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size
}
