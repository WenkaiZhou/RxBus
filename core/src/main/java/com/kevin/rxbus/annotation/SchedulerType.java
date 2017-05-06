package com.kevin.rxbus.annotation;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouwenkai on 2017/5/6.
 * 2017-05-06 11:27:43
 */

public enum SchedulerType {
    SINGLE(Schedulers.single()),
    COMPUTATION(Schedulers.computation()),
    IO(Schedulers.io()),
    TRAMPOLINE(Schedulers.trampoline()),
    NEW_THREAD(Schedulers.newThread());

    private Scheduler scheduler;

    private SchedulerType(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
