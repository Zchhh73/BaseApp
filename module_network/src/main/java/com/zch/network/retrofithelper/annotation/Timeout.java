package com.zch.network.retrofithelper.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Timeout: 标记超时时长，用于支持动态改变超时时长
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Timeout {

    TimeUnit timeUnit() default TimeUnit.SECONDS;
    /**
     * 连接超时 时长（默认单位：秒）
     * @return
     */
    int connectTimeout() default 10;
    /**
     * 读取超时 时长（默认单位：秒）
     * @return
     */
    int readTimeout() default  10;
    /**
     * 写入超时 时长（默认单位：秒）
     * @return
     */
    int writeTimeout() default 10;


}
