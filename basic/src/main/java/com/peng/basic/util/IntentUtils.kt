package com.peng.basic.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import java.io.File


object IntentUtils {
    /**
     * 安装应用Intent
     */
    @JvmStatic
    fun installAppIntent(context: Context, filePath: String): Intent? {
        val apkfile = File(filePath)
        if (!apkfile.exists()) {
            return null
        }
        val intent = Intent(Intent.ACTION_VIEW)
        val contentUri = FileUtils.getUriForFile(context, apkfile)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        return intent
    }

    /**
     * 卸载应用Intent
     */
    @JvmStatic
    fun uninstallAppIntent(packageName: String): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:$packageName")
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 启动应用Intent
     */
    @JvmStatic
    fun launchAppIntent(context: Context, packageName: String): Intent {
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }

    /**
     * 启动应用特定页面Intent
     */
    @JvmStatic
    @JvmOverloads
    fun componentNameIntent(packageName: String, className: String, bundle: Bundle? = null): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        if (bundle != null) intent.putExtras(bundle)
        val cn = ComponentName(packageName, className)
        intent.component = cn
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}