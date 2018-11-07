package com.peng.basic.widget.banner

import android.support.v4.view.PagerAdapter
import android.view.View

class BannerAdapter(var vies: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object`
    }

    override fun getCount(): Int {
        return vies.size
    }

}