package com.zch.network.retrofithelper.parser;

import androidx.annotation.NonNull;

import okhttp3.HttpUrl;


public interface HttpUrlParser {


    /**
     * 解析 httpUrl, 将 httpUrl 的 baseUrl 换成 domainUrl
     * @param domainUrl
     * @param httpUrl
     * @return
     */
    HttpUrl parseHttpUrl(@NonNull HttpUrl domainUrl,@NonNull HttpUrl httpUrl);
}
