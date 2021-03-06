package com.peng.basic.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR2
import android.os.Environment
import android.os.StatFs
import android.support.v4.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


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

    /**
     * 获SD卡取缓存目录如果无SD卡则返回内部返回目录
     */
    fun getCacheDirPriorityExternalStorage(context: Context, subDir: String): File {
        val cachePath: String = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
            || !Environment.isExternalStorageRemovable()
        ) {
            context.externalCacheDir!!.path
        } else {
            context.cacheDir.path
        }
        return File(cachePath + File.separator + subDir)
    }


    /**
     * 获取可见空间大小 单位 字节
     */
    fun getSize(dir: File): Long {
        val statFs = StatFs(dir.absolutePath)
        val blockCount =
            if (SDK_INT < JELLY_BEAN_MR2) statFs.blockCount.toLong() else statFs.blockCountLong
        val blockSize =
            if (SDK_INT < JELLY_BEAN_MR2) statFs.blockSize.toLong() else statFs.blockSizeLong
        return blockCount * blockSize
    }

    /**
     * 保存文件
     * @throws IOException
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IOException::class)
    fun save(file: File, dir: String = file.parent) {
        val target = if (dir == file.parent) file else File(dir, file.name)
        var fos: FileOutputStream? = null
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(file)
            fos = FileOutputStream(target)
            val buffer = ByteArray(4 * 1024)
            var len = -1
            while (true) {
                len = fis.read(buffer)
                if (len == -1) break
                fos.write(buffer, 0, len)
            }
        } finally {
            CloseUtils.close(fis, fos)
        }
    }

}