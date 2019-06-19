package com.peng.basic.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle

abstract class BaseFragment : Fragment(), ILifecycle by DefaultLifecycle() {
    protected val TAG = this.javaClass.simpleName
    protected var activity: Activity? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as Activity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initParams()
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }


    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化view
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int


    /**
     * 初始化arg
     */
    open fun initParams() {}


    @JvmOverloads
    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null) {
        activity?.let {
            val intent = Intent(it, clazz)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }
    }


    override fun onDetach() {
        clear()
        activity = null
        super.onDetach()
    }
}