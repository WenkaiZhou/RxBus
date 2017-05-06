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

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

import static android.R.attr.filter;

/**
 * Created by zwenkai on 2017/5/6.
 */

public class RxBus {

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
        pust(obj, 0);
    }

    public void pust(Object obj, int tag) {
        subject.onNext(new ObserverObject(obj, tag));
    }

    public <T> Disposable subscribe(@NonNull final Consumer<T> onNext) {

        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o instanceof ObserverObject;
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .subscribe(onNext);

    }

    public <T> Disposable subscribe(@NonNull final Predicate<T> filter,
                                    @NonNull final Consumer<T> onNext) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o instanceof ObserverObject;
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .filter(filter)
                .subscribe(onNext);

    }

    public <T> Disposable subscribe(@NonNull final Predicate<T> filter,
                                    @NonNull final Consumer<T> onNext,
                                    @NonNull Scheduler scheduler) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o instanceof ObserverObject;
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .filter(filter)
                .observeOn(scheduler)
                .subscribe(onNext);

    }

    public <T> Disposable subscribe(@NonNull final Predicate<T> filter,
                                    @NonNull final Consumer<T> onNext,
                                    @NonNull Scheduler scheduler,
                                    @NonNull final Consumer<Throwable> onError) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o instanceof ObserverObject;
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .filter(filter)
                .observeOn(scheduler)
                .subscribe(onNext, onError);

    }

    public <T> Disposable subscribe(@NonNull final Predicate<T> filter,
                                    @NonNull final Consumer<T> onNext,
                                    @NonNull Scheduler scheduler,
                                    @NonNull final Consumer<Throwable> onError,
                                    @NonNull final Action onComplete) {

        ObjectHelper.requireNonNull(filter, "filter is null");
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        return o instanceof ObserverObject;
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .filter(filter)
                .observeOn(scheduler)
                .subscribe(onNext, onError, onComplete);

    }


    public <T> Disposable subscribe(@NonNull final Predicate<ObserverObject<T>> filter,
                                    @NonNull final Consumer<T> onNext) {

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        if (!(o instanceof ObserverObject)) {
                            return false;
                        }

                        return filter.test((ObserverObject<T>) o);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .subscribe(onNext);
    }

    public <T> Disposable subscribe(@NonNull final Predicate<ObserverObject<T>> filter,
                                    @NonNull final Consumer<T> onNext,
                                    final Scheduler scheduler) {

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        if (!(o instanceof ObserverObject)) {
                            return false;
                        }

                        return filter.test((ObserverObject<T>) o);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .observeOn(scheduler)
                .subscribe(onNext);

    }

    public <T> Disposable subscribe(@Nullable final Predicate<ObserverObject<T>> filter,
                                    @NonNull final Consumer<T> onNext,
                                    @Nullable Scheduler scheduler,
                                    @Nullable final Consumer<Throwable> onError) {

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        if (!(o instanceof ObserverObject)) {
                            return false;
                        }

                        return filter.test((ObserverObject<T>) o);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .observeOn(scheduler)
                .subscribe(onNext, onError);

    }

    public <T> Disposable subscribe(@Nullable final Predicate<ObserverObject<T>> filter,
                                    @NonNull final Consumer<T> onNext,
                                    @Nullable Scheduler scheduler,
                                    @Nullable final Consumer<Throwable> onError,
                                    @Nullable final Action onComplete) {

        return subject
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(@NonNull Object o) throws Exception {
                        if (!(o instanceof ObserverObject)) {
                            return false;
                        }

                        return filter.test((ObserverObject<T>) o);
                    }
                })
                .map(new Function<Object, T>() {
                    @Override
                    public T apply(@NonNull Object o) throws Exception {
                        return ((ObserverObject<T>) o).obj;
                    }
                })
                .observeOn(scheduler)
                .subscribe(onNext, onError, onComplete);

    }

    private static final class ObserverObject<T> {

        public T obj;
        public int tag;

        public ObserverObject(T obj, int tag) {
            this.obj = obj;
            this.tag = tag;
        }

    }

}
