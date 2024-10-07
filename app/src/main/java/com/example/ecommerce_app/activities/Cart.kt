package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.CartAdapter

class Cart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        //Setup RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)

        val totalPriceTextView = findViewById<TextView>(R.id.totalPrice)

        // Setup adapter and handle total price update
        val adapter = CartAdapter(cartItemsList) { totalPrice ->
            // Update the checkout button's text or a total price TextView (if available)
            totalPriceTextView.text = String.format("%.2f EGP", totalPrice)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Calculate and display the initial total price after the adapter is set up
        totalPriceTextView.text = String.format("%.2f EGP", adapter.calculateTotalPrice())

        //checkout button
        val checkout_btn=findViewById<Button>(R.id.checkout_btn)
        checkout_btn.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            val totalPrice = adapter.calculateTotalPrice()  // Calculate total price
            intent.putExtra("total_price", totalPrice)
            startActivity(intent)
        }
        //back icon
        val backIcon = findViewById<ImageView>(R.id.backIcon)
        backIcon.setOnClickListener {
            onBackPressed() // Or finish() in an activity
        }
    }


    //some list items
    val cartItemsList = mutableListOf(
        CartItem(R.drawable.person, "Product 1", 20.00, 1),
        CartItem(R.drawable.person, "Product 2", 10.00, 1),
        CartItem(R.drawable.person, "Product 3", 15.50, 1)
    )
}
