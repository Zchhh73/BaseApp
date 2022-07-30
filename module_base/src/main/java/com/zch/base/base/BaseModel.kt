package com.zch.base.base

import com.zch.base.bean.IBaseResponse
import com.zch.base.error.ResponseThrowable


/**
 * 通用Base组件
 *
 * Description: 网络数据没有加载本地数据
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
abstract class BaseModel {

    /**
     * @param remoto 网络数据
     * @param local 本地数据
     * @param save 当网络请求成功后，保存数据等操作
     * @param isUseCache 是否使用缓存
     */
    suspend fun <T> cacheNetCall(
        remoto: suspend () -> IBaseResponse<T>,
        local: suspend () -> T?,
        save: suspend (T) -> Unit,
        isUseCache: (T?) -> Boolean = { true }
    ): T {
        val localData = local.invoke()
        return if (isUseCache(localData) && localData != null){ localData}
        else {
            if (remoto().isSuccess()){
                remoto().data().also { save(it) }
            }else{
                throw ResponseThrowable(remoto())
            }
        }
    }

}