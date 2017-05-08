/*
 * Copyright (C) 2012-2016 Kevin zhou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kevin.rxbus;

import com.kevin.rxbus.internal.RxBusConsumer;
import com.kevin.rxbus.internal.RxBusPredicate;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.subjects.Subject;

/**
 * Created by zwenkai on 2017/5/6.
 */

public class RxBus<T> {

    public static final String TAG = "RxBus";

    private static final RxBusBuilder DEFAULT_BUILDER = new RxBusBuilder();

    private final Subject<Object> subject;

    private final boolean throwSubscriberException;
    private final boolean logSubscriberExceptions;
    private final boolean logNoSubscriberMessages;
    private final boolean sendSubscriberExceptionEvent;
    private final boolean sendNoSubscriberEvent;

    static volatile RxBus defaultInstance;

    public static RxBus getDefault() {
        if (null == defaultInstance) {
            synchronized (RxBus.class) {
                if (null == defaultInstance) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance;
    }

    public static RxBusBuilder builder() {
        return new RxBusBuilder();
    }

    public RxBus() {
        this(DEFAULT_BUILDER);
    }

    RxBus(RxBusBuilder builder) {
        subject = builder.subject;
        logSubscriberExceptions = builder.logSubscriberExceptions;
        logNoSubscriberMessages = builder.logNoSubscriberMessages;
        sendSubscriberExceptionEvent = builder.sendSubscriberExceptionEvent;
        sendNoSubscriberEvent = builder.sendNoSubscriberEvent;
        throwSubscriberException = builder.throwSubscriberException;
    }

    /**
     * Registers the given subscriber to receive events. Subscribers must call {@link #unregister(Object)} once they
     * are no longer interested in receiving events.
     */
    public void register(Object subscriber) {
    }

    // Must be called in synchronized block
//    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
//
//    }

    /**
     * Unregisters the given subscriber from all event classes.
     */
    public synchronized void unregister(Object subscriber) {

    }

    public void post(Object obj) {
        subject.onNext(obj);
    }

    public Disposable subscribe(@NonNull final RxBusConsumer<T> onNext,
                                @NonNull final Scheduler scheduler) {

        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");

        return subject
                .ofType(onNext.getType())
                .observeOn(scheduler)
                .subscribe(onNext);

    }

    public Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                @NonNull final RxBusConsumer<T> onNext) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");

        if (!ObjectHelper.equals(filter.getType(), onNext.getType())) {
            throw new RxBusException(
                    String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>",
                            filter.getType().getName(), onNext.getType().getName()));
        }

        return subject
                .ofType(onNext.getType())
                .filter(filter)
                .subscribe(onNext);
    }

    public Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                @NonNull final RxBusConsumer<T> onNext,
                                @NonNull final Scheduler scheduler) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");

        if (!ObjectHelper.equals(filter.getType(), onNext.getType())) {
            throw new RxBusException(
                    String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>",
                            filter.getType().getName(), onNext.getType().getName()));
        }

        return subject
                .ofType(onNext.getType())
                .observeOn(scheduler)
                .subscribe(onNext);

    }

    public Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                @NonNull final RxBusConsumer<T> onNext,
                                @NonNull Scheduler scheduler,
                                @NonNull final Consumer<Throwable> onError) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");

        if (!ObjectHelper.equals(filter.getType(), onNext.getType())) {
            throw new RxBusException(
                    String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>",
                            filter.getType().getName(), onNext.getType().getName()));
        }

        return subject
                .ofType(onNext.getType())
                .observeOn(scheduler)
                .subscribe(onNext, onError);

    }

    public Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                @NonNull final RxBusConsumer<T> onNext,
                                @NonNull Scheduler scheduler,
                                @NonNull final Consumer<Throwable> onError,
                                @NonNull final Action onComplete) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        if (!ObjectHelper.equals(filter.getType(), onNext.getType())) {
            throw new RxBusException(
                    String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>",
                            filter.getType().getName(), onNext.getType().getName()));
        }

        return subject
                .ofType(onNext.getType())
                .observeOn(scheduler)
                .subscribe(onNext, onError, onComplete);

    }


}
