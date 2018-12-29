package com.peng.basic.common

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

open class SimpleViewHolder(private val contentView: View) : RecyclerView.ViewHolder(contentView) {

    private val views: SparseArray<View> = SparseArray()

    var any:Any? = null

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T? {
        var view = views[id]
        if (view == null) {
            view = contentView.findViewById(id)
            if (view == null) {
                return null
            } else {
                views.put(id, view)
            }
        }
        return view as T
    }

    fun setText(id: Int, value: CharSequence): SimpleViewHolder {
        getView<TextView>(id)?.text = value
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): SimpleViewHolder {
        getView<ImageView>(viewId)?.setImageResource(resId)
        return this
    }

    fun setImageBitmap(id: Int, bitmap: Bitmap): SimpleViewHolder {
        getView<ImageView>(id)?.setImageBitmap(bitmap)
        return this
    }

    fun setVisibility(id: Int, visible: Int): SimpleViewHolder {
        getView<View>(id)?.visibility = visible
        return this
    }

}