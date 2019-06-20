package com.peng.basicmodule.data


class DataResult<T> private constructor(var data: T?, var error: Throwable?, var success: Boolean) {


    companion object {
        fun <T> success(t: T): DataResult<T> = DataResult(t, null, true)
        fun <T> error(throwable: Throwable): DataResult<T> = DataResult<T>(null, throwable, false)
    }
}