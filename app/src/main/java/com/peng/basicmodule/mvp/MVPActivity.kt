package com.peng.basicmodule.mvp

import android.os.Bundle
import android.view.View
import com.peng.basic.mvp.BaseMvpActivity
import com.peng.basicmodule.R
import com.peng.basicmodule.di.DaggerMVPComponent

class MVPActivity : BaseMvpActivity<MVPPresenter>(), MVPConceract.View {

    override fun inject() {
        DaggerMVPComponent.create().inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
    }

    override fun getUserSuccess() {
    }

    override fun getUserError() {
    }

    override fun initData() {
        presenter?.getUser()
    }
}