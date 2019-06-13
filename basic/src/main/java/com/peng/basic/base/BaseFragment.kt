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
import android.widget.Toast
import com.peng.basic.util.ToastUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseFragment : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.Default) {
    protected val TAG = this.javaClass.simpleName
    protected var activity: Activity? = null
    protected var compositeDisposable: CompositeDisposable? = null


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


    /**
     * 显示吐司
     */
    @JvmOverloads
    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        activity?.let {
            ToastUtils.showToast(it, msg, duration)
        }
    }

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
        super.onDetach()
        clearDisposable()
        cancel()
        activity = null
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