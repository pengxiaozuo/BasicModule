package com.peng.basicmodule

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.peng.basic.base.BaseActivity
import com.peng.basic.adapter.ItemViewBinder
import com.peng.basic.adapter.MultiTypeAdapter
import com.peng.basic.adapter.SimpleItemViewBinder
import com.peng.basic.adapter.SimpleViewHolder
import com.peng.basic.util.TypeUtils
import com.peng.basic.util.logd
import kotlinx.android.synthetic.main.activity_multitype.*
import java.lang.reflect.ParameterizedType
import kotlin.math.log

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
        mAdapter.registerType(StringTypeItemViewBinder())
        mAdapter.registerType(UserFemaleItemViewBinder())
        mAdapter.registerType(UserMaleItemViewBinder())
        mAdapter.registerType(ListStringViewBinder())
        mAdapter.registerType(ListUserViewBinder())
        mAdapter.registerType(ListListStringViewBinder())
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
        mListData.add(arrayListOf<String>("A", "B", "C"))
        mListData.add(listOf(listOf("A", "B", "C"), listOf("A", "B", "C")))
        mListData.add(listOf(User("OK", 12, Sex.FEMALE)))
        mAdapter.notifyDataSetChanged()
    }

    class StringTypeItemViewBinder : SimpleItemViewBinder<String>(R.layout.item_multi_type_string) {

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

    class UserMaleItemViewBinder : SimpleItemViewBinder<User>(R.layout.item_multi_type_user_male) {
        override fun onBinder(any: Any): Boolean {
            return any is User && any.sex == Sex.MALE
        }

        override fun onBinderViewHolder(item: User, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_age, item.age.toString())
            holder.setText(R.id.tv_sex, item.sex.toString())
        }

    }

    class UserFemaleItemViewBinder : ItemViewBinder<User, SimpleViewHolder>() {
        override fun onBinder(any: Any): Boolean {
            return any is User && any.sex == Sex.FEMALE
        }

        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): SimpleViewHolder {
            return SimpleViewHolder(
                inflater.inflate(
                    R.layout.item_multi_type_user_female,
                    parent,
                    false
                )
            )
        }

        override fun onBinderViewHolder(item: User, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_age, item.age.toString())
            holder.setText(R.id.tv_sex, item.sex.toString())
        }

    }

    class ListStringViewBinder : SimpleItemViewBinder<List<String>>(R.layout.item_multi_type_string) {
        override fun onBinder(any: Any): Boolean {
            logd(TypeUtils.getRawType((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]))
            if (any is List<*>) {
                val type = any.javaClass
                logd("Type == " + type)
                if (type is ParameterizedType) {
                    logd("${TypeUtils.getRawType(type.actualTypeArguments[0])}, ${type.rawType}")
                }
            }

            return super.onBinder(any)
        }

        override fun onBinderViewHolder(item: List<String>, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.toString())
            logd("ListStringViewBinder>>>" + item)
        }
    }

    class ListUserViewBinder : SimpleItemViewBinder<List<User>>(R.layout.item_multi_type_string) {
        override fun onBinderViewHolder(item: List<User>, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.toString())
            logd("ListUserViewBinder>>>" + item)
        }
    }

    class ListListStringViewBinder : SimpleItemViewBinder<List<List<String>>>(R.layout.item_multi_type_string) {
        override fun onBinderViewHolder(item: List<List<String>>, holder: SimpleViewHolder) {
            holder.setText(R.id.tv_name, item.toString())
            logd("ListListStringViewBinder>>>" + item)
        }
    }

    override fun onDestroy() {
        recycler_view.adapter = null
        super.onDestroy()
    }

}