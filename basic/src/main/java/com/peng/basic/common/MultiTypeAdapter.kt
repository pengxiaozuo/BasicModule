package com.peng.basic.common

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class MultiTypeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var data: List<Any>? = null
    private val clazzs: ArrayList<Class<*>> = ArrayList()
    private val itemBinders: ArrayList<ItemViewBinder<*, *>> = ArrayList()
    private val typeBinds: HashMap<Class<*>, (Any) -> Int> = HashMap()

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
        else
            getItemViewTypeByItem(data!![position])
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
        var index = clazzs.indexOf(any::class.java)
        if (index < 0) {
            for (i in 0 until clazzs.size) {
                if (clazzs[i].isAssignableFrom(any::class.java)) {
                    index = i
                    break
                }
            }
        }
        if (index < 0) {
            throw RuntimeException("no found ${any::class.java} on register list")
        }

        val typeBinder = typeBinds[clazzs[index]]

        val toIndex = typeBinder?.invoke(any) ?: 0

        index += toIndex

        if (clazzs[index] != any::class.java && !clazzs[index].isAssignableFrom(any::class.java)) {
            throw RuntimeException("request class ${any::class.java} but found ${clazzs[index]}")
        }

        return index
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified VH : RecyclerView.ViewHolder> getItemViewBinderByViewHolder(holder: VH): ItemViewBinder<*, VH> {

        return itemBinders[holder.itemViewType] as ItemViewBinder<*, VH>
    }


    fun <T, VH : RecyclerView.ViewHolder> registerType(clazz: Class<T>, itemViewBinder: ItemViewBinder<T, VH>) {
        removeClassAndHolder(clazz)
        this.clazzs.add(clazz)
        this.itemBinders.add(itemViewBinder)
        itemViewBinder.adapter = this
    }

    fun <T, VH : RecyclerView.ViewHolder> registerType(
        clazz: Class<T>, vararg itemViewBinders: ItemViewBinder<T, VH>,
        typeBinder: (Any) -> Int
    ) {
        removeClassAndHolder(clazz)
        for (itemViewBinder in itemViewBinders) {
            this.clazzs.add(clazz)
            this.itemBinders.add(itemViewBinder)
            itemViewBinder.adapter = this
        }
        typeBinds[clazz] = typeBinder
    }

    private fun unregisterType(clazz: Class<*>) {
        typeBinds.remove(clazz)
        while (true) {
            val index = this.clazzs.indexOf(clazz)
            if (index > 0) {
                this.clazzs.removeAt(index)
                this.itemBinders.removeAt(index)
            } else {
                break
            }
        }
    }

    private fun removeClassAndHolder(clazz: Class<*>) {
        unregisterType(clazz)
    }
}