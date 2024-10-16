package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the NavController from the NavHostFragment
        val navController = findNavController(R.id.shoppingHostFragment)

        // Set up the bottom navigation with the NavController
        binding.bottomNavigation.setupWithNavController(navController)

        // Handle CartActivity separately since it's not part of the nav graph
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.action_cartFragment) {
                val intent = Intent(this, Cart::class.java)
                startActivity(intent)
                true
            } else {
                // Allow the navigation to continue for other fragments
                navController.navigate(item.itemId)
                true
            }
        }
    }
}
