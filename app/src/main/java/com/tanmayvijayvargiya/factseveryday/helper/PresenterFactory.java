package com.tanmayvijayvargiya.factseveryday.helper;

/**
 * Created by tanmayvijayvargiya on 11/07/16.
 */
public interface PresenterFactory<T extends Presenter> {
    T create();
}