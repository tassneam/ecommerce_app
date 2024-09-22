package com.example.ecommerce_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Find the NavController from the NavHostFragment
        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }
                R.id.action_searchFragment -> {
                    if (navController.currentDestination?.id != R.id.searchFragment) {
                        navController.navigate(R.id.searchFragment)
                    }
                    true
                }
                R.id.action_cartFragment -> {
                    // Handle navigation for cartFragment
                    // navController.navigate(R.id.cartFragment)
                    true
                }
                R.id.action_favouriteFragment -> {
                    // Handle navigation for favouriteFragment
                    // navController.navigate(R.id.favouriteFragment)
                    true
                }
                R.id.action_profileFragment -> {
                    // Handle navigation for profileFragment
                    // navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }
}
