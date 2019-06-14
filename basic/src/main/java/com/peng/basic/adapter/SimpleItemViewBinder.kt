package com.peng.basic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.peng.basic.util.TypeUtils
import com.peng.basic.util.click
import com.peng.basic.util.logd
import java.lang.ClassCastException
import java.lang.Exception
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType

abstract class SimpleItemViewBinder<T>(val layout: Int) : ItemViewBinder<T, SimpleViewHolder>() {
    protected val TAG = this.javaClass.simpleName

    var onItemClickListener: ((SimpleViewHolder, Int) -> Unit)? = null
    var onItemLongClickListener: ((SimpleViewHolder, Int) -> Boolean)? = null

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SimpleViewHolder {
        val view = inflater.inflate(layout, parent, false)
        val holder = SimpleViewHolder(view)
        if (view.isClickable)
            view.click {
                itemClick(holder, holder.adapterPosition)
            }
        if (view.isLongClickable)
            view.setOnLongClickListener {
                return@setOnLongClickListener itemLongClick(
                    holder, holder.adapterPosition
                )
            }
        onCreatedViewHolder(holder)
        return holder
    }

    open fun onCreatedViewHolder(viewHolder: SimpleViewHolder) {}
    private fun itemClick(holder: SimpleViewHolder, position: Int) {
        onItemClickListener(holder, position)
        onItemClickListener?.let {
            it(holder, position)
        }
    }

    override fun onBinder(any: Any): Boolean {
        return try {
            val genType =
                TypeUtils.getRawType((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0])
            val anyType = TypeUtils.getRawType(any::class.java)
            genType.isAssignableFrom(anyType)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    open fun onItemClickListener(holder: SimpleViewHolder, position: Int) {}

    private fun itemLongClick(holder: SimpleViewHolder, position: Int): Boolean {

        var result = false
        onItemLongClickListener?.let {
            result = it(holder, position)
        }
        if (result) return result
        result = onItemLongClickListener(holder, position)
        return result
    }

    open fun onItemLongClickListener(holder: SimpleViewHolder, position: Int): Boolean {
        return false
    }
}