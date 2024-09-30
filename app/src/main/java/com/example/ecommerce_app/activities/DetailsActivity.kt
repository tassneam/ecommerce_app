package com.example.ecommerce_app.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.fragments.shopping.DescriptionFragment
import com.example.ecommerce_app.R
import com.example.ecommerce_app.fragments.shopping.ReviewsFragment
import com.example.ecommerce_app.adapters.DetailsPagerAdapter
import com.example.ecommerce_app.adapters.SizeAdapter
import com.example.ecommerce_app.databinding.ActivityDetailsBinding
import com.example.ecommerce_app.util.getProgessDrawable
import com.example.ecommerce_app.util.loadImage

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var sizeAdapter: SizeAdapter
    lateinit var sizeList: List<String>
    lateinit var detailsPagerAdapter: DetailsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sizeList = listOf("S", "M", "L", "XL")
        sizeAdapter = SizeAdapter(sizeList)
        binding.recyclerViewSize.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewSize.adapter = sizeAdapter

        binding.backBtn.setOnClickListener {
            finish()
        }

        val fragments = listOf(
            DescriptionFragment(),
            ReviewsFragment()
        )

        detailsPagerAdapter = DetailsPagerAdapter(supportFragmentManager, fragments)
        binding.viewPager.adapter = detailsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Get data from item adapter
        val itemIntent = intent
        val itemImageUrl = itemIntent.getStringExtra("imageUrl") ?: ""
        val itemTitle = itemIntent.getStringExtra("title") ?: "No Title"
        val itemPrice = itemIntent.getStringExtra("price")?.toDoubleOrNull() ?: 0.0
        val itemRating = itemIntent.getStringExtra("rating")?.toFloatOrNull() ?: 0f
        val itemRatingCount = itemIntent.getStringExtra("ratingCount")?.toIntOrNull() ?: 0

        Log.d("DetailsActivity", "Retrieved Data - Image URL: $itemImageUrl, Title: $itemTitle, Price: $itemPrice, Rating: $itemRating, Rating Count: $itemRatingCount")

        // Set data to UI components
        binding.imageUrl.loadImage(itemImageUrl, getProgessDrawable(this))
        binding.titleTxt.text = itemTitle
        binding.priceTxt.text = String.format("EGP %.2f", itemPrice) // Ensure price is formatted
        binding.ratingBar.rating = itemRating
        binding.ratingTxt.text = String.format("Rating (%d)", itemRatingCount)
    }
}
