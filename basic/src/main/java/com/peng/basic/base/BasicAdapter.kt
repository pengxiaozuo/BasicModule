package com.peng.basic.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.peng.basic.adapter.ItemViewBinder
import com.peng.basic.adapter.MultiTypeAdapter
import com.peng.basic.util.TypeUtils
import java.lang.reflect.ParameterizedType

/**
 * [RecyclerView.Adapter]适配器封装，或多类型[ItemViewBinder]封装
 * 当[BasicAdapter]当做[RecyclerView.Adapter]使用时[data]生效
 * 如果[BasicAdapter]做[ItemViewBinder]时data为[MultiTypeAdapter]中的数据
 */
abstract class BasicAdapter<T, VM : RecyclerView.ViewHolder>(var data: List<T> = listOf<T>()) :
    RecyclerView.Adapter<VM>(),
    ItemViewBinder<T, VM> {

    override var adapter: MultiTypeAdapter? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VM =
        onCreateViewHolder(LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false), viewType)

    override fun onViewDetachedFromWindow(holder: VM) {
        super<RecyclerView.Adapter>.onViewDetachedFromWindow(holder)
    }

    //RecyclerView.Adapter
    override fun getItemCount() = data.size

    override fun onFailedToRecycleView(holder: VM): Boolean {
        return super<RecyclerView.Adapter>.onFailedToRecycleView(holder)
    }

    //RecyclerView.Adapter
    override fun onBindViewHolder(holder: VM, position: Int) {
        val item = data[position]
        onBinderViewHolder(item, holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super<RecyclerView.Adapter>.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super<RecyclerView.Adapter>.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewRecycled(holder: VM) {
        super<RecyclerView.Adapter>.onViewRecycled(holder)

    }

    override fun onViewAttachedToWindow(holder: VM) {
        super<RecyclerView.Adapter>.onViewAttachedToWindow(holder)
    }


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VM =
        onCreateViewHolder(inflater.inflate(getLayoutId(), parent, false), 0)

    /**
     * 当做[ItemViewBinder]时[viewType]无效
     */
    abstract fun onCreateViewHolder(itemView: View, viewType: Int): VM

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun getDataFromItemViewBinder(): List<T>? {
        return data
    }

    @Suppress("UNCHECKED_CAST")
    fun getItem(position: Int): T {
        if (adapter == null) {
            return data[position]
        } else {
            return adapter!!.getItem(position) as T
        }
    }

    override fun onBinder(any: Any): Boolean {
        return try {
            val genType =
                TypeUtils.getRawType((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0])
            val anyType = TypeUtils.getRawType(any::class.java)
            genType.isAssignableFrom(anyType)
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

}