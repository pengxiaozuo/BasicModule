package com.peng.basic.mvp

import android.content.Context
import com.peng.basic.base.BasicFragment
import javax.inject.Inject

abstract class BasicMvpFragment<P : IPresenter> : BasicFragment(), IView {

    var presenter: P? = null
        @Inject set(value) {
            field = value
        }

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

    override fun hideLoading() {

    }

    override fun showLoading(msg: String?) {

    }

    override fun showEmptyLayout() {
    }

    override fun showErrorLayout() {
    }

    override fun onEmptyRetry() {
    }

    override fun onErrorRetry() {
    }

    override fun hideEmptyAndErrorLayout() {
    }
}