package com.example.ecommerce_app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "categories")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val imageResId: Int
) : Parcelable
