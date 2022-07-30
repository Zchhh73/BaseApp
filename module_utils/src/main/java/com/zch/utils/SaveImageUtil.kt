package com.zch.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.*

/**
 *
 * zch-android
 * Description: 保存图片
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
object SaveImageUtil {

    /**
     * 将bitmap存成文件
     *
     * @param context
     * @param bitmap
     * @param imageName
     */
    fun saveBitmap(context: Context, bitmap: Bitmap, imageName: String): Boolean {
        val fos: FileOutputStream? = null
        var os: OutputStream? = null
        var inputStream: BufferedInputStream? = null
        val storageDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                .toString() + "/qrcode"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (!success) {
            ToastUtils.showToast(
                context, "保存失败"
            )
            return false
        }

        val imageFile = File(storageDir, imageName)
        try {
            val fout: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout)
            fout.close()

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.TITLE, "Image.png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            }
            val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val resolver: ContentResolver = context.contentResolver
            val insertUri: Uri? = resolver.insert(external, values)
            inputStream = BufferedInputStream(FileInputStream(imageFile))
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri)
            }
            if (os != null) {
                val buffer = ByteArray(1024 * 4)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    os.write(buffer, 0, len)
                }
                os.flush()
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(imageFile.toString()),
                arrayOf(imageFile.name),
                null
            )
            ToastUtils.showToast(context, "保存成功")
            return true

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ToastUtils.showToast(
                context, "保存失败"
            )
            return false

        } finally {
            try {
                fos?.close()
                os?.close()
                inputStream?.close()
                imageFile.delete()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}