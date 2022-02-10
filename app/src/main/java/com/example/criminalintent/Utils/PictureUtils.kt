package com.example.criminalintent.Utils

import android.app.Activity
import android.graphics.*
import android.os.Build
import android.util.Size
import android.view.WindowInsets
import android.view.WindowMetrics


class PictureUtils {

    companion object {
        // Эффективое масшабирование
        fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
            // Чтение размеров изображения на диске
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            val srcWidth = options.outWidth.toFloat()
            val srcHeight = options.outHeight.toFloat()

            // Выясняем, на сколько нужно уменьшить
            var inSampleSize = 1
            if (srcHeight > destHeight || srcWidth > destWidth) {
                val heightScale = srcHeight / destHeight
                val widthScale = srcWidth / destWidth

                val sampleScale = if (heightScale > widthScale) heightScale else widthScale
                inSampleSize = Math.round(sampleScale)
            }

            options = BitmapFactory.Options()
            // Задать масштаб
            options.inSampleSize = inSampleSize

            // Чтение и создание окончаельного растрового изображения
            return BitmapFactory.decodeFile(path, options)
        }

        // Масштабирование с консервативной оценкой
        fun getScaledBitmap(path: String, activity: Activity): Bitmap {
            val legacySize: Size
            val windowManager = activity.windowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val metrics: WindowMetrics = windowManager.currentWindowMetrics
                // Gets all excluding insets
                val windowInsets = metrics.windowInsets
                val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
                    WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
                )

                val insetsWidth: Int = insets.right + insets.left
                val insetsHeight: Int = insets.top + insets.bottom

                // Legacy size that Display#getSize reports
                val bounds: Rect = metrics.bounds
                legacySize = Size(
                    bounds.width() - insetsWidth,
                    bounds.height() - insetsHeight
                )
            } else {
                val size = Point()
                windowManager.defaultDisplay.getSize(size)
                legacySize = Size(size.x, size.y)
            }

            return getScaledBitmap(path, legacySize.width, legacySize.height)
        }
    }

}