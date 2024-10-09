package com.example.ecommerce_app.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ecommerce_app.R
import java.util.Locale

class language_setting : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,

        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        loadLanguagePreference()

        val languageMenu: ImageView = view.findViewById(R.id.language_menu)
        languageMenu.setOnClickListener {
            val popup = PopupMenu(requireContext(), languageMenu)
            popup.menuInflater.inflate(R.menu.language, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.language_english -> {
                        setLocale("en")
                        saveLanguagePreference("en")
                        Toast.makeText(requireContext(), "English Selected", Toast.LENGTH_SHORT).show()
                        requireActivity().recreate()
                        true
                    }
                    R.id.language_arabic -> {
                        setLocale("ar")
                        saveLanguagePreference("ar")
                        Toast.makeText(requireContext(), "العربية Selected", Toast.LENGTH_SHORT).show()
                        requireActivity().recreate()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        return view
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun saveLanguagePreference(language: String) {
        val sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("My_Language", language)
        editor.apply()
    }

    private fun loadLanguagePreference() {
        val sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("My_Language", "en")
        setLocale(language ?: "en")
    }

}
