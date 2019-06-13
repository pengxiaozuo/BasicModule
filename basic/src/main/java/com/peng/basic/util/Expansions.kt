package com.peng.basic.util

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun View.click(onNext: ((Unit) -> Unit)?) {
    onNext?.also {
        this.clicks().clickDelay().toMain().subscribe(it)
    }
}

fun <T> Observable<T>.clickDelay(): Observable<T> = throttleLatest(1, TimeUnit.SECONDS)

fun <T> Observable<T>.fromIoToMain() = fromIo().toMain()
fun <T> Observable<T>.fromIo() = subscribeOn(Schedulers.io())
fun <T> Observable<T>.toMain() = observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.fromIoToMain() = fromIo().toMain()
fun <T> Flowable<T>.fromIo() = subscribeOn(Schedulers.io())
fun <T> Flowable<T>.toMain() = observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.fromIoToMain() = fromIo().toMain()
fun <T> Maybe<T>.fromIo() = subscribeOn(Schedulers.io())
fun <T> Maybe<T>.toMain() = observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.fromIoToMain() = fromIo().toMain()
fun <T> Single<T>.fromIo() = subscribeOn(Schedulers.io())
fun <T> Single<T>.toMain() = observeOn(AndroidSchedulers.mainThread())
