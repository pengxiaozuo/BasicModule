package com.peng.basic.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle
import com.peng.basic.util.click
import com.peng.basic.util.longClick

open class SimpleViewHolder(contentView: View) : RecyclerView.ViewHolder(contentView),
    ILifecycle by DefaultLifecycle() {

    private val views: SparseArray<View> = SparseArray()
    private val clickables: SparseArray<Boolean> = SparseArray()
    private val longClickables: SparseArray<Boolean> = SparseArray()
    var binding: ViewDataBinding? = null

    constructor(binding: ViewDataBinding) : this(binding.root) {
        this.binding = binding
    }

    val context: Context = contentView.context
    var any: Any? = null
    internal var onChildClickListener: ((SimpleViewHolder, View) -> Unit)? = null
    internal var onChildLongClickListener: ((SimpleViewHolder, View) -> Boolean)? = null

    fun setOnChildClickListener(id: Int) {
        getView<View>(id).let { v ->
            clickables.put(id, v.isClickable)
            v.click { onChildClickListener?.invoke(this, v) }.add()
        }
    }

    fun setOnChildLongClickListener(id: Int) {
        getView<View>(id).let { v ->
            longClickables.put(id, v.isLongClickable)
            v.longClick { onChildLongClickListener?.invoke(this, v) ?: false }.add()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T {
        var view = views[id]
        if (view == null) {
            view = itemView.findViewById(id)
            if (view == null) {
                throw IllegalArgumentException("not found view")
            } else {
                views.put(id, view)
            }
        }
        return view as T
    }

    fun getTextView(id: Int): TextView {
        return getView(id)
    }

    fun getImageView(id: Int): ImageView {
        return getView(id)
    }

    fun getEditText(id: Int): EditText {
        return getView(id)
    }

    fun getButton(id: Int): Button {
        return getView(id)
    }

    fun setText(id: Int, value: CharSequence): SimpleViewHolder {
        getView<TextView>(id).text = value
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): SimpleViewHolder {
        getView<ImageView>(viewId).setImageResource(resId)
        return this
    }

    fun setImageBitmap(id: Int, bitmap: Bitmap): SimpleViewHolder {
        getView<ImageView>(id).setImageBitmap(bitmap)
        return this
    }

    fun setVisibility(id: Int, visible: Int): SimpleViewHolder {
        getView<View>(id).visibility = visible
        return this
    }

    fun clearHolder() {
        clear()
        for (i in 0 until clickables.size()) {
            val id = clickables.keyAt(i)
            val value = clickables.valueAt(i)
            getView<View>(id).isClickable = value
        }
        clickables.clear()

        for (i in 0 until longClickables.size()) {
            val id = longClickables.keyAt(i)
            val value = longClickables.valueAt(i)
            getView<View>(id).isLongClickable = value
        }
        longClickables.clear()
    }
}