package com.peng.basic.mvp

import android.os.Bundle
import com.peng.basic.base.BaseActivity
import javax.inject.Inject

abstract class BaseMvpActivity<P : IPresenter> : BaseActivity(), IView {

    var presenter: P? = null
        @Inject set

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

    override fun hideLoading() {

    }

    override fun showLoading() {
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
