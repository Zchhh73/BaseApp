package com.zch.network.retrofithelper.interceptor;


import com.zch.network.retrofithelper.RetrofitHelper;

import java.io.IOException;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //如果支持添加头
        if(RetrofitHelper.getInstance().isAddHeader()){

            Map<String,String> headers = RetrofitHelper.getInstance().getHeaders();
            if(!headers.isEmpty()){
                Request.Builder builder = chain.request().newBuilder();
                for(Map.Entry<String,String> entry : headers.entrySet()){
                    builder.addHeader(entry.getKey(),entry.getValue());
                }
                return chain.proceed(builder.build());
            }
        }

        return chain.proceed(chain.request());
    }
}
