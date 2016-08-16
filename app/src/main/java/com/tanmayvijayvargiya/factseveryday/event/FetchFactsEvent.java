package com.tanmayvijayvargiya.factseveryday.event;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FetchFactsEvent {
    private boolean isSuccess;

    public FetchFactsEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
