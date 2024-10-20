package com.example.ecommerce_app

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DialogOfflinePayment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dialog_offline_payment)

        //animation
        val deliveryView = findViewById<ImageView>(R.id.deliveryView)
        // Load the animation
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_animation)
        val translateAnimation=AnimationUtils.loadAnimation(this, R.anim.translate_animation)
        // Start the animation on the deliveryView
        deliveryView.startAnimation(translateAnimation)
        // Set up the OK button to dismiss the dialog (if you have a dialog)
        val btnOk = findViewById<Button>(R.id.btnOk)
        btnOk.setOnClickListener {
            finish()  // Close the activity or dialog when OK is clicked
        }
    }
}