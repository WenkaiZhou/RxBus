package com.kevin.rxbus;


import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Creates RxBus instances with custom parameters and also allows to install a custom default RxBus instance.
 * Create a new builder using {@link RxBus#builder()}.
 */

public class RxBusBuilder {

    final Subject<Object> subject;

    boolean logSubscriberExceptions = true;
    boolean logNoSubscriberMessages = true;
    boolean sendSubscriberExceptionEvent = true;
    boolean sendNoSubscriberEvent = true;
    boolean throwSubscriberException;

    RxBusBuilder() {
        subject = PublishSubject.create().toSerialized();
    }

    /** Default: true */
    public RxBusBuilder logSubscriberExceptions(boolean logSubscriberExceptions) {
        this.logSubscriberExceptions = logSubscriberExceptions;
        return this;
    }

    /** Default: true */
    public RxBusBuilder logNoSubscriberMessages(boolean logNoSubscriberMessages) {
        this.logNoSubscriberMessages = logNoSubscriberMessages;
        return this;
    }

    /** Default: true */
    public RxBusBuilder sendSubscriberExceptionEvent(boolean sendSubscriberExceptionEvent) {
        this.sendSubscriberExceptionEvent = sendSubscriberExceptionEvent;
        return this;
    }

    /** Default: true */
    public RxBusBuilder sendNoSubscriberEvent(boolean sendNoSubscriberEvent) {
        this.sendNoSubscriberEvent = sendNoSubscriberEvent;
        return this;
    }

    /**
     * Fails if an subscriber throws an exception (default: false).
     * <p/>
     * Tip: Use this with BuildConfig.DEBUG to let the app crash in DEBUG mode (only). This way, you won't miss
     * exceptions during development.
     */
    public RxBusBuilder throwSubscriberException(boolean throwSubscriberException) {
        this.throwSubscriberException = throwSubscriberException;
        return this;
    }

    /**
     * Installs the default EventBus returned by {@link RxBus#getDefault()} using this builders' values. Must be
     * done only once before the first usage of the default EventBus.
     *
     * @throws RxBusException if there's already a default EventBus instance in place
     */
    public RxBus installDefaultEventBus() {
        synchronized (RxBus.class) {
            if (RxBus.defaultInstance != null) {
                throw new RxBusException("Default instance already exists." +
                        " It may be only set once before it's used the first time to ensure consistent behavior.");
            }
            RxBus.defaultInstance = build();
            return RxBus.defaultInstance;
        }
    }

    /** Builds an EventBus based on the current configuration. */
    public RxBus build() {
        return new RxBus(this);
    }

}
