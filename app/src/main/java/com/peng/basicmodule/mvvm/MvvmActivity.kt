package com.peng.basicmodule.mvvm

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.click
import com.peng.basic.util.toast
import com.peng.basicmodule.R
import com.peng.basicmodule.data.DataResult
import com.peng.basicmodule.data.User
import com.peng.basicmodule.databinding.ActivityMvvmBinding
import kotlinx.android.synthetic.main.activity_mvvm.*

class MvvmActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityMvvmBinding
    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        binding.viewModel = userViewModel
        dialog = ProgressDialog(this)
        userViewModel.userLiveData.observe(this, Observer<DataResult<User>> {
            hideLoading()
            if (it?.success == true) {
                binding.user = it.data
            } else {
                toast("获取数据失败")
            }
        })

        btn_get.click {
            KeyboardUtils.hideSoftInput(this, btn_get)
            btn_get.clearFocus()
            showLoading("加载数据中...")
            userViewModel.getUser()
        }
    }


    fun showLoading(msg: String?) {
        if (!isFinishing) {

            if (dialog?.isShowing == true) {
                dialog?.dismiss()
            }
            dialog = ProgressDialog.show(this, "", msg ?: "")

        }
    }

    fun hideLoading() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
        }
    }

}