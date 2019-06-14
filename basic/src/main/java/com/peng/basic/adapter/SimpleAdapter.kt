package com.peng.basic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.peng.basic.util.click

/**
 * 简单适配器
 */
abstract class SimpleAdapter<T>(var data: List<T>, val layout: Int) :
        RecyclerView.Adapter<SimpleViewHolder>() {

    protected val TAG = this.javaClass.simpleName

    var onItemClickListener: ((SimpleViewHolder, Int) -> Unit)? = null
    var onLongItemClickListener: ((SimpleViewHolder, Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = SimpleViewHolder(view)
        if (view.isClickable)
            view.click {
                setOnItemClickListener(holder, holder.adapterPosition)
            }
        if (view.isLongClickable)
            view.setOnLongClickListener {
                return@setOnLongClickListener setOnLongItemClickListener(
                        holder, holder.adapterPosition
                )
            }

        return holder
    }

    private fun setOnItemClickListener(holder: SimpleViewHolder, position: Int) {
        onItemClickListener(holder, position)
        onItemClickListener?.let {
            it(holder, position)
        }
    }

    open fun onItemClickListener(holder: SimpleViewHolder, position: Int) {}

    private fun setOnLongItemClickListener(holder: SimpleViewHolder, position: Int): Boolean {
        var result = false
        result = onLongItemClickListener(holder, position)
        onLongItemClickListener?.let {
            result = it(holder, position)
        }
        return result
    }

    open fun onLongItemClickListener(holder: SimpleViewHolder, position: Int): Boolean {
        return false
    }

    override fun getItemCount(): Int {
        return data.size
    }

}