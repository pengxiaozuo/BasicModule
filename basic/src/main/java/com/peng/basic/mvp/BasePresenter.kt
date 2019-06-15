package com.peng.basic.mvp

import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

open class BasePresenter<V : IView> : IPresenter, ILifecycle by DefaultLifecycle() {

    protected val TAG = this.javaClass.simpleName

    protected var view: V? = null

    override fun takeView(view: IView) {
        this.view = view as V
    }

    override fun dropView() {
        this.view = null
        clear()
    }
}