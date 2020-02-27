package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.peng.basic.base.BasicActivity
import com.peng.basic.util.BarUtils
import com.peng.basic.util.dp2px
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_banner.*

class BannerActivity : BasicActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_banner
    }

    private var data = arrayListOf<String>()

    private val adapter = object : BannerView.Adapter<String>() {

        override fun onCreateView(parent: BannerView, item: String): View {
            return View.inflate(this@BannerActivity, R.layout.item_banner_card, null)
        }

        override fun onBindView(view: View, item: String) {
            val iv = view.findViewById<ImageView>(R.id.iv)
            Picasso.get().load(item).into(iv)
        }

        override fun onPageSelected(view: View, position: Int) {
            super.onPageSelected(view, position)
        }
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        BarUtils.setTransparentMode(this, true)
        val pageMargin = dp2px(5f).toInt()
        val pd = dp2px(30f).toInt()
        banner.setPageMargin(pd, 0, pd, 0, pageMargin)
        banner.setInterpolator(LinearInterpolator(), 1000)
        banner.setPageTransformer(ScalePageTransformer(0.6f))
    }

    override fun initData(savedInstanceState: Bundle?) {
        adapter.data = data
        banner.setAdapter(adapter)
        adapter.notifyDataSetChanged()

        data.add("https://n.sinaimg.cn/news/transform/700/w1000h500/20190619/1cf2-hyrtarw1088593.jpg")
        data.add("https://n.sinaimg.cn/photo/700/w1000h500/20190618/0503-hyrtarv6539136.jpg")
        data.add("https://p.ssl.qhimg.com/dmfd/400_300_/t010f807b18d13c16a9.jpg")
        data.add("http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg")
        data.add("http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg")

        adapter.notifyDataSetChanged()
    }

    class ScalePageTransformer(
        private var minScale: Float
    ) : ViewPager.PageTransformer {

        override fun transformPage(page: View, position: Float) {
            val size = when {
                position < -1 -> minScale
                position >= -1 && position < 0 -> minScale + (1 - minScale) * (1 + position)
                position < 1 -> minScale + (1 - minScale) * (1 - position)
                else -> minScale
            }
            page.scaleY = size
        }

    }
}