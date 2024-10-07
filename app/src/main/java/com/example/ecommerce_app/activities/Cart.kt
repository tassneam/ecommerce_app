package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        val adapter = CartAdapter(cartItemsList)  // cartItemsList is your data
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //checkout button
        val checkout_btn=findViewById<Button>(R.id.checkout_btn)
        checkout_btn.setOnClickListener {
           val intent= Intent(this , Payment::class.java)
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
        CartItem(R.drawable.person, "Product 1", 19.99, 2),
        CartItem(R.drawable.person, "Product 2", 9.99, 1),
        CartItem(R.drawable.person, "Product 3", 14.99, 3)
    )
}
