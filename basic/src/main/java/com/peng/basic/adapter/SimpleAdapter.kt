package com.peng.basic.adapter

import android.view.View
import com.peng.basic.base.BaseAdapter
import com.peng.basic.util.click

/**
 * 简单适配器
 */
abstract class SimpleAdapter<T> :
    BaseAdapter<T, SimpleViewHolder>() {

    var onItemClickListener: ((SimpleViewHolder, Int) -> Unit)? = null
    var onItemLongClickListener: ((SimpleViewHolder, Int) -> Boolean)? = null

    override fun onCreateViewHolder(itemView: View, viewType: Int): SimpleViewHolder {
        val holder = SimpleViewHolder(itemView)
        if (itemView.isClickable)
            itemView.click {
                itemClick(holder, holder.adapterPosition)
            }
        if (itemView.isLongClickable)
            itemView.setOnLongClickListener {
                return@setOnLongClickListener itemLongClick(
                    holder, holder.adapterPosition
                )
            }

        onViewHolderCreated(holder)
        return holder
    }

    open fun onViewHolderCreated(holder: SimpleViewHolder) {}

    private fun itemClick(holder: SimpleViewHolder, position: Int) {
        if (onItemClickListener == null) {
            onItemClickListener(holder, position)
        } else {
            onItemClickListener?.let {
                it(holder, position)
            }
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