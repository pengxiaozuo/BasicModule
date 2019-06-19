package com.peng.basicmodule.mvvm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.peng.basic.mvvm.BaseAndroidViewModel
import com.peng.basicmodule.api.GithubApiService
import com.peng.basicmodule.data.DataResult
import com.peng.basicmodule.data.User
import com.peng.httputils.HttpUtils
import com.peng.httputils.create
import kotlinx.coroutines.launch

class UserViewModel constructor(application: Application) : BaseAndroidViewModel(application) {

    val userLiveData = MutableLiveData<DataResult<User>>()
    val input = MutableLiveData<String>()

    fun getUser() {

        launch {
            try {
                userLiveData.postValue(
                    DataResult.success(
                        HttpUtils.create<GithubApiService>().getUser(
                            input.value ?: ""
                        )
                    )
                )
            } catch (t: Throwable) {
                userLiveData.postValue(DataResult.error(t))
            }
        }
    }
}
