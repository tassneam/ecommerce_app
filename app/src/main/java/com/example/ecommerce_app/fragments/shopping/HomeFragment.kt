package com.example.ecommerce_app.fragments.shopping

import FavoritesAdapter
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerce_app.R
import com.example.ecommerce_app.activities.FavoriteItemsActivity
//import com.example.ecommerce_app.adapters.FavoritesAdapter
import com.example.ecommerce_app.adapters.HomeCategoriesAdapter
import com.example.ecommerce_app.adapters.ItemAdapter
import com.example.ecommerce_app.databinding.FragmentHomeBinding
import com.example.ecommerce_app.models.CartItem
import com.example.ecommerce_app.models.Category
import com.example.ecommerce_app.models.FavouriteData
import com.example.ecommerce_app.models.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeCategoriesAdapter: HomeCategoriesAdapter
    private lateinit var categories: List<Category>
    private var filteredCategories: MutableList<Category> = mutableListOf()
    lateinit var database: DatabaseReference

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var itemList: ArrayList<Item>

    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoriteItems: ArrayList<FavouriteData>

    companion object {
        private const val REQUEST_CODE_SPEECH_INPUT = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditTxt.isCursorVisible = false

        // Initialize the categories after the view is created
        categories = listOf(
            Category(getString(R.string.category_sale), R.drawable.sale),
            Category(getString(R.string.category_dresses), R.drawable.dress),
            Category(getString(R.string.category_tshirts), R.drawable.tshirt),
            Category(getString(R.string.category_pants), R.drawable.pants),
            Category(getString(R.string.category_jeans), R.drawable.jeans),
            Category(getString(R.string.category_see_all), R.drawable.ic_plus)
        )
        filteredCategories.addAll(categories)

        // Show progress bar
        binding.progressBarCategories.visibility = View.VISIBLE
        binding.recyclerViewCategories.visibility = View.GONE

        // Set up RecyclerView
        binding.recyclerViewCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        loadCategories()
        loadBannerImage()

        //////////////////////////////////
        itemList = ArrayList()
        // Initialize the adapter with item list and click listeners
        itemAdapter = ItemAdapter(
            context = requireContext(),
            itemList = itemList,
            onCartClick = { item ->
                addToCart(item) // Define this function to handle cart click
            },
            onFavoriteClick = { item ->
                addToFavorites(convertToFavouriteData(item)) // Define this function to handle favorite click
            }
        )
        // Set the adapter to the RecyclerView
        binding.recyclerViewCard.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        binding.recyclerViewCard.setHasFixedSize(true)
        binding.recyclerViewCard.adapter = itemAdapter
        getItemData()
        ///////////////////////////////

        binding.searchEditTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCategories(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        // Set up mic icon click listener
        binding.mic.setOnClickListener {
            startVoiceInput()
        }
    }

    // Function to start voice input
    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...")

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Speech recognition is not supported on this device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                binding.searchEditTxt.setText(result[0])
            }
        }
    }

    private fun filterCategories(query: String) {
        filteredCategories.clear()
        if (query.isEmpty()) {
            filteredCategories.addAll(categories)
        } else {
            val filtered = categories.filter { it.title.contains(query, ignoreCase = true) }
            filteredCategories.addAll(filtered)
        }
        homeCategoriesAdapter.notifyDataSetChanged()
    }

    private fun addToCart(item: Item) {
        val cartDatabase = FirebaseDatabase.getInstance().getReference("cartItems")
        cartDatabase.orderByChild("title").equalTo(item.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (cartItemSnapshot in snapshot.children) {
                            val existingCartItem = cartItemSnapshot.getValue(CartItem::class.java)
                            if (existingCartItem != null) {
                                val newQuantity = existingCartItem.quantity + 1
                                cartItemSnapshot.ref.child("quantity").setValue(newQuantity)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Item quantity updated", Toast.LENGTH_SHORT).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(requireContext(), "Failed to update item: ${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    } else {
                        val cartItemId = cartDatabase.push().key
                        if (cartItemId != null) {
                            val newCartItem = CartItem(
                                id = cartItemId,
                                title = item.title,
                                imageUrl = item.imageUrl,
                                price = item.price,
                                quantity = 1
                            )
                            cartDatabase.child(cartItemId).setValue(newCartItem)
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Item added to cart", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed to add item to cart: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to check cart: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getItemData() {
        database = FirebaseDatabase.getInstance().getReference("item")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        itemList.add(item!!)
                    }
                    binding.recyclerViewCard.adapter = itemAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadBannerImage() {
        val bannerDatabaseReference = FirebaseDatabase.getInstance().getReference("/banner/imageUrl")
        bannerDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val bannerUrl = snapshot.getValue(String::class.java)
                    if (bannerUrl != null) {
                        Log.d("HomeFragment", "Banner URL: $bannerUrl")
                        Glide.with(requireContext()).load(bannerUrl).into(binding.bannerImg)
                    } else {
                        Log.e("HomeFragment", "Banner URL is null")
                    }
                } else {
                    Log.e("HomeFragment", "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadCategories() {
        Handler(Looper.getMainLooper()).postDelayed({
            homeCategoriesAdapter = HomeCategoriesAdapter(filteredCategories) { category ->
                when (category.title) {
                    getString(R.string.category_sale) -> findNavController().navigate(R.id.action_homeFragment_to_saleFragment)
                    getString(R.string.category_dresses) -> findNavController().navigate(R.id.action_homeFragment_to_dressesFragment)
                    getString(R.string.category_tshirts) -> findNavController().navigate(R.id.action_homeFragment_to_tshirtsFragment)
                    getString(R.string.category_pants) -> findNavController().navigate(R.id.action_homeFragment_to_pantsFragment)
                    getString(R.string.category_jeans) -> findNavController().navigate(R.id.action_homeFragment_to_jeansFragment)
                    getString(R.string.category_see_all) -> findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
                }
            }
            binding.recyclerViewCategories.adapter = homeCategoriesAdapter

            // Hide progress bar and show categories
            binding.progressBarCategories.visibility = View.GONE
            binding.recyclerViewCategories.visibility = View.VISIBLE
        }, 1000)
    }

    private fun addToFavorites(item: FavouriteData) {
        val favoritesDatabase = FirebaseDatabase.getInstance().getReference("favoriteItems")
        favoritesDatabase.orderByChild("title").equalTo(item.title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(requireContext(), "Item is already in favorites", Toast.LENGTH_SHORT).show()
                    } else {
                        val favoriteItemId = favoritesDatabase.push().key
                        if (favoriteItemId != null) {
                            val newFavoriteItem = FavouriteData(
                                id = favoriteItemId,
                                imageUrl = item.imageUrl,
                                title = item.title,
                                price = item.price,
                                rating = item.rating,
                                ratingCount = item.ratingCount,
                                isFavorite = true
                            )
                            favoritesDatabase.child(favoriteItemId).setValue(newFavoriteItem)
                                .addOnSuccessListener {
                                    if (isAdded) {
                                        Toast.makeText(requireContext(), "Item added to favorites", Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), "Failed to add item to favorites: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to add to favorites: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    private fun convertToFavouriteData(item: Item): FavouriteData {
        return FavouriteData(
            id = item.id, // Assuming `Item` has an `id` field, otherwise generate a unique ID
            imageUrl = item.imageUrl,
            title = item.title,
            price = item.price,
            rating = item.rating, // Assuming `Item` has a `rating` field
            ratingCount = item.ratingCount, // Assuming `Item` has a `ratingCount` field
            isFavorite = true // Mark it as a favorite
        )
    }
}