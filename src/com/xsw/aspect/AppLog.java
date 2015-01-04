package com.xsw.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author loginboot.vicp.net
 * 
 * @creator xiesw
 * @version 1.0.0
 * @date 2014-12-30
 * @description 应用APP系统日志注解类
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppLog {
    // 参数下标
    int position() default -1;

    // 数据对象名称
    String name() default "";

    // 数据详情
    String details() default "";
}
