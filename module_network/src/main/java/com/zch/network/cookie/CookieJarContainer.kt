package com.zch.network.cookie

import okhttp3.CookieJar

interface CookieJarContainer : CookieJar {

    fun setCookieJar(cookieJar: CookieJar)

    fun removeCookieJar()

}
