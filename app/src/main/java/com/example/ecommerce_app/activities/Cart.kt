package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.CartAdapter
import com.example.ecommerce_app.models.CartItem
import com.google.firebase.database.*

class Cart : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private val cartItemsList = mutableListOf<CartItem>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var totalPriceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("cartItems")

        // Setup RecyclerView
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPrice) // Assuming you have a TextView for total price

        // Initialize CartAdapter with onPriceUpdated lambda
        cartAdapter = CartAdapter(cartItemsList) { updatedTotalPrice ->
            totalPriceTextView.text = " $updatedTotalPrice $" // Update total price TextView
        }

        cartRecyclerView.adapter = cartAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load cart items from Firebase
        loadCartItems()

        // Checkout button
        val checkoutButton = findViewById<Button>(R.id.checkout_btn)
        checkoutButton.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            startActivity(intent)
        }

        // Back icon
        val backIcon = findViewById<ImageView>(R.id.backIcon)
        backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadCartItems() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItemsList.clear() // Clear the existing list to avoid duplicates
                for (cartItemSnapshot in snapshot.children) {
                    val cartItem = cartItemSnapshot.getValue(CartItem::class.java)
                    cartItem?.let { cartItemsList.add(it) }
                }
                cartAdapter.notifyDataSetChanged() // Notify adapter about data changes
                updateTotalPrice() // Update total price after loading items
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CartActivity", "Failed to load cart items: ${error.message}")
            }
        })
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItemsList.sumOf { it.price * it.quantity }
        totalPriceTextView.text = "$totalPrice EGP"
    }
}
