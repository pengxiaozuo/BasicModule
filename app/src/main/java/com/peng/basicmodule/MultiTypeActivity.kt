package com.peng.basicmodule

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.peng.basic.adapter.BindingAdapter
import com.peng.basic.adapter.MultiTypeAdapter
import com.peng.basic.adapter.SimpleAdapter
import com.peng.basic.adapter.SimpleViewHolder
import com.peng.basic.base.BasicActivity
import com.peng.basic.util.toast
import com.peng.basicmodule.databinding.ItemMultiTypeUserMaleBindingBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_multitype.*


class MultiTypeActivity : BasicActivity() {

    private val mAdapter = MultiTypeAdapter()
    private val mListData = mutableListOf<Any>()


    override fun getLayoutId(): Int {
        return R.layout.activity_multitype
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
    }

    val strList = arrayListOf<String>("A", "B", "C")
    val userList = listOf(User("OK", 12, Sex.FEMALE), User("OK", 12, Sex.FEMALE), User("NoOK", 12, Sex.MALE))
    val imageList = listOf(
        Image("https://p.ssl.qhimg.com/dmfd/400_300_/t010f807b18d13c16a9.jpg"),
        Image("https://p.ssl.qhimg.com/dmfd/400_300_/t0120b2f23b554b8402.jpg"),
        Image("https://p.ssl.qhimg.com/dmfd/400_300_/t01d5786ff848892dfe.jpg"),
        Image("https://p.ssl.qhimg.com/dmfd/365_365_/t01143b496faeb476d0.jpg"),
        Image("https://p.ssl.qhimg.com/dmfd/420_207_/t01cabe2d0967540783.jpg"),
        Image("http://www.pptok.com/wp-content/uploads/2012/08/xunguang-7.jpg"),
        Image("http://img2.imgtn.bdimg.com/it/u=2309772032,1565890452&fm=200&gp=0.jpg")
    )

    override fun initData() {
        mAdapter.registerType(StringTypeItemViewBinder())
        mAdapter.registerType(UserFemaleItemViewBinder())
        mAdapter.registerType(UserMaleItemViewBinder())
        mAdapter.registerType(ListStringViewBinder())
        mAdapter.registerType(ListUserViewBinder())
        mAdapter.registerType(ListImageAdapter())
        mAdapter.data = mListData
        recycler_view.adapter = mAdapter


        for (i in 0..20) {
            mListData.add("String Type Index $i")
            mListData.add(
                User(
                    "Jame $i",
                    i + 10,
                    if (i % 2 == 0) {
                        Sex.MALE
                    } else Sex.FEMALE
                )
            )
            mListData.add(ImageList(imageList))
        }

        mListData.add(StringList(strList))
        mListData.add(UserList(userList))
        mAdapter.notifyDataSetChanged()
    }

    class StringTypeItemViewBinder : SimpleAdapter<String>() {
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

    data class StringList(var data: List<String>)
    data class UserList(var data: List<User>)
    data class User(var name: String, var age: Int, var sex: Sex)
    data class Image(var url: String)
    data class ImageList(var data: List<Image>) {
        var firstVisiblePosition: Int = 0
        var offset = 0
    }

    enum class Sex(private val desc: String) {

        MALE("男的"), FEMALE("女的");

        override fun toString(): String {
            return desc
        }
    }

    class UserMaleItemViewBinder : BindingAdapter<User>() {
        override fun getLayoutId(): Int {
            return R.layout.item_multi_type_user_male_binding
        }

        override fun onBinder(any: Any): Boolean {
            return any is User && any.sex == Sex.MALE
        }

        override fun onBinderViewHolder(
            item: User,
            holder: SimpleViewHolder,
            binding: ViewDataBinding
        ) {
            if(binding is ItemMultiTypeUserMaleBindingBinding) {
                binding.user = item
                binding.executePendingBindings()
            }
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: User) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }
    }

    class UserFemaleItemViewBinder : SimpleAdapter<User>() {
        override fun getLayoutId(): Int {
            return R.layout.item_multi_type_user_female
        }

        override fun onBinder(any: Any): Boolean {
            return any is User && any.sex == Sex.FEMALE
        }


