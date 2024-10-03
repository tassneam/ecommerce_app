package com.example.ecommerce_app.Data

data class CartItem(
    val photoResId: Int,   // Resource ID for the product image
    val title: String,     // Product title
    val price: Double,     // Product price
    var quantity: Int      // Product quantity
)
