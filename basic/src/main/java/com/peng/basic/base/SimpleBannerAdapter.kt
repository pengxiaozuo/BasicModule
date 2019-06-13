package com.peng.basic.base

import android.view.LayoutInflater
import android.view.View
import com.peng.basic.widget.banner.BannerView

abstract class SimpleBannerAdapter<T> : BannerView.Adapter<T>() {
    override fun onCreateView(parent: BannerView, item: T): View {
        return LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
    }

    abstract fun getLayoutId(): Int
}