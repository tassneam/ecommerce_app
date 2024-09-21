package com.example.ecommerce_app.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecommerce_app.databinding.FragmentDressesBinding

class DressesFragment : Fragment() {
    lateinit var binding: FragmentDressesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDressesBinding.inflate(layoutInflater)
        return binding.root
    }

}