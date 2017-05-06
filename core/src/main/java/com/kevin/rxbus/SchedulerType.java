/*
 * Copyright 2017 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.rxbus;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
    NEW_THREAD(Schedulers.newThread()),
    MAIN_THREAD(AndroidSchedulers.mainThread());

    private Scheduler scheduler;

    private SchedulerType(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}
