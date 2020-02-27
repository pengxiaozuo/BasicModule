package com.peng.basic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 复杂类型适配器
 */
class MultiTypeAdapter(val dataFrom: DataFrom = DataFrom.Data) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Any>? = null
    private val itemBinders: ArrayList<ItemViewBinder<*, *>> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return itemBinders[viewType].onCreateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (dataFrom == DataFrom.Data) {
            getItemViewBinderByViewHolder(holder).onBinderViewHolder(holder, data!!, position)
        } else {
            val itemViewBinder = getItemViewBinderByViewHolder(holder)
            itemViewBinder.onBinderViewHolder(
                holder,
                itemViewBinder.getDataFromItemViewBinder()!!,
                getItemViewBinderPosition(position)
            )
        }
    }

    override fun getItemCount(): Int {
        return when (dataFrom) {
            DataFrom.Data -> data?.size ?: 0
            DataFrom.ItemViewBinder -> {
                var size = 0
                itemBinders.forEach {
                    size += it.getDataFromItemViewBinderItemCount()
                }
                size
            }
        }
    }


    override fun getItemViewType(position: Int): Int {

        if (dataFrom == DataFrom.Data) {

            return if (data == null)
                super.getItemViewType(position)
            else {
                getItemViewTypeByItem(data!![position])
            }
        } else {
            return getItemViewTypeByPosition(position)
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

    private fun getItemViewTypeByPosition(position: Int): Int {
        val itemViewBinder = getItemViewBinderByPosition(position)
        val index = itemBinders.indexOf(itemViewBinder)

        if (index < 0) {
            throw RuntimeException("no found $position on register list")
        }
        return index
    }

    private fun getItemViewBinderPosition(position: Int): Int {
        var preEndPosition = -1
        var itemViewBinderPosition = -1
        for (itemBinder in itemBinders) {
            val size = itemBinder.getDataFromItemViewBinderItemCount()
            if (position in (preEndPosition + 1)..(preEndPosition + size)) {
                itemViewBinderPosition = position - preEndPosition - 1
                break
            }
            preEndPosition += size
        }

        return itemViewBinderPosition
    }

    private fun getItemViewBinderByPosition(position: Int): ItemViewBinder<*, *> {
        var preEndPosition = -1
        var itemViewBinder: ItemViewBinder<*, *>? = null
        for (itemBinder in itemBinders) {
            val size = itemBinder.getDataFromItemViewBinderItemCount()
            if (position in (preEndPosition + 1)..(preEndPosition + size)) {
                itemViewBinder = itemBinder
                break
            }
            preEndPosition += size
        }

        if (itemViewBinder == null) {
            throw RuntimeException("get itemViewBinder by position is null")
        }
        return itemViewBinder
    }

    fun getItem(position: Int): Any? {
        return if (dataFrom == DataFrom.Data) data?.get(position)
        else getItemViewBinderByPosition(position)
            .getDataFromItemViewBinder()
            ?.get(getItemViewBinderPosition(position))
    }

    private fun getItemViewTypeByItem(any: Any): Int {

        val itemViewBinder = itemBinders.firstOrNull {
            it.onBinder(any)
        }
        val index = itemBinders.indexOf(itemViewBinder)

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

    enum class DataFrom {
        Data, ItemViewBinder
    }
}