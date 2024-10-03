package com.example.ecommerce_app.models


data class Item(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val rating: Float = 0.0f,
    val ratingCount: Int = 0
)
