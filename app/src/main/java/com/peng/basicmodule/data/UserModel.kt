package com.peng.basicmodule.data

import com.google.gson.annotations.SerializedName
import com.peng.basicmodule.api.GithubApiService
import javax.inject.Inject

class UserModel
@Inject constructor(
    private val githubApiService: GithubApiService
) {

    suspend fun getUser(username: String): DataResult<User> {
        return try {
            val user = githubApiService.getUser(username)
            DataResult.success(user)
        } catch (t: Throwable) {
            DataResult.error(t)
        }
    }
}

data class User(
    var name: String = "",
    @SerializedName("avatar_url") var avatarUrl: String = ""
)