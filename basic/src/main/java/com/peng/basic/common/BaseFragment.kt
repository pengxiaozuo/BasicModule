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

    open fun getLayoutId(): Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        initParams()

        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initData()
    }


    override fun initParams() {}


    @JvmOverloads
    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        activity?.let {
            ToastUtils.showToast(it, msg, duration)
        }
    }

    @JvmOverloads
    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null) {
        activity?.let {
            val intent = Intent(it, clazz)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

}