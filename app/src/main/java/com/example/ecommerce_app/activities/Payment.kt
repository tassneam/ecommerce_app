package com.example.ecommerce_app.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecommerce_app.R

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)


        // Get the total price from the Intent
        val totalPrice = intent.getDoubleExtra("total_price", 0.0)
        Log.d("PaymentActivity", "Total Price: $totalPrice")

        // Display the total price in the total_amount_value TextView using string resource
        val totalAmountValue = findViewById<TextView>(R.id.total_amount_value)
         // Use the resource string
        totalAmountValue.text = getString(R.string.total_amount_format, totalPrice)

        // Display the total price in the total_amount_value TextView
        totalAmountValue.text = String.format("%.2f", totalPrice) + " EGP"  // Format to 2 decimal places

        //back icon
        val backIcon = findViewById<ImageView>(R.id.backIcon)
        backIcon.setOnClickListener {
            onBackPressed() // Or finish() in an activity
        }

    }
}