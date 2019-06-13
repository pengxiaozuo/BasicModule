package com.peng.basic.base

import android.content.Context
import com.peng.basic.mvp.IPresenter
import com.peng.basic.mvp.IView
import javax.inject.Inject

abstract class BaseMvpFragment<P : IPresenter> : BaseFragment(), IView {

    var presenter: P? = null
        @Inject set

    override fun onAttach(context: Context?) {
        inject()
        presenter?.takeView(this)
        super.onAttach(context)
    }


    override fun onDetach() {

        presenter?.dropView()
        super.onDetach()
    }

    abstract fun inject()
}