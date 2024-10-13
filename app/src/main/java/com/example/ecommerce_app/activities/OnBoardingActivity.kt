package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

        private lateinit var binding: ActivityOnBoardingBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityOnBoardingBinding.inflate(layoutInflater)
            setContentView(binding.root)

         binding.getStartedBtn.setOnClickListener {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
        }
    }
}


