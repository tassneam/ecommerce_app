package com.example.ecommerce_app.fragments.shopping

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.activities.BillingAndAddresses
import com.example.ecommerce_app.activities.Cart
import com.example.ecommerce_app.activities.MainActivity
import com.example.ecommerce_app.activities.SignInActivity
import com.example.ecommerce_app.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import com.google.firebase.database.*

class SettingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSettingBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the user data from Firebase and display the username
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            fetchUsername(uid)
        }

        // Logout action
        binding.logoutLayout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        // Language selection action
        binding.languageMenu.setOnClickListener {
            showLanguagePopup(it)
        }

        // Navigate to Cart
        binding.allOrders.setOnClickListener {
            val intent = Intent(requireContext(), Cart::class.java)
            startActivity(intent)
        }

        // Navigate to Profile
        binding.myprofile.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_profileFragment)
        }

        // Navigate to Billing and Addresses
        binding.goBillingAndAddresses.setOnClickListener {
            val intent = Intent(requireContext(), BillingAndAddresses::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUsername(uid: String) {
        // Database path where the user information is stored
        val userRef = database.child("users").child(uid)

        // Add a listener to retrieve the user data
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("username").getValue(String::class.java)
                    username?.let {
                        binding.profileName.text = it
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors, such as logging the error
            }
        })
    }

    private fun showLanguagePopup(view: View) {
        // Create a PopupMenu to show language options
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.language, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.language_english -> {
                    changeLanguage("en")
                    true
                }
                R.id.language_arabic -> {
                    changeLanguage("ar")
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun changeLanguage(languageCode: String) {
        // Save selected language to SharedPreferences
        with(sharedPreferences.edit()) {
            putString("selected_language", languageCode)
            apply()
        }

        // Set the locale and restart the activity to apply the change
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart the activity to apply language changes
        val refreshIntent = Intent(requireContext(), MainActivity::class.java)
        refreshIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(refreshIntent)
        requireActivity().finish()
    }
}
