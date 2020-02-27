package com.peng.basic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ItemViewBinder<T, VH : RecyclerView.ViewHolder> {

    var adapter: MultiTypeAdapter?

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    /**
     * 当[MultiTypeAdapter.dataFrom] 是 [MultiTypeAdapter.DataFrom.Data] 时, [data]为[MultiTypeAdapter.data], [position] 基于整个列表
     *
     * 当[MultiTypeAdapter.dataFrom] 是 [MultiTypeAdapter.DataFrom.ItemViewBinder] 时, [data]为[ItemViewBinder.getDataFromItemViewBinder], [position] 基于[ItemViewBinder.getDataFromItemViewBinder]自身
     */
    @Suppress("UNCHECKED_CAST")
    open fun onBinderViewHolder(holder: VH, data: List<*>, position: Int) {
        val item = data[position]
        onBinderViewHolder(item as T, holder)
    }

    abstract fun onBinderViewHolder(item: T, holder: VH)

    open fun onFailedToRecycleView(holder: VH) = false

    open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {}

    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {}

    open fun onViewRecycled(holder: VH) {}

    open fun onViewAttachedToWindow(holder: VH) {}

    open fun onViewDetachedFromWindow(holder: VH) {}

    open fun getDataFromItemViewBinder(): List<T>? = null

    open fun getDataFromItemViewBinderItemCount() = getDataFromItemViewBinder()?.size ?: 0

    abstract fun onBinder(any: Any): Boolean

}