package com.example.ecommerce_app.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.fragments.shopping.DescriptionFragment
import com.example.ecommerce_app.fragments.shopping.ReviewsFragment
import com.example.ecommerce_app.adapters.DetailsPagerAdapter
import com.example.ecommerce_app.adapters.SizeAdapter
import com.example.ecommerce_app.databinding.ActivityDetailsBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var sizeAdapter: SizeAdapter
    lateinit var sizeList: List<String>
    lateinit var detailsPagerAdapter: DetailsPagerAdapter


        private lateinit var cartDatabase: DatabaseReference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityDetailsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Initialize Firebase Database reference
            cartDatabase = FirebaseDatabase.getInstance().getReference("cartItems")

            // Initialize size options
            sizeList = listOf("S", "M", "L", "XL")
            sizeAdapter = SizeAdapter(sizeList)
            binding.recyclerViewSize.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewSize.adapter = sizeAdapter

            // Initialize fragments for view pager
            val fragments = listOf(
                DescriptionFragment(),
                ReviewsFragment()
            )

            detailsPagerAdapter = DetailsPagerAdapter(this, supportFragmentManager, fragments)
            binding.viewPager.adapter = detailsPagerAdapter
            binding.tabLayout.setupWithViewPager(binding.viewPager)

            // Back button functionality
            binding.backBtn.setOnClickListener {
                finish()
            }

            // Retrieve the Item object from Intent
            val item = intent.getParcelableExtra<Item>("itemDetails")
            binding.itemDetails = item

            // Add to Cart button functionality
            binding.addToCartBtn.setOnClickListener {
                if (item != null) {
                    addToCart(item)
                } else {
                    Toast.makeText(this, "Item details not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun addToCart(item: Item) {
            cartDatabase.orderByChild("title").equalTo(item.title)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (cartItemSnapshot in snapshot.children) {
                                val existingCartItem = cartItemSnapshot.getValue(CartItem::class.java)
                                if (existingCartItem != null) {
                                    val newQuantity = existingCartItem.quantity + 1
                                    cartItemSnapshot.ref.child("quantity").setValue(newQuantity)
                                        .addOnSuccessListener {
                                            Toast.makeText(this@DetailsActivity, "Item quantity updated", Toast.LENGTH_SHORT).show()
                                        }.addOnFailureListener {
                                            Toast.makeText(this@DetailsActivity, "Failed to update item: ${it.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                        } else {
                            val cartItemId = cartDatabase.push().key
                            if (cartItemId != null) {
                                val newCartItem = CartItem(
                                    id = cartItemId,
                                    title = item.title,
                                    imageUrl = item.imageUrl,
                                    price = item.price,
                                    quantity = 1
                                )
                                cartDatabase.child(cartItemId).setValue(newCartItem)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@DetailsActivity, "Item added to cart", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(this@DetailsActivity, "Failed to add item to cart: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@DetailsActivity, "Failed to check cart: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

