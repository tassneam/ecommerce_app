package com.example.ecommerce_app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "reviews")
@Parcelize
data class Review(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var description: String,
    var picUrl: String,
    var rating: Double,
    var itemId: Int
) : Parcelable
