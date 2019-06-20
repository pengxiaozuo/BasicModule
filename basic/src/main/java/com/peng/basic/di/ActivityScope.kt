package com.peng.basic.di

import java.lang.annotation.Documented
import javax.inject.Scope
import kotlin.annotation.Retention

@Scope
@Documented
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope