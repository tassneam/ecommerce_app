package com.example.ecommerce_app.models

import android.os.Parcel
import android.os.Parcelable

// Add the no-argument constructor
data class Item(
    val imageUrl: String = "",
    val title: String = "",
    val price: Double = 0.0,
    val rating: Float = 0f,
    val ratingCount: Int = 0
) : Parcelable {
    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readFloat(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(title)
        parcel.writeDouble(price)
        parcel.writeFloat(rating)
        parcel.writeInt(ratingCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
