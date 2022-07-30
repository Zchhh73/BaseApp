package com.zch.network.retrofithelper.annotation;



import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DomainName: 标记域名别名，用于支持动态改变 BaseUrl。优先级低于{@link BaseUrl}
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainName {
    String value();
}
