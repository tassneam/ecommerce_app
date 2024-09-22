package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.HomeCategoriesAdapter
import com.example.ecommerce_app.databinding.FragmentHomeBinding
import com.example.ecommerce_app.models.Category

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeCategoriesAdapter: HomeCategoriesAdapter

    private val categories = listOf(
        Category("Sale", R.drawable.sale),
        Category("Dresses", R.drawable.dress),
        Category("T-shirts", R.drawable.tshirt),
        Category("Pants", R.drawable.pants),
        Category("Jeans", R.drawable.jeans)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchEditTxt.isCursorVisible = false

        // Show progress bar
        binding.progressBarCategories.visibility = View.VISIBLE
        binding.recyclerViewCategories.visibility = View.GONE

        // Set up RecyclerView
        binding.recyclerViewCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Simulate loading data (replace with actual data loading logic)
        loadCategories()
    }

    private fun loadCategories() {
        // Simulating a network delay
        Handler(Looper.getMainLooper()).postDelayed({
            // Initialize the adapter after data is "loaded"
            homeCategoriesAdapter = HomeCategoriesAdapter(categories) { category ->
                when (category.title) {
                    "Sale" -> findNavController().navigate(R.id.action_homeFragment_to_saleFragment)
                    "Dresses" -> findNavController().navigate(R.id.action_homeFragment_to_dressesFragment)
                    "T-shirts" -> findNavController().navigate(R.id.action_homeFragment_to_tshirtsFragment)
                    "Pants" -> findNavController().navigate(R.id.action_homeFragment_to_pantsFragment)
                    "Jeans" -> findNavController().navigate(R.id.action_homeFragment_to_jeansFragment)
                }
            }

            // Set the adapter
            binding.recyclerViewCategories.adapter = homeCategoriesAdapter

            // Hide progress bar and show RecyclerView
            binding.progressBarCategories.visibility = View.GONE
            binding.recyclerViewCategories.visibility = View.VISIBLE
        }, 1000) // Simulate a delay of 2 seconds
    }
}
