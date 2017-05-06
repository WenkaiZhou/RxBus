package com.kevin.rxbus.annotation;

import com.kevin.rxbus.SchedulerType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouwenkai on 2017/5/6.
 * 2017-05-06 11:02:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Subscribe {
    SchedulerType scheduler() default SchedulerType.TRAMPOLINE;
}
