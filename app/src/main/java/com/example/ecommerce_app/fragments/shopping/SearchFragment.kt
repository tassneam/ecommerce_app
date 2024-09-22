package com.example.ecommerce_app.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerce_app.R
import com.example.ecommerce_app.adapters.CategoryAdapter
import com.example.ecommerce_app.adapters.HomeCategoriesAdapter
import com.example.ecommerce_app.databinding.FragmentSearchBinding
import com.example.ecommerce_app.models.Category
import com.google.android.material.internal.ViewUtils.hideKeyboard


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    lateinit var categoryAdapter: CategoryAdapter
    private val categories = listOf(
        Category("Sale", R.drawable.sale),
        Category("Dresses", R.drawable.dress),
        Category("T-shirts", R.drawable.tshirt),
        Category("Pants", R.drawable.pants),
        Category("Jeans", R.drawable.jeans)
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add TextWatcher to EditText
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.cancelButton.visibility = View.INVISIBLE
                } else {
                    binding.cancelButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.cancelButton.setOnClickListener {
            binding.searchEditText.text.clear()
            hideKeyboard()

        }

        binding.recyclerViewCategoriescards.layoutManager = GridLayoutManager(context, 2)
        categoryAdapter = CategoryAdapter(categories) { category ->
            when (category.title) {
                "Sale" -> findNavController().navigate(R.id.action_searchFragment_to_saleFragment)
                "Dresses" -> findNavController().navigate(R.id.action_searchFragment_to_dressesFragment)
                "T-shirts" -> findNavController().navigate(R.id.action_searchFragment_to_tshirtsFragment)
                "Pants" -> findNavController().navigate(R.id.action_searchFragment_to_pantsFragment)
                "Jeans" -> findNavController().navigate(R.id.action_searchFragment_to_jeansFragment)
            }
        }
        binding.recyclerViewCategoriescards.adapter = categoryAdapter

    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocusView = requireActivity().currentFocus
        currentFocusView?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}
