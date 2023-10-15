package com.easypan.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * @author Tao
 * @since 2023/08/21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface GlobalInterceptor {

    /**
     * 校验参数
     *
     * @return boolean
    */
    boolean checkParams() default false;

    /**
     * 校验登录
     *
     * @return boolean
    */
    boolean checkLogin() default true;

    /**
     * 校验超级管理员
     *
     * @return boolean
    */
    boolean checkAdmin() default false;

}
