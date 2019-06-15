package com.peng.basic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

interface ItemViewBinder<T, VH : RecyclerView.ViewHolder> {

    var adapter: MultiTypeAdapter?

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH

    @Suppress("UNCHECKED_CAST")
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

    abstract fun onBinder(any: Any): Boolean

}