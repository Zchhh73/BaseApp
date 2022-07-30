package com.zch.base

import android.os.Bundle
import com.zch.base.base.BaseActivity
import com.zch.base.base.NoViewModel
import com.zch.base.databinding.ActivityMainBinding


class MainActivity : BaseActivity<NoViewModel, ActivityMainBinding>() {
    override fun statusBarColorId() = R.color.white

    override fun isActivityDarkMode() = false

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

}