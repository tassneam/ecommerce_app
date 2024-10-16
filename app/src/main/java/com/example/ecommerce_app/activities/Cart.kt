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

    private lateinit var cartAdapter: CartAdapter
    private val cartItemsList = mutableListOf<CartItem>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var totalPriceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

    // Initialize totalPriceTextView here
    totalPriceTextView = findViewById(R.id.totalPrice)

    val checkoutBtn = findViewById<Button>(R.id.checkout_btn)
    checkoutBtn.setOnClickListener {
        // Retrieve the total price value from the TextView and pass it to the Payment activity
        val totalPrice = totalPriceTextView.text.toString().replace(" EGP", "").toDouble()
        val intent = Intent(this, Payment::class.java)
        intent.putExtra("total_price", totalPrice)
        startActivity(intent)
    }
    setupUI()
    loadCartItems()
}
    private fun setupUI() {
        totalPriceTextView = findViewById(R.id.totalPrice)

        cartAdapter = CartAdapter(cartItemsList) { updatedTotalPrice ->
            totalPriceTextView.text = String.format("%.2f EGP", updatedTotalPrice)
        }

        findViewById<RecyclerView>(R.id.cartRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@Cart)
            adapter = cartAdapter
        }

        findViewById<ImageView>(R.id.backIcon).setOnClickListener { onBackPressed() }
    }

    private fun loadCartItems() {
        databaseReference = FirebaseDatabase.getInstance().getReference("cartItems")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartItemsList.clear()
                for (cartItemSnapshot in snapshot.children) {
                    val cartItem = cartItemSnapshot.getValue(CartItem::class.java)
                    cartItem?.let { cartItemsList.add(it) }
                }
                cartAdapter.notifyDataSetChanged()
                updateTotalPrice()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cart", "Failed to load cart items: ${error.message}")
            }
        })
    }

    private fun updateTotalPrice() {
        val totalPrice = cartItemsList.sumOf { it.price * it.quantity }
        totalPriceTextView.text = String.format("%.2f EGP", totalPrice)
    }
}
