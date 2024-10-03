package com.example.ecommerce_app.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ecommerce_app.R


fun getProgessDrawable(c: Context): CircularProgressDrawable {
    return CircularProgressDrawable(c).apply {
        strokeWidth = 5f
        centerRadius = 40f
        start()
    }
}

//set images
fun ImageView.loadImage(uri: String?, progressDawable: CircularProgressDrawable) {
    val option = RequestOptions().placeholder(progressDawable)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        view.loadImage(url, getProgessDrawable(view.context))
    } else {
        // Set a placeholder image if the URL is null or empty
        view.setImageResource(R.drawable.avatar)
    }
}
