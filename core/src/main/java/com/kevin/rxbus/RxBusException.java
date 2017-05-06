package com.kevin.rxbus;

/**
 * An {@link RuntimeException} thrown in cases something went wrong inside RxBus.
 *
 * @author zwenkai on 2017-05-06 15:51:47
 *
 */

public class RxBusException extends RuntimeException {

    public RxBusException(String detailMessage) {
        super(detailMessage);
    }

    public RxBusException(Throwable throwable) {
        super(throwable);
    }

    public RxBusException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
