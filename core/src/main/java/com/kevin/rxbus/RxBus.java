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

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.subjects.Subject;

/**
 * Created by zwenkai on 2017/5/6.
 */

public class RxBus {

    public static final String TAG = "RxBus";

    private static final RxBusBuilder DEFAULT_BUILDER = new RxBusBuilder();

    private final Subject<Object> mSubject;
    private final Map<Class<?>, Object> mStickyMap;

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
        mSubject = builder.subject;
        mStickyMap = builder.mStickyMap;
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
        mSubject.onNext(obj);
    }

    public void postSticky(Object obj) {
        synchronized (mStickyMap) {
            mStickyMap.put(obj.getClass(), obj);
        }
        mSubject.onNext(obj);
    }

    public <T> Disposable subscribe(@NonNull final RxBusConsumer<T> onNext) {

        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return doSubscribe(null, onNext, null, null, null);
    }

    public <T> Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                    @NonNull final RxBusConsumer<T> onNext) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return doSubscribe(filter, onNext, null, null, null);
    }

    public <T> Disposable subscribe(@NonNull final RxBusPredicate<T> filter,
                                    @NonNull final RxBusConsumer<T> onNext,
                                    @NonNull final Scheduler scheduler,
                                    @NonNull final Consumer<Throwable> onError,
                                    @NonNull final Action onComplete) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        return doSubscribe(filter, onNext, scheduler, onError, onComplete);

    }

    private <T> Disposable doSubscribe(final RxBusPredicate<T> filter,
                                       final RxBusConsumer<T> onNext,
                                       Scheduler scheduler,
                                       final Consumer<Throwable> onError,
                                       final Action onComplete) {

        if (null != filter && null != onNext && !ObjectHelper.equals(filter.getType(), onNext.getType())) {
            throw new RxBusException(
                    String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>.",
                            filter.getType().getName(), onNext.getType().getName()));
        }

        Observable<T> observable = mSubject.ofType(onNext.getType());
        if (null != filter) {
            observable.filter(filter);
        }
        if (null != scheduler) {
            observable.observeOn(scheduler);
        }
        if (null != onError && null != onComplete) {
            return observable.subscribe(onNext, onError, onComplete);
        } else {
            return observable.subscribe(onNext);
        }

    }

    // ------------------- Sticky Subscribe -----------------------

    public <T> Disposable subscribeSticky(@NonNull final RxBusConsumer<T> onNext) {

        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return doSubscribeSticky(null, onNext, null, null, null);
    }

    public <T> Disposable subscribeSticky(@NonNull final RxBusPredicate<T> filter,
                                          @NonNull final RxBusConsumer<T> onNext) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return doSubscribeSticky(filter, onNext, null, null, null);
    }

    public <T> Disposable subscribeSticky(@NonNull final RxBusPredicate<T> filter,
                                          @NonNull final RxBusConsumer<T> onNext,
                                          @NonNull final Scheduler scheduler,
                                          @NonNull final Consumer<Throwable> onError,
                                          @NonNull final Action onComplete) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        return doSubscribeSticky(filter, onNext, scheduler, onError, onComplete);
    }

    private <T> Disposable doSubscribeSticky(final RxBusPredicate<T> filter,
                                             final RxBusConsumer<T> onNext,
                                             final Scheduler scheduler,
                                             final Consumer<Throwable> onError,
                                             final Action onComplete) {

        synchronized (mStickyMap) {

            if (null != filter && null != onNext && !ObjectHelper.equals(filter.getType(), onNext.getType())) {
                throw new RxBusException(
                        String.format("RxBusPredicate<%1$s>'s generic don`t match the RxBusConsumer<%2$s>.",
                                filter.getType().getName(), onNext.getType().getName()));
            }

            Observable<T> observable = mSubject
                    .ofType(onNext.getType())
                    .filter(new Predicate<T>() {
                        @Override
                        public boolean test(@NonNull T t) throws Exception {
                            return null != mStickyMap.get(onNext.getType());
                        }
                    });
            if (null != filter) {
                observable.filter(filter);
            }
            observable.mergeWith(new Observable<T>() {
                @Override
                protected void subscribeActual(Observer<? super T> observer) {
                    observer.onNext(onNext.getType().cast(mStickyMap.get(onNext.getType())));
                }
            });
            if (null != scheduler) {
                observable.observeOn(scheduler);
            }
            if (null != onError && null != onComplete) {
                return observable.subscribe(onNext, onError, onComplete);
            } else {
                return observable.subscribe(onNext);
            }
        }

    }

    public boolean removeSticky(Object event) {
        synchronized (mStickyMap) {
            Class<?> eventType = event.getClass();
            Object existingEvent = mStickyMap.get(eventType);
            if (event.equals(existingEvent)) {
                mStickyMap.remove(eventType);
                return true;
            } else {
                return false;
            }
        }
    }

    public void removeAllSticky() {
        synchronized (mStickyMap) {
            mStickyMap.clear();
        }
    }


}
