package com.example.ecommerce_app.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.ecommerce_app.R

class DetailsPagerAdapter(private val context: Context, fragmentManager: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.description)
            1 -> context.getString(R.string.reviews)
            else -> null
        }
    }

}
