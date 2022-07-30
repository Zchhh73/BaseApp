package com.zch.network.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Basic okhttp3 CookieJar container
 */
class YDCookieJarContainer : CookieJarContainer {

    private var cookieJar: CookieJar? = null

    override fun setCookieJar(cookieJar: CookieJar) {
        this.cookieJar = cookieJar
    }

    override fun removeCookieJar() {
        this.cookieJar = null
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookieJar != null) {
            cookieJar!!.saveFromResponse(url, cookies)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return if (cookieJar != null) {
            cookieJar!!.loadForRequest(url)
        } else emptyList()
    }
}
