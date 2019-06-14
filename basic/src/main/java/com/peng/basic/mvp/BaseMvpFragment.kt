package com.peng.basic.mvp

import android.content.Context
import com.peng.basic.base.BaseFragment
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