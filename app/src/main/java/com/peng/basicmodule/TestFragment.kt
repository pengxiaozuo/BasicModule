package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import com.peng.basic.base.BaseFragment
import java.util.concurrent.TimeUnit

class TestFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun initView(contentView: View, savedInstanceState: Bundle?) {
        contentView.clicks().throttleFirst(2,TimeUnit.SECONDS).subscribe()
    }

    override fun initData() {

    }
}