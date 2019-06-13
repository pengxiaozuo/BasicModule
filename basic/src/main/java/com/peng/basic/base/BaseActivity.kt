package com.peng.basic.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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


abstract class BaseActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Default) {

    protected val TAG = this.javaClass.simpleName
    private var contextWR: WeakReference<Activity>? = null
    protected var compositeDisposable: CompositeDisposable? = null


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
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 初始化视图
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 布局layout id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化Intent中存储的数据
     */
    open fun initParams() {}


    @JvmOverloads
    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        ToastUtils.showToast(this, msg, duration)
    }

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
        clearDisposable()
        cancel()
        super.onDestroy()
    }


    fun Disposable.add() {
        addDisposable(this)
    }

    fun Disposable.remove() {
        unDisposable(this)
    }


    fun addDisposable(d: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.addAll(d)
    }

    fun unDisposable(d: Disposable) {
        compositeDisposable?.remove(d)
    }

    fun clearDisposable() {
        compositeDisposable?.clear()
    }
}