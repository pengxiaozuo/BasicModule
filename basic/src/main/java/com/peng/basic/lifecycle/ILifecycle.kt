package com.peng.basic.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

interface ILifecycle : CoroutineScope, LifecycleObserver {

    fun Disposable.add() {
        addDisposable(this)
    }

    fun Disposable.remove() {
        unDisposable(this)
    }

    fun addDisposable(d: Disposable)

    fun unDisposable(d: Disposable)

    fun clear()
}

class DefaultLifecycle : ILifecycle, CoroutineScope by MainScope() {
    private var compositeDisposable: CompositeDisposable? = null

    override fun addDisposable(d: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.addAll(d)
    }

    override fun unDisposable(d: Disposable) {
        compositeDisposable?.remove(d)
    }


    override fun clear() {
        try {
            compositeDisposable?.clear()
            cancel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onLifecycleOwnerDestroy() {
        clear()
    }
}