package com.peng.basicmodule.mvp

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.widget.editorActions
import com.peng.basic.mvp.BasicMvpActivity
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.click
import com.peng.basic.util.toast
import com.peng.basicmodule.MainApp
import com.peng.basicmodule.R
import com.peng.basicmodule.data.User
import com.peng.basicmodule.di.DaggerActivityComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mvp.*

class MVPActivity : BasicMvpActivity<MVPContract.Presenter>(), MVPContract.View {

    private var dialog: ProgressDialog? = null
    override fun inject() {
        DaggerActivityComponent.builder().appComponent((application as MainApp).appComponent).build().inject(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_mvp
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        dialog = ProgressDialog(this)
        btn_get.click {
            getUser()
        }
        et_username.editorActions().subscribe { getUser() }.add()
    }

    private fun getUser() {
        KeyboardUtils.hideSoftInput(this, btn_get)
        btn_get.clearFocus()
        presenter?.getUser(et_username.text.toString())
    }

    override fun getUserSuccess(user: User) {
        tv_result.text = user.name
        Picasso.get().load(user.avatarUrl).into(iv_avatar)
    }

    override fun getUserError(msg: String?) {
        tv_result.text = msg ?: "net error"
        iv_avatar.setImageDrawable(null)
        toast(msg)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun showLoading(msg: String?) {
        super.showLoading(msg)
        if (!isFinishing) {

            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            dialog = ProgressDialog.show(this, "", msg ?: "")

        }
    }

    override fun hideLoading() {
        super.hideLoading()
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}