package com.peng.basic.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.peng.basic.util.ToastUtils

abstract class BaseFragment : Fragment(), IBaseUi {

    protected var activity: Activity? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activity = context as Activity
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun getLayoutId(): Int = 0

    open fun getLayoutView(): View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        initParams()

        val view: View = if (getLayoutView() == null) {
            inflater.inflate(getLayoutId(), container, false)
        } else {
            getLayoutView()!!
        }

        initView(view)

        initData()

        return view
    }


    override fun initParams() {}


    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        activity?.let {
            ToastUtils.showToast(it, msg, duration)
        }
    }

    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null) {
        activity?.let {
            val intent = Intent(it, clazz)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }
    }

}