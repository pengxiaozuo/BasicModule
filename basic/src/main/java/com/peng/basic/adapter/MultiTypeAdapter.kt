package com.peng.basic.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.peng.basic.util.LogUtils
import com.peng.basic.util.logd

/**
 * 复杂类型适配器
 */
class MultiTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val TAG = this.javaClass.simpleName

    var data: List<Any>? = null
    private val itemBinders: ArrayList<ItemViewBinder<*, *>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return itemBinders[viewType].onCreateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItemViewBinderByViewHolder(holder).onBinderViewHolder(holder, data!!, position)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (data == null)
            super.getItemViewType(position)
        else {
            getItemViewTypeByItem(data!![position])
        }
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return getItemViewBinderByViewHolder(holder).onFailedToRecycleView(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        itemBinders.forEach {
            it.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemBinders.forEach {
            it.onDetachedFromRecyclerView(recyclerView)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        getItemViewBinderByViewHolder(holder).onViewRecycled(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        getItemViewBinderByViewHolder(holder).onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        getItemViewBinderByViewHolder(holder).onViewDetachedFromWindow(holder)
    }


    private fun getItemViewTypeByItem(any: Any): Int {

        val itemViewBinder = itemBinders.firstOrNull {
            it.onBinder(any)
        }
        var index = itemBinders.indexOf(itemViewBinder)

        if (index < 0) {
            throw RuntimeException("no found ${any::class.java.simpleName} on register list")
        }
        return index
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified VH : RecyclerView.ViewHolder> getItemViewBinderByViewHolder(holder: VH): ItemViewBinder<*, VH> {

        return itemBinders[holder.itemViewType] as ItemViewBinder<*, VH>
    }


    fun <T, VH : RecyclerView.ViewHolder> registerType(itemViewBinder: ItemViewBinder<T, VH>) {
        unregisterType(itemViewBinder)
        this.itemBinders.add(itemViewBinder)
        itemViewBinder.adapter = this
    }


    private fun <T, VH : RecyclerView.ViewHolder> unregisterType(itemViewBinder: ItemViewBinder<T, VH>) {
        itemBinders.remove(itemViewBinder)
    }

}