package com.zch.base.network

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.zch.utils.Utils


/**
 * 通用Base组件
 * Description: 网络变化监听
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class NetworkStateManager private constructor() : DefaultLifecycleObserver {
    private var mNetworkStateReceive: NetworkStateReceive? = null
    override fun onResume(owner: LifecycleOwner) {
        mNetworkStateReceive = NetworkStateReceive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        Utils.getApp().applicationContext.registerReceiver(mNetworkStateReceive, filter)
    }

    override fun onPause(owner: LifecycleOwner) {
        Utils.getApp().applicationContext.unregisterReceiver(mNetworkStateReceive)
    }

    companion object {
        val instance = NetworkStateManager()
    }
}