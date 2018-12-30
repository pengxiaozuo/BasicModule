package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.peng.basic.common.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : BaseActivity() {
    override fun configUi(config: UiConfig) {
        config.layoutId = R.layout.activity_main
    }

    override fun initView(contentView: View, savedInstanceState: Bundle?) {
        btn_multi_type.setOnClickListener {
            startActivity(MultiTypeActivity::class.java)
        }

        btn_banner.setOnClickListener {
            startActivity(BannerActivity::class.java)
        }
    }

    override fun initData() {

    }

}
