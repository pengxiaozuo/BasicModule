package com.peng.basic.mvp

import android.os.Bundle
import com.peng.basic.base.BasicActivity
import javax.inject.Inject

abstract class BasicMvpActivity<P : IPresenter> : BasicActivity(), IView {

    var presenter: P? = null
        @Inject set(value) {
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        presenter?.takeView(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        presenter?.dropView()
        super.onDestroy()
    }

    abstract fun inject()

}
