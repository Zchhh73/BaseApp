package com.zch.network.retrofithelper.interceptor;

import com.zch.network.retrofithelper.RetrofitHelper;
import com.zch.network.retrofithelper.annotation.BaseUrl;
import com.zch.network.retrofithelper.annotation.DomainName;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Invocation;


public class DomainInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(processRequest(chain.request()));
    }

    /**
     * 处理请求，切换 BaseUrl
     * @param request
     * @return
     */
    private Request processRequest(Request request){
        //如果支持动态配置 BaseUrl
        if(RetrofitHelper.getInstance().isDynamicDomain()){
            Invocation invocation = request.tag(Invocation.class);
            if(invocation != null){
                BaseUrl baseUrl = invocation.method().getAnnotation(BaseUrl.class);
                if(baseUrl != null){
                    HttpUrl domainUrl = HttpUrl.parse(baseUrl.value());
                    if(domainUrl != null){
                        HttpUrl httpUrl = RetrofitHelper.getInstance().parseHttpUrl(domainUrl,request.url());
                        //如果不为空，则切换 BaseUrl
                        if(httpUrl != null){
                            return request.newBuilder()
                                    .url(httpUrl)
                                    .build();
                        }
                    }
                }
                DomainName domainName = invocation.method().getAnnotation(DomainName.class);
                if(domainName != null){
                    HttpUrl httpUrl = RetrofitHelper.getInstance().obtainParserDomainUrl(domainName.value(),request.url());
                    //如果不为空，则切换 BaseUrl
                    if(httpUrl != null){
                        return request.newBuilder()
                                .url(httpUrl)
                                .build();
                    }
                }
            }

            HttpUrl baseUrl = RetrofitHelper.getInstance().getBaseUrl();
            if(baseUrl != null){
                HttpUrl httpUrl = RetrofitHelper.getInstance().parseHttpUrl(baseUrl,request.url());
                //如果不为空，则切换 BaseUrl
                if(httpUrl != null){
                    return request.newBuilder()
                            .url(httpUrl)
                            .build();
                }
            }
        }

        return request;

    }


}
