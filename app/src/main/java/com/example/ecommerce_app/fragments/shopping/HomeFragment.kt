package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.HomeCategoriesAdapter
import com.example.ecommerce_app.adapters.ItemAdapter
import com.example.ecommerce_app.databinding.FragmentHomeBinding
import com.example.ecommerce_app.models.Category
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeCategoriesAdapter: HomeCategoriesAdapter

    private val categories = listOf(
        Category("Sale", R.drawable.sale),
        Category("Dresses", R.drawable.dress),
        Category("T-shirts", R.drawable.tshirt),
        Category("Pants", R.drawable.pants),
        Category("Jeans", R.drawable.jeans),
        Category("See all", R.drawable.ic_plus)
    )

    lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter

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

        itemList = ArrayList()
        itemAdapter = ItemAdapter(requireContext(), itemList)
        binding.recyclerViewCard.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewCard.setHasFixedSize(true)
        binding.recyclerViewCard.adapter = itemAdapter

        getItemData()
    }

    private fun getItemData() {
        database = FirebaseDatabase.getInstance().getReference("item")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        itemList.add(item!!)
                    }
                    binding.recyclerViewCard.adapter = itemAdapter
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
            }

        })
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
                    "See all" -> findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
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
