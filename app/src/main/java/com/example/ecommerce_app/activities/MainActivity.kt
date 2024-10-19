package com.example.ecommerce_app.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Load the saved language preference
        val languageCode = sharedPreferences.getString("selected_language", "en")
        languageCode?.let { setLocale(it) }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.action_cartFragment) {
                val intent = Intent(this, Cart::class.java)
                startActivity(intent)
                true
            } else {
                navController.navigate(item.itemId)
                true
            }
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
