package com.peng.basic.util

import android.content.Context
import kotlin.reflect.KProperty

object SPUtils {

    fun getString(
        context: Context,
        key: String,
        default: String?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): String? {
        return context.getSharedPreferences(filename, mode).run {
            getString(key, default)
        }
    }

    fun getInt(
        context: Context,
        key: String,
        default: Int,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Int {
        return context.getSharedPreferences(filename, mode).run {
            getInt(key, default)
        }
    }

    fun getFloat(
        context: Context,
        key: String,
        default: Float,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Float {
        return context.getSharedPreferences(filename, mode).run {
            getFloat(key, default)
        }
    }

    fun getLong(
        context: Context,
        key: String,
        default: Long,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Long {
        return context.getSharedPreferences(filename, mode).run {
            getLong(key, default)
        }
    }

    fun getBoolean(
        context: Context,
        key: String,
        default: Boolean,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).run {
            getBoolean(key, default)
        }
    }

    fun getStringSet(
        context: Context,
        key: String,
        default: Set<String?>?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Set<String?>? {
        return context.getSharedPreferences(filename, mode).run {
            getStringSet(key, default)
        }
    }

    fun getAll(
        context: Context,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Map<String, *> {
        return context.getSharedPreferences(filename, mode).run {
            getAll()
        }
    }

    fun putString(
        context: Context,
        key: String,
        value: String?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putString(key, value)
        }.commit()
    }

    fun putInt(
        context: Context,
        key: String,
        value: Int,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putInt(key, value)
        }.commit()
    }

    fun putFloat(
        context: Context,
        key: String,
        value: Float,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putFloat(key, value)
        }.commit()
    }

    fun putLong(
        context: Context,
        key: String,
        value: Long,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putLong(key, value)
        }.commit()
    }

    fun putBoolean(
        context: Context,
        key: String,
        value: Boolean,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putBoolean(key, value)
        }.commit()
    }

    fun putStringSet(
        context: Context,
        key: String,
        value: Set<String?>?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            putStringSet(key, value)
        }.commit()
    }

    fun remove(
        context: Context,
        key: String,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            remove(key)
        }.commit()
    }

    fun clear(
        context: Context,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            clear()
        }.commit()
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    fun <T> get(
        context: Context,
        key: String,
        default: T?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): T? {
        return context.getSharedPreferences(filename, mode).run {
            when (default) {
                is String? -> getString(key, default)
                is Int -> getInt(key, default)
                is Long -> getLong(key, default)
                is Float -> getFloat(key, default)
                is Boolean -> getBoolean(key, default)
                is Set<*>? -> getStringSet(
                    key,
                    if (default == null) default else default as Set<String?>
                )
                else -> throw IllegalArgumentException("type error")
            } as T
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> put(
        context: Context,
        key: String,
        value: T?,
        filename: String = "shared_preference",
        mode: Int = Context.MODE_PRIVATE
    ): Boolean {
        return context.getSharedPreferences(filename, mode).edit().apply {
            when (value) {
                is String? -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                is Set<*>? -> putStringSet(
                    key,
                    if (value == null) value else value as Set<String?>
                )
                else -> throw IllegalArgumentException("type error")
            }
        }.commit()
    }

}

class PreferenceDelegate<T>(
    private val context: Context,
    private val key: String,
    private val default: T? = null,
    private val filename: String = "shared_preference",
    private val mode: Int = Context.MODE_PRIVATE
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return SPUtils.get(context, key, default, filename, mode)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        SPUtils.put(context, key, value, filename, mode)
    }
}