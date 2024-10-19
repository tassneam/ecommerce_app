package com.example.ecommerce_app.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.CategoryAdapter
import com.example.ecommerce_app.databinding.FragmentSearchBinding
import com.example.ecommerce_app.models.Category

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var categoryAdapter: CategoryAdapter
    private var filteredCategories: MutableList<Category> = mutableListOf()
    private lateinit var categories: List<Category> // Moved initialization to onViewCreated

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize categories from string resources
        categories = listOf(
            Category(getString(R.string.category_sale), R.drawable.sale),
            Category(getString(R.string.category_dresses), R.drawable.dress),
            Category(getString(R.string.category_tshirts), R.drawable.tshirt),
            Category(getString(R.string.category_pants), R.drawable.pants),
            Category(getString(R.string.category_jeans), R.drawable.jeans)
        )
        filteredCategories.addAll(categories)

        // Setup the RecyclerView with the filtered categories
        binding.recyclerViewCategoriescards.layoutManager = GridLayoutManager(context, 2)
        categoryAdapter = CategoryAdapter(filteredCategories) { category ->
            when (category.title) {
                getString(R.string.category_sale) -> findNavController().navigate(R.id.action_searchFragment_to_saleFragment)
                getString(R.string.category_dresses) -> findNavController().navigate(R.id.action_searchFragment_to_dressesFragment)
                getString(R.string.category_tshirts) -> findNavController().navigate(R.id.action_searchFragment_to_tshirtsFragment)
                getString(R.string.category_pants) -> findNavController().navigate(R.id.action_searchFragment_to_pantsFragment)
                getString(R.string.category_jeans) -> findNavController().navigate(R.id.action_searchFragment_to_jeansFragment)
            }
        }
        binding.recyclerViewCategoriescards.adapter = categoryAdapter

        // Add TextWatcher to EditText
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCategories(s.toString())
                binding.cancelButton.visibility = if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.cancelButton.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()
            filterCategories("") // Show all categories
        }
    }

    private fun filterCategories(query: String) {
        filteredCategories.clear()
        if (query.isEmpty()) {
            filteredCategories.addAll(categories) // Show all categories if query is empty
        } else {
            val filtered = categories.filter { it.title.contains(query, ignoreCase = true) }
            filteredCategories.addAll(filtered) // Add filtered categories
        }
        categoryAdapter.notifyDataSetChanged() // Notify the adapter to refresh the list
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
