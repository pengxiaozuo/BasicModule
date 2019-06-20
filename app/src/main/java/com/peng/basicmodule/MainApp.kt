package com.peng.basicmodule

import android.app.Application
import com.peng.basicmodule.api.GithubApiService
import com.peng.basicmodule.di.AppComponent
import com.peng.basicmodule.di.DaggerAppComponent
import com.peng.basic.mvvm.ViewModelFactory
import com.peng.httputils.HttpUtils
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class MainApp : Application(), HasAndroidInjector {

    @Inject
    @Volatile
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)

        initHttp()
    }


    private fun initHttp() {
        //详细指定需要的配置
        //初始化，如果初始化过多个baseUrl的话，在create时，需要指定baseUrl
        HttpUtils.init(GithubApiService.BASE_URL) {
            //是否打印http的Request Response信息 默认false
            printLog = BuildConfig.DEBUG
            connectTimeout = 60 * 1000L
            readTimeout = 60 * 1000L
            writeTimeout = 60 * 1000L
            //是否失败重试 默认true
            retry = true
            //配置cookie缓存实现 默认null
            cookieJar = null

            addConverterFactory(GsonConverterFactory.create())
        }
    }

}