package com.peng.basic.util

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.InputStream


object BitmapUtils {

    /**
     * ByteArray转Bitmap
     */
    @JvmStatic
    fun byteArray2Bitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /**
     * Bitmap 转 ByteArray
     */
    @JvmStatic
    @JvmOverloads
    fun bitmap2ByteArray(bitmap: Bitmap, format: Bitmap.CompressFormat, recycle: Boolean = true): ByteArray {
        val bos = ByteArrayOutputStream()
        bitmap.compress(format, 100, bos)
        if (recycle) bitmap.recycle()
        return bos.toByteArray()
    }

    /**
     * Drawable转Bitmap
     */
    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable): Bitmap {
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        val config = Bitmap.Config.ARGB_4444
        val bitmap = Bitmap.createBitmap(w, h, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * Bitmap 转Drawable
     */
    @JvmStatic
    fun bitmap2Drawable(resources: Resources, bitmap: Bitmap): Drawable {
        return BitmapDrawable(resources, bitmap)
    }

    /**
     * 缩放
     */
    @JvmStatic
    @JvmOverloads
    fun scaleByNewSize(src: Bitmap, newWidth: Int, newHeight: Int, recycle: Boolean = true): Bitmap {
        val bitmap = Bitmap.createScaledBitmap(src, newWidth, newHeight, true)
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 缩放
     */
    @JvmStatic
    @JvmOverloads
    fun scale(src: Bitmap, scaleX: Float, scaleY: Float, recycle: Boolean = true): Bitmap {
        val matrix = Matrix()
        matrix.setScale(scaleX, scaleY)
        val bitmap = Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 剪切
     */
    @JvmStatic
    @JvmOverloads
    fun clip(src: Bitmap, x: Int, y: Int, width: Int, height: Int, recycle: Boolean = true): Bitmap {
        val bitmap = Bitmap.createBitmap(src, x, y, width, height)
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 旋转
     */
    @JvmStatic
    @JvmOverloads
    fun rotate(src: Bitmap, px: Float, py: Float, degrees: Float, recycle: Boolean = true): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(degrees, px, py)
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    /**
     * 转换为圆角
     */
    @JvmStatic
    @JvmOverloads
    fun round(src: Bitmap, radiusX: Float, radiusY: Float, recycle: Boolean = true): Bitmap {
        val bitmap = Bitmap.createBitmap(src.width, src.height, src.config)
        val bitmapShader = BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val paint = Paint()
        val canvas = Canvas(bitmap)
        val rect = RectF(0F, 0F, src.width.toFloat(), src.height.toFloat())
        paint.isAntiAlias = true
        paint.shader = bitmapShader
        canvas.drawRoundRect(rect, radiusX, radiusY, paint)
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 转换为圆形
     */
    @JvmStatic
    @JvmOverloads
    fun circle(src: Bitmap, recycle: Boolean = true): Bitmap {
        val radius = Math.min(src.width, src.height) / 2

        val bitmap = Bitmap.createBitmap(radius * 2, radius * 2, src.config)
        val paint = Paint()
        val canvas = Canvas(bitmap)
        paint.isAntiAlias = true
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        val rect = Rect(0, 0, src.width, src.height)
        canvas.drawBitmap(src, rect, rect, paint)
        paint.xfermode = null
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 灰度
     */
    @JvmStatic
    @JvmOverloads
    fun gray(src: Bitmap, recycle: Boolean = true): Bitmap {
        val bitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_4444)
        val paint = Paint()
        val canvas = Canvas(bitmap)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0F)
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(src, 0F, 0F, paint)
        if (recycle) src.recycle()
        return bitmap
    }

    /**
     * 压缩
     */
    @JvmStatic
    @JvmOverloads
    fun compress(src: Bitmap, maxSize: Long, recycle: Boolean = true): Bitmap {
        val bos = ByteArrayOutputStream()
        var quality = 100
        src.compress(Bitmap.CompressFormat.PNG, quality, bos)
        while (bos.toByteArray().size > maxSize && quality > 0) {
            bos.reset()
            quality -= 5
            src.compress(Bitmap.CompressFormat.PNG, quality, bos)
        }

        val byteArray = bos.toByteArray()
        if (recycle) src.recycle()
        return byteArray2Bitmap(byteArray)
    }

    /**
     * 压缩
     */
    @JvmStatic
    @JvmOverloads
    fun compress(src: Bitmap, sampleSize: Int, recycle: Boolean = true): Bitmap {
        val opt = BitmapFactory.Options()
        opt.inSampleSize = sampleSize
        val byteArray = bitmap2ByteArray(src, Bitmap.CompressFormat.PNG)
        if (recycle) src.recycle()
        return byteArray2Bitmap(byteArray)
    }

    /**
     * 计算采样大小
     */
    @JvmStatic
    fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
        if (maxWidth == 0 || maxHeight == 0) {
            return 1
        }
        var height = options.outHeight
        var width = options.outWidth
        var inSampleSize = 1
        while (height.apply { height = height shr 1 } >= maxHeight
            && width.apply { width = width shr 1 } >= maxWidth) {
            inSampleSize = inSampleSize shl 1
        }
        return inSampleSize
    }

    @JvmStatic
    fun getBitmap(path: String, maxWidth: Int, maxHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    @JvmStatic
    fun getBitmap(resources: Resources, id: Int, maxWidth: Int, maxHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, id, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, id, options)
    }

    @JvmStatic
    fun getBitmap(inputStream: InputStream, maxWidth: Int, maxHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(inputStream, null, options)
    }

    @JvmStatic
    fun getDrawable(context: Context, resId: Int) = ContextCompat.getDrawable(context, resId)
}