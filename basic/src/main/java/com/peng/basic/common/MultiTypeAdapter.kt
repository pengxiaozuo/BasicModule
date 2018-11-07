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
        val viewType = getItemViewType(position)
        itemBinders[viewType].onBinderViewHolder(holder, data!!, position)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {

        return if (data == null)
            super.getItemViewType(position)
        else
            getItemViewTypeForItem(data!![position])
    }

    private fun getItemViewTypeForItem(any: Any): Int {
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


    fun <T, VM : RecyclerView.ViewHolder> registerType(clazz: Class<T>, itemViewBinder: ItemViewBinder<T, VM>) {
        removeClassAndHolder(clazz)
        this.clazzs.add(clazz)
        this.itemBinders.add(itemViewBinder)
        itemViewBinder.adapter = this
    }

    fun <T, VM : RecyclerView.ViewHolder> registerType(clazz: Class<T>, vararg itemViewBinders: ItemViewBinder<T, VM>,
                                                       typeBinder: (Any) -> Int) {
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