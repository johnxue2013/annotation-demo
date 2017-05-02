package com.johnxue.common.annotation;

import java.lang.annotation.*;

/**
 * @author han.xue
 * @since 2017-04-29 23:23：S
 */
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {
    String description();
}
