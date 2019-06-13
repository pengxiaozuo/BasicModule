package com.peng.basicmodule

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.peng.basic.base.BaseActivity
import com.peng.basic.base.ItemViewBinder
import com.peng.basic.base.MultiTypeAdapter
import com.peng.basic.base.SimpleViewHolder
import kotlinx.android.synthetic.main.activity_multitype.*

class MultiTypeActivity : BaseActivity() {

    private val mAdapter = MultiTypeAdapter()
    private val mListData = mutableListOf<Any>()


    override fun getLayoutId(): Int {
        return R.layout.activity_multitype
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
    }

    override fun initData() {
        mAdapter.registerType(String::class.java, StringTypeItemViewBinder())
        mAdapter.registerType(User::class.java, UserFemaleItemViewBinder(), UserMaleItemViewBinder()) {
            it as User
            return@registerType if (it.sex == Sex.FEMALE) 0 else 1
        }
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
        }
        mAdapter.notifyDataSetChanged()
    }

    class StringTypeItemViewBinder : ItemViewBinder<String, SimpleViewHolder>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SimpleViewHolder {
            return SimpleViewHolder(inflater.inflate(R.layout.item_multi_type_string, parent, false))
        }

        override fun onBinderViewHolder(item: String, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item)
        }

    }

    data class User(var name: String, var age: Int, var sex: Sex)
    enum class Sex(private val desc: String) {

        MALE("男的"), FEMALE("女的");

        override fun toString(): String {
            return desc
        }
    }

    class UserMaleItemViewBinder : ItemViewBinder<User, SimpleViewHolder>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SimpleViewHolder {
            return SimpleViewHolder(inflater.inflate(R.layout.item_multi_type_user_male, parent, false))
        }

        override fun onBinderViewHolder(item: User, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_age, item.age.toString())
            holder.setText(R.id.tv_sex, item.sex.toString())
        }

    }

    class UserFemaleItemViewBinder : ItemViewBinder<User, SimpleViewHolder>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SimpleViewHolder {
            return SimpleViewHolder(inflater.inflate(R.layout.item_multi_type_user_female, parent, false))
        }

        override fun onBinderViewHolder(item: User, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_age, item.age.toString())
            holder.setText(R.id.tv_sex, item.sex.toString())
        }

    }

    override fun onDestroy() {
        recycler_view.adapter = null
        super.onDestroy()
    }

}