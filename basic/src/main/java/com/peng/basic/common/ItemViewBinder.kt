package com.peng.basic.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class ItemViewBinder<T, VM : RecyclerView.ViewHolder> {

    lateinit var adapter: MultiTypeAdapter

    abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VM

    open fun onBinderViewHolder(holder: RecyclerView.ViewHolder, data: List<Any>, position: Int) {
        val item = data[position]
        onBinderViewHolder(item as T, holder as VM)
    }

    abstract fun onBinderViewHolder(item: T, holder: VM)


}