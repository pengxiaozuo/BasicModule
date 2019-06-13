package com.peng.basic.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

open class BasePresenter<V : IView> : IPresenter, CoroutineScope by CoroutineScope(Dispatchers.Default) {

    protected val TAG = this.javaClass.simpleName

    protected var compositeDisposable: CompositeDisposable? = null

    protected var view: V? = null

    override fun takeView(view: IView) {
        this.view = view as V
    }

    override fun dropView() {
        this.view = null
        clearDisposable()
        cancel()
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