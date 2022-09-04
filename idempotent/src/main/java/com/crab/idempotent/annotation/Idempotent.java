package com.crab.idempotent.annotation;

import java.lang.annotation.*;

/**
 * Idempotent annotation use on method
 *
 * @author hackdc
 * @Date 2022/8/24 12:05 AM
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * Name used to determine the target idempotent key prefix
     */
    String name();

    /**
     * support Spring Expression Language (SpEL)
     */
    String key();

    /**
     * set idempotent key expire time, default 0 second
     */
    long idempotentExpire() default 0;

    /**
     * set lock expire time default 60 seconds
     */
    long lockExpire() default 60;

}
