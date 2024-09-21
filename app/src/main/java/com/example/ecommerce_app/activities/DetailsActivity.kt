package com.example.ecommerce_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.fragments.shopping.DescriptionFragment
import com.example.ecommerce_app.R
import com.example.ecommerce_app.fragments.shopping.ReviewsFragment
import com.example.ecommerce_app.adapters.DetailsPagerAdapter
import com.example.ecommerce_app.adapters.SizeAdapter
import com.example.ecommerce_app.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var sizeAdapter: SizeAdapter
    lateinit var sizeList: List<String>

    lateinit var detailsPagerAdapter:DetailsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sizeList = listOf("S", "M", "L", "XL")
        sizeAdapter = SizeAdapter(sizeList)
        val recyclerViewSize = findViewById<RecyclerView>(R.id.recyclerViewSize)
        recyclerViewSize.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewSize.adapter = sizeAdapter


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

    }
}
