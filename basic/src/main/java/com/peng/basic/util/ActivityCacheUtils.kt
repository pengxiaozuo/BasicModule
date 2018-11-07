package com.peng.basic.util

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*


/**
 * Activity 缓存工具
 */
object ActivityCacheUtils {

    private var activitys: Stack<WeakReference<Activity>>? = null

    /**
     * 缓存Activity
     */
    fun pushTask(task: WeakReference<Activity>) {
        if (activitys == null) {
            activitys = Stack()
        }
        activitys!!.push(task)
    }

    /**
     * 将传入的Activity对象从缓存中移除
     */
    fun removeTask(task: WeakReference<Activity>) {
        activitys?.remove(task)
    }

    /**
     * 根据指定位置从缓存中移除Activity
     */
    fun removeTask(taskIndex: Int) {
        if (taskIndex > 0 && taskIndex <= activitys?.lastIndex ?: 0 && activitys != null) {
            activitys?.removeAt(taskIndex)
        }
    }

    /**
     * 移除缓存顶部之外的其他缓存对象
     */
    fun removeToTop() {
        if (activitys == null) {
            return
        }
        val end = activitys!!.size
        val start = 1
        for (i in end - 1 downTo start) {
            val activity = activitys!![i].get()
            if (null != activity && !activity.isFinishing) {
                activity.finish()
            }
        }
    }

    /**
     * 移除全部
     */
    fun removeAll() {
        //finish所有的Activity
        if (activitys == null) return
        for (task in activitys!!) {
            val activity = task.get()
            if (null != activity && !activity.isFinishing) {
                activity.finish()
            }
        }
    }
}