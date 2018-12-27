package com.peng.basic.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class ItemViewBinder<T, VH : RecyclerView.ViewHolder> {

    lateinit var adapter: MultiTypeAdapter

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    open fun onBinderViewHolder(holder: VH, data: List<Any>, position: Int) {
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

}