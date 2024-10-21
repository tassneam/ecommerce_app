package com.example.ecommerce_app.fragments.shopping

import FavoritesAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
//import com.example.ecommerce_app.adapters.FavoritesAdapter
import com.example.ecommerce_app.adapters.ItemAdapter
import com.example.ecommerce_app.databinding.FragmentTshirtsBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.FavouriteData
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TshirtsFragment : Fragment() {
    lateinit var binding: FragmentTshirtsBinding
    private lateinit var recyclerView: RecyclerView
    lateinit var database: DatabaseReference
    private lateinit var itemList: ArrayList<Item>
    private lateinit var itemAdapter: ItemAdapter

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoriteItems: ArrayList<FavouriteData>
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
        // Initialize the adapter with item list and click listeners
        itemAdapter = ItemAdapter(
            context = requireContext(),
            itemList = itemList,
            onCartClick = { item ->
                addToCart(item) // Define this function to handle cart click
            },
            onFavoriteClick = { item ->
                addToFavorites(convertToFavouriteData(item)) // Define this function to handle favorite click
            }
        )
        // Set the adapter to the RecyclerView
        //recyclerView.adapter = itemAdapter
        binding.recyclerViewTshirts.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewTshirts.setHasFixedSize(true)
        binding.recyclerViewTshirts.adapter = itemAdapter
        getItemData()
        //fetchFavorites()
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
//    private fun fetchFavorites() {
//        val db = FirebaseDatabase.getInstance().reference.child("item")
//        db.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                favoriteItems.clear()
//                for (data in snapshot.children) {
//                    val item = data.getValue(FavouriteData::class.java)
//                    if (item != null) {
//                        favoriteItems.add(item)
//                    }
//                }
//                // Notify adapter about data changes
//                favoritesAdapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//                Log.e("Favorite", "Failed to load Favorite items: ${error.message}")
//            }
//        })
//    }

    private fun addToFavorites(item: FavouriteData) {
        val favoritesDatabase = FirebaseDatabase.getInstance().getReference("favoriteItems")
        favoritesDatabase.orderByChild("title").equalTo(item.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(requireContext(), "Item is already in favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        val favoriteItemId = favoritesDatabase.push().key
                        if (favoriteItemId != null) {
                            val newFavoriteItem = FavouriteData(
                                id = favoriteItemId,
                                imageUrl = item.imageUrl,
                                title = item.title,
                                price = item.price,
                                rating = item.rating,
                                ratingCount = item.ratingCount,
                                isFavorite = true
                            )
                            favoritesDatabase.child(favoriteItemId).setValue(newFavoriteItem)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Item added to favorites", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed to add item to favorites: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to add to favorites: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun convertToFavouriteData(item: Item): FavouriteData {
        return FavouriteData(
            id = item.id, // Assuming `Item` has an `id` field, otherwise generate a unique ID
            imageUrl = item.imageUrl,
            title = item.title,
            price = item.price,
            rating = item.rating, // Assuming `Item` has a `rating` field
            ratingCount = item.ratingCount, // Assuming `Item` has a `ratingCount` field
            isFavorite = true // Mark it as a favorite
        )
    }
}
