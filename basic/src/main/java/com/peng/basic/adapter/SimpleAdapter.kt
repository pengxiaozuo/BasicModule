package com.peng.basic.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peng.basic.base.BasicAdapter
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle
import com.peng.basic.util.click
import com.peng.basic.util.longClick

/**
 * 简单适配器
 */
abstract class SimpleAdapter<T> :
    BasicAdapter<T, SimpleViewHolder>(), ILifecycle by DefaultLifecycle() {

    /**
     * Item点击事件，需要itemView的isClickable设置为true，优先级比重写onItemClickListener方法高，
     * 如果onItemClickListener不为null则不会回调重写的Item点击事件方法
     */
    var onItemClickListener: ((SimpleViewHolder, T) -> Unit)? = null
    /**
     * Item长按事件，需要itemView的isLongClickable设置为true，优先级比重写onItemLongClickListener高，
     * 如果此方法返回true则不会回调重写的Item长按事件
     */
    var onItemLongClickListener: ((SimpleViewHolder, T) -> Boolean)? = null

    /**
     * Item中子view的点击事件接收,需要在onBinderViewHolder方法中调用holder.setOnChildClickListener(id),
     * 优先级比重写onItemChildClickListener方法高，如果此变量不为null则不会回调onItemChildClickListener方法
     */
    var onItemChildClickListener: ((SimpleViewHolder, View, T) -> Unit)? = null
    /**
     * Item中子view的长按事件接收,需要在onBinderViewHolder方法中调用holder.setOnChildLongClickListener(id),
     * 优先级比重写onItemChildLongClickListener方法高，如果此变量不为null，并且返回true则不会回调onItemChildLongClickListener方法
     */
    var onItemChildLongClickListener: ((SimpleViewHolder, View, T) -> Boolean)? = null

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
            }.add()
        if (itemView.isLongClickable)
            itemView.longClick { itemLongClick(holder, getItem(holder.adapterPosition)) }.add()
        holder.onChildClickListener = ::itemChildClick
        holder.onChildLongClickListener = ::itemChildLongClick
    }

    /**
     * SimpleViewHolder 创建并初始化完毕，如果要修改SimpleViewHolder 可以重写此方法
     */
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

    /**
     * 重写此方法获取接收item点击事件，需要itemView的isClickable设置为true，、
     * 优先级比onItemClickListener变量低，如果onItemClickListener变量不为null则不会回调此方法
     */
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

    /**
     * 重写此方法接收item长按事件，需要itemView的isLongClickable设置为true，优先级比onItemLongClickListener变量低，
     * 如果onItemLongClickListener变量不为null，并且返回true则不会回调此方法
     */
    open fun onItemLongClickListener(holder: SimpleViewHolder, item: T): Boolean {
        return false
    }

    private fun itemChildClick(holder: SimpleViewHolder, view: View) {
        val item = getItem(holder.adapterPosition)
        if (onItemChildClickListener == null) {
            onItemChildClickListener(holder, view, item)
        } else {
            onItemChildClickListener?.let {
                it(holder, view, item)
            }
        }
    }

    /**
     * 重写此方法接收Item中子view的点击事件,需要在onBinderViewHolder方法中调用holder.setOnChildClickListener(id),
     * 优先级比onItemChildLongClickListener变量低，如果onItemChildLongClickListener变量不为null并且返回true则不会回调此方法
     */
    open fun onItemChildClickListener(holder: SimpleViewHolder, view: View, item: T) {}

    private fun itemChildLongClick(holder: SimpleViewHolder, view: View): Boolean {
        val item = getItem(holder.adapterPosition)
        var result = false
        onItemChildLongClickListener?.let {
            result = it(holder, view, item)
        }
        if (result) return result
        result = onItemChildLongClickListener(holder, view, item)
        return result
    }

    /**
     * 重写此方法接收Item中子view的长按事件,需要在onBinderViewHolder方法中调用holder.setOnChildLongClickListener(id),
     * 优先级比onItemChildClickListener变量低，如果onItemChildClickListener变量不为null则不会回调此方法
     */
    open fun onItemChildLongClickListener(holder: SimpleViewHolder, view: View, item: T): Boolean {
        return false
    }

    override fun onViewRecycled(holder: SimpleViewHolder) {
        super.onViewRecycled(holder)
        holder.clearHolder()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        clear()
    }
}