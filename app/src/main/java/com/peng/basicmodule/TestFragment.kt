package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.peng.basic.common.BaseFragment
import com.peng.basic.util.LogUtils

class TestFragment : BaseFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_test
    }

    override fun initView(contentView: View, savedInstanceState: Bundle?) {
        LogUtils.d("initView-------")
    }

    override fun initData() {

    }
}