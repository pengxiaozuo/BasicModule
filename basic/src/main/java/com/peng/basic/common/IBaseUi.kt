package com.peng.basic.common

import android.os.Bundle
import android.view.View

interface IBaseUi {

    fun initParams()

    fun initView(contentView: View, savedInstanceState: Bundle?)

    fun initData()

}