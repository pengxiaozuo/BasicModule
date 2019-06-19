package com.peng.basic.mvp

import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle

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