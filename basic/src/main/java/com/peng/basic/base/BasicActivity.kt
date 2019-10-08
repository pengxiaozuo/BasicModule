package com.peng.basic.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle
import com.peng.basic.util.ActivityCacheUtils
import java.lang.ref.WeakReference


abstract class BasicActivity : AppCompatActivity(), ILifecycle by DefaultLifecycle() {

    protected val TAG = this.javaClass.simpleName
    private var contextWR: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        initContentView(getLayoutId())

        contextWR = WeakReference(this)
        ActivityCacheUtils.pushTask(contextWR!!)

        val view = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        initView(view, savedInstanceState)

        initData()
        initData(savedInstanceState)
    }

    open fun initContentView(@LayoutRes layout: Int) {
        setContentView(layout)
    }

    /**
     * 初始化Intent中存储的数据
     */
    open fun initParams() {}

    /**
     * 初始化视图
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    @Deprecated("")
    open fun initData() {}

    fun initData(savedInstanceState: Bundle?) {}

    /**
     * 布局layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int


    @JvmOverloads
    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        contextWR?.let {
            ActivityCacheUtils.removeTask(it)
        }
        clear()
        super.onDestroy()
    }

}