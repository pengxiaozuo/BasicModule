@file:JvmName("Extensions")
@file:JvmMultifileClass

package com.peng.basic.util

import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

fun View.click(onNext: (Unit) -> Unit): Disposable =
    clicks().throttleFirst(1, TimeUnit.SECONDS).toMain().subscribe(onNext)


fun View.click(onNext: (Unit) -> Unit, onError: (Throwable) -> Unit): Disposable =
    clicks().throttleFirst(1, TimeUnit.SECONDS).toMain().subscribe(onNext, onError)

fun <T> Observable<T>.fromIoToMain(): Observable<T> = fromIo().toMain()
fun <T> Observable<T>.fromIo(): Observable<T> = subscribeOn(Schedulers.io())
fun <T> Observable<T>.toMain(): Observable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.fromIoToMain(): Flowable<T> = fromIo().toMain()
fun <T> Flowable<T>.fromIo(): Flowable<T> = subscribeOn(Schedulers.io())
fun <T> Flowable<T>.toMain(): Flowable<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Maybe<T>.fromIoToMain(): Maybe<T> = fromIo().toMain()
fun <T> Maybe<T>.fromIo(): Maybe<T> = subscribeOn(Schedulers.io())
fun <T> Maybe<T>.toMain(): Maybe<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.fromIoToMain(): Single<T> = fromIo().toMain()
fun <T> Single<T>.fromIo(): Single<T> = subscribeOn(Schedulers.io())
fun <T> Single<T>.toMain(): Single<T> = observeOn(AndroidSchedulers.mainThread())

fun <T> CoroutineScope.asyncUi(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.Main, start, block)

fun <T> CoroutineScope.asyncIo(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = async(Dispatchers.IO, start, block)

fun CoroutineScope.launchUi(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = launch(Dispatchers.Main, start, block)

fun CoroutineScope.launchIo(
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = launch(Dispatchers.IO, start, block)
