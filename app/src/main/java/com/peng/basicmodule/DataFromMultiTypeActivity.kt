package com.peng.basicmodule

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.peng.basic.adapter.MultiTypeAdapter
import com.peng.basic.adapter.SimpleAdapter
import com.peng.basic.adapter.SimpleViewHolder
import com.peng.basic.base.BaseActivity
import com.peng.basic.util.toast
import com.peng.basic.widget.banner.BannerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_multitype.*

class DataFromMultiTypeActivity : BaseActivity() {
    private val mAdapter = MultiTypeAdapter(MultiTypeAdapter.DataFrom.ItemViewBinder)
    override fun initView(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
    }

    override fun initData() {
        mAdapter.registerType(bannerItemViewBinder)
        mAdapter.registerType(stringItemViewBinder)
        recycler_view.adapter = mAdapter


        stringItemViewBinder.data = listOf(
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C",
            "A", "B", "C"
        )

        val itemBannerData = listOf(
            Banner("http://www.pptok.com/wp-content/uploads/2012/08/xunguang-7.jpg"),
            Banner("http://pic33.photophoto.cn/20141028/0038038006886895_b.jpg"),
            Banner("http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg"),
            Banner("https://p.ssl.qhimg.com/dmfd/400_300_/t010f807b18d13c16a9.jpg")
        )
        bannerItemViewBinder.data = listOf(BannerList(itemBannerData))

    }

    override fun getLayoutId() = R.layout.activity_multitype

    data class Banner(var url: String)


    private val bannerItemViewBinder = object : SimpleAdapter<BannerList>() {
        override fun getLayoutId() = R.layout.item_banner
        override fun onViewHolderCreated(holder: SimpleViewHolder) {
            super.onViewHolderCreated(holder)
            holder.getView<BannerView>(R.id.banner)?.let {
                val adapter = BannerAdapter()
                it.setAdapter(adapter)
                holder.any = adapter
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun onBinderViewHolder(item: BannerList, holder: SimpleViewHolder) {
            holder.getView<BannerView>(R.id.banner)?.let { banner ->
                holder.any?.let {
                    if (it is BannerView.Adapter<*>) {
                        val ad = it as BannerView.Adapter<Banner>
                        ad.data = item.data
                        ad.notifyDataSetChanged()
                    }
                }
                banner.setCurrentItem(item.lastItem, false)
            }
        }

        override fun onViewRecycled(holder: SimpleViewHolder) {
            super.onViewRecycled(holder)
            getItem(holder.adapterPosition).lastItem = holder.getView<BannerView>(R.id.banner)!!.getCurrentItem()
        }

    }

    private val stringItemViewBinder = object : SimpleAdapter<String>() {
        override fun getLayoutId(): Int {
            return R.layout.item_multi_type_string
        }

        override fun onBinderViewHolder(item: String, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item)
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: String) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item)
        }
    }


    data class BannerList(val data: List<Banner>) {
        var lastItem = 0
    }

    class BannerAdapter : BannerView.Adapter<Banner>() {

        override fun onCreateView(parent: BannerView, item: Banner): View {
            return View.inflate(parent.context, R.layout.item_banner_image, null)
        }

        override fun onBindView(view: View, item: Banner) {
            Picasso.get().load(item.url).into(view as ImageView)
        }

        override fun onPageSelected(view: View, position: Int) {
            super.onPageSelected(view, position)
        }
    }

}