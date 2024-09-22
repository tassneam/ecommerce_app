package com.example.ecommerce_app.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity(tableName = "categories")
@Parcelize
data class Category(
    val title: String,
    val imageResId: Int
) : Parcelable
