package com.peng.basicmodule.mvp

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.peng.basic.mvp.BaseMvpActivity
import com.peng.basic.util.click
import com.peng.basic.util.toast
import com.peng.basicmodule.R
import com.peng.basicmodule.di.DaggerMVPComponent
import kotlinx.android.synthetic.main.activity_mvp.*

class MVPActivity : BaseMvpActivity<MVPPresenter>(), MVPContract.View {

    private var dialog: ProgressDialog? = null
    override fun inject() {
        DaggerMVPComponent.create().inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_mvp
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        dialog = ProgressDialog(this)
        tv_get.click {
            presenter?.getUser()
        }
    }

    override fun getUserSuccess(user: String) {
        tv_get.text = user
    }

    override fun getUserError(msg: String?) {
        toast(msg)
    }

    override fun initData() {
    }

    override fun showLoading(msg: String?) {
        super.showLoading(msg)
        if (!isFinishing) {

            if (dialog?.isShowing == true){
                dialog?.dismiss()
            }
            dialog = ProgressDialog.show(this,"",msg?:"")

        }
    }

    override fun hideLoading() {
        super.hideLoading()
        if (dialog?.isShowing == true){
            dialog?.dismiss()
        }
    }
}