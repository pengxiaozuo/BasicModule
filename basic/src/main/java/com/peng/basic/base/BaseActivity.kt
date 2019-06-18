package com.peng.basic.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle
import com.peng.basic.util.ActivityCacheUtils
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.ToastUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


abstract class BaseActivity : AppCompatActivity(), ILifecycle by DefaultLifecycle() {

    protected val TAG = this.javaClass.simpleName
    private var contextWR: WeakReference<Activity>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        setContentView(getLayoutId())

        contextWR = WeakReference(this)
        ActivityCacheUtils.pushTask(contextWR!!)

        val view = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        initView(view, savedInstanceState)

        initData()
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
    abstract fun initData()

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