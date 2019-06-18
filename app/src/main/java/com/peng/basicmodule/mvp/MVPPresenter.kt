package com.peng.basicmodule.mvp

import com.peng.basic.util.*
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject

class MVPPresenter
@Inject constructor() : MVPContract.Presenter() {

    var controlGetUserResultSuccess = true

    override fun getUser() {
        launch {
            try {
                val deferred = asyncIo(SupervisorJob(job())) {
                    logd("获取数据中....")
                    delay(2000)
                    if (!controlGetUserResultSuccess) throw IOException()
                    "张三 18 男"
                }
                view?.showLoading("正在获取数据，请稍等...")
                deferred.start()
                val user = deferred.await()
                logd("get user success: $user")
                view?.getUserSuccess(user)
                controlGetUserResultSuccess = false
            } catch (e: IOException) {
                logw("get user error!", throwable = e)
                view?.getUserError("get user error")
                controlGetUserResultSuccess = true
            } finally {
                logd("get user finish")
                view?.hideLoading()
            }
        }
    }

}