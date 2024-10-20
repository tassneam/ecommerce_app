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

class BillingAndAddresses : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing_and_addresses)

        val backIcon_BillingAndAddresses = findViewById<ImageView>(R.id.backIcon_BillingAndAddresses)
        backIcon_BillingAndAddresses.setOnClickListener {
            onBackPressed()
        }

        val OnlinePaymentButton=findViewById<Button>(R.id.btnOnlinePayment)
      /*  OnlinePaymentButton.setOnClickListener {
            val intent = Intent(this, ::class.java)
            startActivity(intent)
        }

       */
    }
}