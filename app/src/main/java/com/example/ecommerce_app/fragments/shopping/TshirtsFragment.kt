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
import com.example.ecommerce_app.databinding.FragmentTshirtsBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TshirtsFragment : Fragment() {
    lateinit var binding: FragmentTshirtsBinding

    lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTshirtsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }


        itemList = ArrayList()
        itemAdapter = ItemAdapter(requireContext(), itemList) { selectedItem ->
            // Add item to cart in Firebase
            addToCart(selectedItem)
        }
        binding.recyclerViewTshirts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewTshirts.setHasFixedSize(true)
        binding.recyclerViewTshirts.adapter = itemAdapter

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
        database = FirebaseDatabase.getInstance().getReference("tshirts")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        itemList.add(item!!)
                    }
                    binding.recyclerViewTshirts.adapter = itemAdapter
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}
