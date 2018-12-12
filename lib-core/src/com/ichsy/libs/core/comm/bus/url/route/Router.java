package com.ichsy.libs.core.comm.bus.url.route;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由跳转注解
 * Created by liuyuhang on 2018/1/12.
 */
@Target(ElementType.TYPE)//Target注解决定MyAnnotation注解可以加在哪些成分上，Class, interface or enum declaration.
@Documented
@Retention(RetentionPolicy.CLASS)//这个注解的意思是让MyAnnotation注解在java源文件(.java文件)中存在，编译成.class文件后注解也还存在，
public @interface Router {

    String DEFAULT = "route_none";

    String value() default DEFAULT;
}
