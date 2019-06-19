package com.peng.basicmodule.binds

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["android:url"])
fun ImageView.load(url: String?) {
    Picasso.get().load(url).into(this)
}

