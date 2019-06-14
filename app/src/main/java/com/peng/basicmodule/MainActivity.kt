package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.peng.basic.base.BaseActivity
import com.peng.basic.util.KeyboardUtils
import com.peng.basic.util.click
import com.peng.basicmodule.mvp.MVPActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        btn_multi_type.setOnClickListener {
            startActivity(MultiTypeActivity::class.java)
        }

        btn_banner.click {
            startActivity(BannerActivity::class.java)
        }
        supportFragmentManager.beginTransaction().add(R.id.f_content, TestFragment(), "TestFragment").commit()
        KeyboardUtils.registerSoftInputChangedListener(this, object : KeyboardUtils.OnSoftInputChangedListener {
            override fun onSoftInputChanged(height: Int) {
            }

        })

        btn_input.click {
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
