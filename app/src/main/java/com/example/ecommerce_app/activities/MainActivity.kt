package com.example.ecommerce_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}