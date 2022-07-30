package com.zch.network.utils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 *
 * classtime-android
 * Description: RequestBody工具类
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
object OkHttpRequestBodyUtil {
    /**
     * 图片media
     */
    const val imgMediaType: String = "image/png"

    /**
     * form表单
     */
    const val formMediaType: String = "multipart/form-data"

    /**
     * json
     */
    const val jsonMediaType: String = "application/json;charset=utf-8"

    /**
     * text
     */
    const val textMediaType: String = "text/x-markdown;charset=utf-8"

    /**
     * json requestBody
     */
    fun getRequestBody(str: String): RequestBody {
        //String转RequestBody String、ByteArray、ByteString都可以用toRequestBody()
        return str.toRequestBody(jsonMediaType.toMediaType())
    }

    /**
     * mediaType requestBody
     */
    fun getRequestBody(str: String, mediaType: String): RequestBody {
        //String转RequestBody String、ByteArray、ByteString都可以用toRequestBody()
        return str.toRequestBody(mediaType.toMediaType())
    }

    /**
     * mediaType 单个文件上传
     */
    fun getMultipartBody(file: File, mediaType: String): MultipartBody {

        val fileBody = file.asRequestBody(mediaType.toMediaType())
        return MultipartBody.Builder()
            .addFormDataPart("file", file.name, fileBody)
            .build()
    }

    /**
     * form表单 单个文件上传
     */
    fun getMultipartBody(file: File): MultipartBody {
        val fileBody = file.asRequestBody(formMediaType.toMediaType())
        return MultipartBody.Builder()
            .addFormDataPart("file", file.name, fileBody)
            .build()
    }

    /**
     * mediaType 多个文件上传
     */
    fun getMultipartBodyList(
        fileList: MutableList<File>,
        mediaType: String
    ): MutableList<MultipartBody> {
        val multipartBodyList: MutableList<MultipartBody> = ArrayList()
        for (file in fileList) {
            val fileBody = file.asRequestBody(mediaType.toMediaType())
            val multipartBody = MultipartBody.Builder()
                .addFormDataPart("file", file.name, fileBody)
                .build()
            multipartBodyList.add(multipartBody)
        }
        return multipartBodyList
    }

    /**
     * form表单多个文件上传
     */
    fun getMultipartBodyList(fileList: MutableList<File>): MutableList<MultipartBody> {
        val multipartBodyList: MutableList<MultipartBody> = ArrayList()
        for (file in fileList) {
            val fileBody = file.asRequestBody(formMediaType.toMediaType())
            val multipartBody = MultipartBody.Builder()
                .addFormDataPart("file", file.name, fileBody)
                .build()
            multipartBodyList.add(multipartBody)
        }
        return multipartBodyList
    }
}