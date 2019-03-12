package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.peng.basic.common.BaseActivity
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.LogUtils
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
        supportFragmentManager.beginTransaction().add(R.id.f_content, TestFragment(), "TestFragment").commit()
        KeyboardUtils.registerSoftInputChangedListener(this, object : KeyboardUtils.OnSoftInputChangedListener {
            override fun onSoftInputChanged(height: Int) {
                LogUtils.d("Height = $height")
            }

        })

        btn_input.setOnClickListener {
            if (KeyboardUtils.isSoftInputVisible(this)) {
                KeyboardUtils.hideSoftInput(this)
            } else {
                KeyboardUtils.showSoftInput(this)
            }
//            KeyboardUtils.toggleSoftInput(this)
        }
    }

    override fun initData() {

    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.unregisterSoftInputChangedListener(this)
    }

}
