package com.example.ecommerce_app.activities

import FavoritesAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.CartAdapter
//import com.example.ecommerce_app.adapters.
import com.example.ecommerce_app.adapters.ItemAdapter
import com.example.ecommerce_app.databinding.ActivityFavoriteItemsBinding
import com.example.ecommerce_app.databinding.FavoriteCardBinding
import com.example.ecommerce_app.databinding.FragmentHomeBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.FavouriteData
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FavoriteItemsActivity : AppCompatActivity() {
    private lateinit var favAdapter: FavoritesAdapter
    private val favoriteItemsList : MutableList<FavouriteData> = mutableListOf()
    private lateinit var databaseReference: DatabaseReference


    private lateinit var itemAdapter: ItemAdapter
    private lateinit var binding: ActivityFavoriteItemsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        binding.recyclerViewFavorites .layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewFavorites.setHasFixedSize(true)
        binding.recyclerViewFavorites.adapter = itemAdapter

         */


        //back icon
        findViewById<ImageView>(R.id.backIcon_favorite).setOnClickListener { onBackPressed() }

        loadFavoriteItems()
        setupUI()
    }
   private fun setupUI() {
       favAdapter = FavoritesAdapter(favoriteItemsList) // Initialize the adapter with the list
        findViewById<RecyclerView>(R.id.recyclerViewFavorites).apply {
            layoutManager = LinearLayoutManager(this@FavoriteItemsActivity)
            adapter = favAdapter
        }
    }

    private fun loadFavoriteItems() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("favoriteItems")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteItemsList.clear() // Clear the list before adding items
                for (data in snapshot.children) {
                    val item = data.getValue(FavouriteData::class.java)
                    item?.let { favoriteItemsList.add(it) }
                }
                // Notify the adapter about data changes
                favAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Favorite", "Failed to load Favorite items: ${error.message}")
            }
        })
    }

}