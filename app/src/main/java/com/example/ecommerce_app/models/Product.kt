package com.example.ecommerce_app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "products")
@Parcelize
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val price: Double,
    val imageResId: Int,
    val rating: Float,
    val categoryId: Int
) : Parcelable
