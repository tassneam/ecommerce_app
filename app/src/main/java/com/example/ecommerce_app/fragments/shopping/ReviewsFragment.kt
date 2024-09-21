package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.adapters.ReviewAdapter
import com.example.ecommerce_app.databinding.FragmentReviewsBinding
import com.example.ecommerce_app.models.Review

class ReviewsFragment : Fragment() {
    private lateinit var binding: FragmentReviewsBinding
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(inflater, container, false)

        // Static review data
        val staticReviews = listOf(
            Review(1, "John Doe", "Great product!", "https://example.com/john_pic.jpg", 4.5, 101),
            Review(2, "Jane Smith", "Good value for money.", "https://example.com/jane_pic.jpg", 4.0, 101),
            Review(3, "Mike Johnson", "Highly recommend!", "https://example.com/mike_pic.jpg", 5.0, 101)
        )

        // Setup RecyclerView
        reviewAdapter = ReviewAdapter(staticReviews)
        binding.recyclerViewReview.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewReview.adapter = reviewAdapter

        return binding.root
    }
}
