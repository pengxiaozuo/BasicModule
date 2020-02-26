package com.peng.basicmodule.mvvm

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.widget.editorActions
import com.peng.basic.base.BasicActivity
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.click
import com.peng.basic.util.toast
import com.peng.basicmodule.MainApp
import com.peng.basicmodule.R
import com.peng.basicmodule.data.DataResult
import com.peng.basicmodule.data.User
import com.peng.basicmodule.databinding.ActivityMvvmBinding
import kotlinx.android.synthetic.main.activity_mvvm.*

class MvvmActivity : BasicActivity() {

    private lateinit var binding: ActivityMvvmBinding
    private var dialog: ProgressDialog? = null
    private val viewModel: UserViewModel by lazy {
        initViewModel()
    }

    override fun initContentView(layout: Int) {
        binding = DataBindingUtil.setContentView(this, layout)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        dialog = ProgressDialog(this)
        viewModel.userLiveData.observe(this, Observer<DataResult<User>> {
            setUser(it)
        })

        btn_get.click { getUser() }.add()
        et_username.editorActions().subscribe { getUser() }.add()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun getLayoutId() = R.layout.activity_mvvm

    private fun initViewModel(): UserViewModel {
        return ViewModelProviders.of(this, (application as MainApp).viewModelFactory)
            .get(UserViewModel::class.java)
    }

    private fun getUser() {
        KeyboardUtils.hideSoftInput(this, btn_get)
        btn_get.clearFocus()
        showLoading("加载数据中...")
        viewModel.getUser()
    }

    private fun setUser(result: DataResult<User>?) {
        hideLoading()
        if (result?.success == true) {
            binding.user = result.data
        } else {
            binding.user = null
            toast("获取数据失败")
        }
    }


    private fun showLoading(msg: String?) {
        if (!isFinishing) {
            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            dialog = ProgressDialog.show(this, "", msg ?: "")
        }
    }

    private fun hideLoading() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }
}