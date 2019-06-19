package com.peng.basicmodule.mvp

import com.peng.basicmodule.data.UserModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MVPPresenter
@Inject constructor(
    private val userModel: UserModel
) : MVPContract.Presenter() {

    override fun getUser(username: String) {
        launch {
            try {
                view?.showLoading("正在获取数据，请稍等...")
                val result = userModel.getUser(username)
                if (result.success) {
                    view?.getUserSuccess(result.data!!)
                } else {

                    view?.getUserError("get user error")
                }
            } finally {
                view?.hideLoading()
            }
        }
    }

}