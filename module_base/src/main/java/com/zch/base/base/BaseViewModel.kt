package com.zch.base.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.zch.base.bean.IBaseResponse
import com.zch.base.error.ResponseThrowable
import com.zch.base.app.CommonUtil
import com.zch.base.event.ErrorEvent
import com.zch.base.event.Message
import com.zch.base.event.SingleLiveEvent
import com.zch.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 通用Base组件
 *
 * Description: BaseViewModel基类
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
open class BaseViewModel(
    application: Application = Utils.getApp()
) : AndroidViewModel(application), LifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun launchGo(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
            defUI.toastEvent.postValue(it.errMsg)

        },
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = true, isShowErrorNet: Boolean = true
    ) {
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                {
                    error(it)
                    defUI.showNetError.postValue(ErrorEvent(it.dataType, isShowErrorNet))

                },
                {
                    defUI.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyResultLocal(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.toastEvent.postValue(it.errMsg)
        },
        complete: () -> Unit = {},
        isShowDialog: Boolean = true, isShowErrorNet: Boolean = true
    ) {
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block()
                    }.also {
                        success(it)
                    }
                },
                {
                    error(it)
                    defUI.showNetError.postValue(ErrorEvent(it.dataType, isShowErrorNet))
                },
                {
                    defUI.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchOnlyresult(
        block: suspend CoroutineScope.() -> IBaseResponse<T>,
        success: (T) -> Unit,
        error: (ResponseThrowable) -> Unit = {
            defUI.toastEvent.postValue(it.errMsg)
        },
        complete: () -> Unit = {},
        isShowDialog: Boolean = true, isShowErrorNet: Boolean = true
    ) {
        if (isShowDialog) defUI.showDialog.call()
        launchUI {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block().let {
                            if (it.isSuccess()) it.data()
                            else throw ResponseThrowable(it.code(), it.msg(), null, 1)
                        }
                    }.also { success(it) }
                },
                {
                    error(it)
                    defUI.showNetError.postValue(ErrorEvent(it.dataType, isShowErrorNet))
                },
                {
                    defUI.dismissDialog.call()
                    complete()
                }
            )
        }
    }


    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                error(CommonUtil.getConfig().globalExceptionHandle(e))
            } finally {
                complete()
            }
        }
    }


    /**
     * UI事件
     */
    inner class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
        val toastEvent by lazy { SingleLiveEvent<String>() }
        val msgEvent by lazy { SingleLiveEvent<Message>() }
        val showNetError by lazy { SingleLiveEvent<ErrorEvent>() }

    }
}