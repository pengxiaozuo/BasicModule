package com.peng.basicmodule.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["url", "placeholder"])
fun ImageView.load(url: String?, placeholder: Drawable?) {
    if (url.isNullOrEmpty()) {
        setImageDrawable(null)
        setBackgroundDrawable(null)
    } else {
        Picasso.get().load(url).also {
            if (placeholder != null) {
                it.placeholder(placeholder)
            }
        }.into(this)
    }
}

