package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        val isLogin: Boolean = firebaseAuth.currentUser != null

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (isLogin) {
                // Start MainActivity instead of HomeFragment
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, OnBoardingActivity::class.java)
                startActivity(intent)
            }
            finish() // Close SplashScreen
        }, 5000)
    }
    private fun init() {
        firebaseAuth = FirebaseAuth.getInstance()
    }
}
