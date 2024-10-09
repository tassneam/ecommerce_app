package com.example.ecommerce_app.models


data class CartItem(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    var quantity: Int = 1
)
