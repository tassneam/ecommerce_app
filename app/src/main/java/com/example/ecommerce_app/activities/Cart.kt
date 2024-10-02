package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_app.R

class Cart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

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
}
