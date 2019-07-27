package com.peng.basic.adapter

import android.view.View
import com.peng.basic.base.BasicAdapter
import com.peng.basic.util.click

/**
 * 简单适配器
 */
abstract class SimpleAdapter<T> :
    BasicAdapter<T, SimpleViewHolder>() {

    var onItemClickListener: ((SimpleViewHolder, T) -> Unit)? = null
    var onItemLongClickListener: ((SimpleViewHolder, T) -> Boolean)? = null

    override fun onCreateViewHolder(itemView: View, viewType: Int): SimpleViewHolder {
        val holder = SimpleViewHolder(itemView)
        initListener(holder)
        onViewHolderCreated(holder)
        return holder
    }

    protected fun initListener(holder: SimpleViewHolder) {
        val itemView = holder.itemView
        if (itemView.isClickable)
            itemView.click {
                itemClick(holder, getItem(holder.adapterPosition))
            }
        if (itemView.isLongClickable)
            itemView.setOnLongClickListener {
                return@setOnLongClickListener itemLongClick(
                    holder, getItem(holder.adapterPosition)
                )
            }
    }

    open fun onViewHolderCreated(holder: SimpleViewHolder) {}

    private fun itemClick(holder: SimpleViewHolder, item: T) {
        if (onItemClickListener == null) {
            onItemClickListener(holder, item)
        } else {
            onItemClickListener?.let {
                it(holder, item)
            }
        }
    }

    open fun onItemClickListener(holder: SimpleViewHolder, item: T) {}

    private fun itemLongClick(holder: SimpleViewHolder, item: T): Boolean {
        var result = false
        onItemLongClickListener?.let {
            result = it(holder, item)
        }
        if (result) return result
        result = onItemLongClickListener(holder, item)
        return result
    }

    open fun onItemLongClickListener(holder: SimpleViewHolder, item: T): Boolean {
        return false
    }

}