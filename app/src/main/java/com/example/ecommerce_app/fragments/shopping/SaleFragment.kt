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
import com.example.ecommerce_app.databinding.FragmentSaleBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SaleFragment : Fragment() {
    lateinit var binding: FragmentSaleBinding
    lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaleBinding.inflate(layoutInflater)
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
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            // Add item to cart in Firebase
            addToCart(selectedItem)
        }
        binding.recyclerViewSale.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewSale.setHasFixedSize(true)
        binding.recyclerViewSale.adapter = itemAdapter

        getItemData()
    }
    private fun addToCart(item: Item) {
        val cartDatabase = FirebaseDatabase.getInstance().getReference("cartItems")
        val cartItemId = cartDatabase.push().key // Generate a unique ID for the cart item

        if (cartItemId != null) {
            val cartItem = CartItem(
                id = cartItemId,
                title = item.title,
                imageUrl = item.imageUrl,
                price = item.price,
                quantity=1
            )

            cartDatabase.child(cartItemId).setValue(cartItem).addOnSuccessListener {
                Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Failed to add item to cart: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun getItemData() {
        database = FirebaseDatabase.getInstance().getReference("all")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        itemList.add(item!!)
                    }
                    binding.recyclerViewSale.adapter = itemAdapter
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun showSortOptions(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.sort_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.sort_high_to_low -> {
                    sortSaleByPriceHighToLow()
                    true
                }
                R.id.sort_low_to_high -> {
                    sortSaleByPriceLowToHigh()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun sortSaleByPriceHighToLow() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }
    private fun sortSaleByPriceLowToHigh() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }
}
