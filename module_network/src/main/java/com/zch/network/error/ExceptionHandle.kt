package com.zch.network.error

import android.net.ParseException
import android.util.Log
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.zch.network.error.ERROR
import com.zch.network.error.ResponseThrowable
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *
 * classtime-android
 * Description: 网络错误提示
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable

        when (e) {
            is ResponseThrowable -> {
                ex = e
            }
            is HttpException -> {
                ex = ResponseThrowable(ERROR.HTTP_ERROR, e, 0)
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ex = ResponseThrowable(ERROR.PARSE_ERROR, e, 0)
            }
            is ConnectException -> {
                ex = ResponseThrowable(ERROR.NETWORD_ERROR, e, 0)
            }
            is javax.net.ssl.SSLException -> {
                ex = ResponseThrowable(ERROR.REQUEST_FAILED, e, 0)
            }
            is java.net.SocketTimeoutException -> {
                ex = ResponseThrowable(ERROR.REQUEST_FAILED, e, 0)
            }
            is java.net.UnknownHostException -> {
                ex = ResponseThrowable(ERROR.REQUEST_FAILED, e, 0)
            }

            else -> {
                ex = ResponseThrowable(ERROR.HTTP_ERROR, e, 0)

//                ex = if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e)
//                else ResponseThrowable(ERROR.REQUEST_FAILED, e, 0)
            }

        }

        Log.i("net-------", e.toString());
        return ex
    }
}