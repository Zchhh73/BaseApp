package com.zch.network.error

import com.zch.network.bean.IBaseResponse
import com.zch.network.error.ERROR


/**
 *
 * classtime-android
 * Description: 错误实体类
 * Created by zhangchenhan on 2021/12/2 3:33 下午
 *
 **/
open class ResponseThrowable : Exception {
    var dataType: Int
    var code: Int
    var errMsg: String


    constructor(error: ERROR, e: Throwable? = null, dataType: Int=0) : super(e) {
        code = error.getKey()
        errMsg = error.getValue()
        this.dataType = dataType
    }

    constructor(code: Int, msg: String, e: Throwable? = null,dataType :Int=0) : super(e) {
        this.code = code
        this.errMsg = msg
        this.dataType = dataType

    }

    constructor(base: IBaseResponse<*>, e: Throwable? = null, dataType :Int=0) : super(e) {
        this.code = base.code()
        this.errMsg = base.msg()
        this.dataType = dataType
    }

}

