package com.peng.basic.common

import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * 简单适配器
 */
abstract class SimpleAdapter<T>(protected var data: List<T>, val layout: Int) :
        RecyclerView.Adapter<SimpleViewHolder>() {


    var onItemClickListener: ((SimpleViewHolder, Int, T) -> Unit)? = null
    var onLongItemClickListener: ((SimpleViewHolder, Int, T) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = SimpleViewHolder(view)
        view.setOnClickListener {
            onItemClickListener(holder, holder.adapterPosition, data[holder.adapterPosition])
        }

        view.setOnLongClickListener {
            return@setOnLongClickListener onLongItemClickListener(holder, holder.adapterPosition,
                    data[holder.adapterPosition])

        }

        return holder
    }

    @CallSuper
    open fun onItemClickListener(holder: SimpleViewHolder, position: Int, t: T) {
        onItemClickListener?.let {
            it(holder, position, t)
        }
    }

    @CallSuper
    open fun onLongItemClickListener(holder: SimpleViewHolder, position: Int, t: T): Boolean {
        var result = false
        onLongItemClickListener?.let {
            result = it(holder, position, t)
        }
        return result
    }

    override fun getItemCount(): Int {
        return data.size
    }

}