        override fun onBinderViewHolder(item: User, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_age, item.age.toString())
            holder.setText(R.id.tv_sex, item.sex.toString())
            if (item.age == 11) {
                holder.setOnChildClickListener(R.id.iv_header)
            }
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: User) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }

        override fun onItemChildClickListener(holder: SimpleViewHolder, view: View, item: User) {
            super.onItemChildClickListener(holder, view, item)
            holder.context.toast("重写方法Header click:" + item.name)
        }

    }

    class ListStringViewBinder : SimpleAdapter<StringList>() {
        override fun getLayoutId(): Int {
            return R.layout.item_horizontal_recycler
        }

        override fun onViewHolderCreated(holder: SimpleViewHolder) {
            super.onViewHolderCreated(holder)
            holder.getView<RecyclerView>(R.id.recycler_view)?.also {
                it.layoutManager = LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false)
                it.adapter = StringTypeItemViewBinder()
                holder.any = it.adapter
            }
        }

        override fun onBinderViewHolder(item: StringList, holder: SimpleViewHolder) {

            (holder.any as StringTypeItemViewBinder).let {
                it.data = item.data
                it.notifyDataSetChanged()
            }

        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: StringList) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }
    }

    class ListUserViewBinder : SimpleAdapter<UserList>() {

        override fun getLayoutId(): Int {
            return R.layout.item_horizontal_recycler
        }

        override fun onViewHolderCreated(holder: SimpleViewHolder) {
            super.onViewHolderCreated(holder)
            holder.getView<RecyclerView>(R.id.recycler_view)?.also {
                it.layoutManager = LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false)
                val adapter = MultiTypeAdapter()
                adapter.registerType(UserMaleItemViewBinder())
                adapter.registerType(UserFemaleItemViewBinder())
                it.adapter = adapter
                holder.any = it.adapter
            }
        }

        override fun onBinderViewHolder(item: UserList, holder: SimpleViewHolder) {

            (holder.any as MultiTypeAdapter).let {
                it.data = item.data
                it.notifyDataSetChanged()
            }
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: UserList) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }
    }

    class ImageAdapter : SimpleAdapter<Image>() {
        override fun getLayoutId(): Int {
            return R.layout.item_image
        }

        override fun onBinderViewHolder(item: Image, holder: SimpleViewHolder) {
            holder.getView<ImageView>(R.id.iv)?.let {
                Picasso.get().load(item.url).into(it)
            }
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: Image) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }


    }

    class ListImageAdapter : SimpleAdapter<ImageList>() {
        override fun getLayoutId(): Int {
            return R.layout.item_horizontal_recycler
        }

        override fun onViewHolderCreated(holder: SimpleViewHolder) {
            super.onViewHolderCreated(holder)
            holder.getView<RecyclerView>(R.id.recycler_view)?.also {
                it.layoutManager = LinearLayoutManager(holder.context, LinearLayoutManager.HORIZONTAL, false)
                it.layoutManager?.onSaveInstanceState()
                it.adapter = ImageAdapter()
                holder.any = it.adapter
            }
        }

        override fun onBinderViewHolder(item: ImageList, holder: SimpleViewHolder) {

            (holder.any as ImageAdapter).let {
                it.data = item.data
                it.notifyDataSetChanged()
            }

            holder.getView<RecyclerView>(R.id.recycler_view)?.layoutManager?.let {
                if (it is LinearLayoutManager) {
                    it.scrollToPositionWithOffset(item.firstVisiblePosition, item.offset)
                }
            }
        }

        override fun onViewRecycled(holder: SimpleViewHolder) {
            adapter?.data?.getOrNull(holder.adapterPosition)?.let { item ->
                holder.getView<RecyclerView>(R.id.recycler_view)?.let { recyclerView ->
                    if (recyclerView.layoutManager is LinearLayoutManager && item is ImageList) {
                        item.firstVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager)
                            .findFirstVisibleItemPosition()
                        item.offset = recyclerView.getChildAt(0)?.x?.toInt() ?: 0
                    }
                }
            }
            super.onViewRecycled(holder)
        }

        override fun onItemClickListener(holder: SimpleViewHolder, item: ImageList) {
            super.onItemClickListener(holder, item)
            holder.context.toast(item.toString())
        }
    }


    override fun onDestroy() {
        recycler_view.adapter = null
        super.onDestroy()
    }

}