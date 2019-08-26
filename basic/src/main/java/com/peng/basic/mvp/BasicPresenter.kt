package com.peng.basic.mvp

import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle

open class BasicPresenter<V : IView> : IPresenter, ILifecycle by DefaultLifecycle() {

    protected val TAG = this.javaClass.simpleName

    protected var view: V? = null

    @Suppress("UNCHECKED_CAST")
    override fun takeView(view: IView) {
        this.view = view as V
    }

    override fun dropView() {
        this.view = null
        clear()
    }
}