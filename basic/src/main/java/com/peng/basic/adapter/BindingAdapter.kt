package com.peng.basic.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.View

abstract class BindingAdapter<T> : SimpleAdapter<T>() {

    override fun onCreateViewHolder(itemView: View, viewType: Int): SimpleViewHolder {
        val binding:ViewDataBinding? = DataBindingUtil.bind(itemView)
        assert(binding != null) { "ViewDataBinding is null" }
        val holder = SimpleViewHolder(binding!!)
        initListener(holder)
        onViewHolderCreated(holder)
        return holder
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBinderViewHolder(item: T, holder: SimpleViewHolder) {
        onBinderViewHolder(item, holder, holder.binding!!)
    }

    abstract fun onBinderViewHolder(item: T, holder: SimpleViewHolder, binding: ViewDataBinding)

}