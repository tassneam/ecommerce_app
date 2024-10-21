package com.example.ecommerce_app.models


data class FavouriteData(
    var id: String = "",
    var imageUrl: String = "",
    var title: String = "",
    var price: Double = 0.0,
    var rating: Float = 0f,
    var ratingCount: Int = 0,
    var isFavorite: Boolean= false
)
