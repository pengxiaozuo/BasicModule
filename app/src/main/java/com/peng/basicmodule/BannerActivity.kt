package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.peng.basic.common.BaseActivity
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BaseActivity() {
    override fun configUi(config: UiConfig) {
        config.layoutId = R.layout.activity_banner
    }

    override fun initView(contentView: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        val data = listOf(
            "http://img3.3lian.com/2013/c3/62/d/48.jpg",
            "http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg",
            "http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg",
            "http://up.enterdesk.com/edpic_source/60/e8/be/60e8bee2be7ee1cb65c44d68bcb693e0.jpg"
        )
        val adapter = object : BannerView.Adapter() {
            override fun onCreateView(any: Any): View {
                val view = ImageView(this@BannerActivity)
                view.scaleType = ImageView.ScaleType.CENTER_CROP
                return view
            }

            override fun onBindView(view: View, any: Any) {
                Picasso.get().load(any as String).into(view as ImageView)
            }
        }

        adapter.data = data

        banner.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }
}