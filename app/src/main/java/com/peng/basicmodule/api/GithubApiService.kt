package com.peng.basicmodule.api

import com.peng.basicmodule.data.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): User
    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}