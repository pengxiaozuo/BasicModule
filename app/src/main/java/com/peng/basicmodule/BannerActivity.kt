package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.peng.basic.base.BaseActivity
import com.peng.basic.util.BarUtils
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_banner
    }

    private var data = arrayListOf<String>()

    private val adapter = object : BannerView.Adapter<String>() {

        override fun onCreateView(parent: BannerView, any: String): View {
            return View.inflate(this@BannerActivity, R.layout.item_banner_image, null)
        }

        override fun onBindView(view: View, position: Int) {
            val any = data!![position]
            Picasso.get().load(any as String).into(view as ImageView)
        }

        override fun onPageSelected(view: View, position: Int) {
            super.onPageSelected(view, position)
        }
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        BarUtils.setTransparentMode(this, true)
    }

    override fun initData() {
        adapter.data = data
        banner.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        if (data.isEmpty()) {
            data.add("http://www.pptok.com/wp-content/uploads/2012/08/xunguang-7.jpg")
            data.add("http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg")
            data.add("https://p.ssl.qhimg.com/dmfd/400_300_/t010f807b18d13c16a9.jpg")
            data.add("http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg")
            adapter.notifyDataSetChanged()
        }

    }

}