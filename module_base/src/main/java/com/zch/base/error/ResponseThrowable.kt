package com.zch.base.error

import com.zch.base.bean.IBaseResponse


/**
 * zch-android
 * Description: 错误实体类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
open class ResponseThrowable : Exception {
    var dataType: Int
    var code: String
    var errMsg: String


    constructor(error: ERROR, e: Throwable? = null, dataType: Int=0) : super(e) {
        code = error.getKey()
        errMsg = error.getValue()
        this.dataType = dataType
    }

    constructor(code: String, msg: String, e: Throwable? = null,dataType :Int=0) : super(e) {
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

