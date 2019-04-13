package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.peng.basic.common.BaseActivity
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BaseActivity() {

    private var data = arrayListOf<String>()

    private val adapter = object : BannerView.Adapter() {

        override fun onCreateView(parent: BannerView, any: Any): View {
            return View.inflate(this@BannerActivity, R.layout.item_banner_image, null)
        }

        override fun onBindView(view: View, position: Int) {
            val any = data!![position]
            Picasso.get().load(any as String).into(view as ImageView)
        }
    }

    override fun configUi(config: UiConfig) {
        config.layoutId = R.layout.activity_banner
        config.statusBarTransparentMode = true
        config.fullScreen = true
        config.statusDarkMode = true
    }


    override fun initView(contentView: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        adapter.data = data
        banner.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        data.add(
            "http://www.pptok.com/wp-content/uploads/2012/08/xunguang-7.jpg")
        data.add(
            "http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg")
        adapter.notifyDataSetChanged()
        data.add(
            "http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg")
        data.add(
            "http://up.enterdesk.com/edpic_source/60/e8/be/60e8bee2be7ee1cb65c44d68bcb693e0.jpg")
        adapter.notifyDataSetChanged()

    }

}