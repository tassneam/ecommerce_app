package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.FragmentTshirtsBinding

class TshirtsFragment : Fragment() {
    lateinit var binding: FragmentTshirtsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTshirtsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sort.setOnClickListener {
            showSortOptions(it)
        }
    }

    private fun showSortOptions(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.sort_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.sort_high_to_low -> {
                    sortTshirtsByPriceHighToLow()
                    true
                }
                R.id.sort_low_to_high -> {
                    sortTshirtsByPriceLowToHigh()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    private fun sortTshirtsByPriceHighToLow() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }

    private fun sortTshirtsByPriceLowToHigh() {
        // Implement sorting logic here
        // E.g., update your RecyclerView adapter with sorted data
    }
}
