package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.ItemAdapter
import com.example.ecommerce_app.databinding.FragmentPantsBinding
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PantsFragment : Fragment() {
    lateinit var binding: FragmentPantsBinding
    lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPantsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sort.setOnClickListener {
            showSortOptions(it)
        }

        itemList = ArrayList()
        itemAdapter = ItemAdapter(requireContext(), itemList)
        binding.recyclerViewPants.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewPants.setHasFixedSize(true)
        binding.recyclerViewPants.adapter = itemAdapter
        getItemData()
    }
    private fun getItemData() {
        database = FirebaseDatabase.getInstance().getReference("pants")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        itemList.add(item!!)
                    }
                    binding.recyclerViewPants.adapter = itemAdapter
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showSortOptions(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.sort_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.sort_high_to_low -> {
                    sortPantsByPriceHighToLow()
                    true
                }
                R.id.sort_low_to_high -> {
                    sortPantsByPriceLowToHigh()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun sortPantsByPriceHighToLow() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }
    private fun sortPantsByPriceLowToHigh() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }
}
