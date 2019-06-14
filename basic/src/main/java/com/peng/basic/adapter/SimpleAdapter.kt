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
    var onItemLongClickListener: ((SimpleViewHolder, Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
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

    override fun getItemCount(): Int {
        return data.size
    }

}