package com.peng.basic.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File

/**
 * 文件工具类
 *
 * TODO 等待填充方法
 */
object FileUtils {

    /**
     * 获取文件的uri
     * android7.0以上需要配置FileProvider 并且authority为包名
     */
    @JvmStatic
    fun getUriForFile(mContext: Context, file: File): Uri? {
        var fileUri: Uri? = null
        fileUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(mContext, mContext.packageName + ".fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
        return fileUri
    }

}