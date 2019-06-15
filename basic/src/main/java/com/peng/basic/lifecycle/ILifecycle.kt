package com.peng.basic.lifecycle

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

interface ILifecycle : CoroutineScope {

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

class DefaultLifecycle : ILifecycle, CoroutineScope by CoroutineScope(Dispatchers.Default) {
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
        compositeDisposable?.clear()
        cancel()
    }


}