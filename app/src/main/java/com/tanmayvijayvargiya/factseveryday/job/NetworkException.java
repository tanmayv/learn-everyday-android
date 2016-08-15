package com.tanmayvijayvargiya.factseveryday.job;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class NetworkException extends RuntimeException {

    private final int mErrorCode;

    public NetworkException(int errorCode) {
        mErrorCode = errorCode;
    }

    public boolean shouldRetry() {
        return mErrorCode < 400 || mErrorCode > 499;
    }
}