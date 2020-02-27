package com.peng.basicmodule.mvvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.peng.basic.mvvm.BasicViewModel
import com.peng.basicmodule.data.DataResult
import com.peng.basicmodule.data.User
import com.peng.basicmodule.data.UserModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModel @Inject constructor(
    private val userModel: UserModel,
    application: Application
) : BasicViewModel(application) {

    val userLiveData = MutableLiveData<DataResult<User>>()
    val input = MutableLiveData<String>()

    fun getUser() {

        launch {
            try {
                userLiveData.postValue(userModel.getUser(input.value ?: ""))
            } catch (t: Throwable) {
                userLiveData.postValue(DataResult.error(t))
            }
        }
    }
}
