package com.zch.base.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * 通用Base组件
 *
 * Description: 网络变化监听
 * Created by zch on 2022/8/1 3:33 下午
 *
 **/
class NetworkStateReceive : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {

        }
    }
}