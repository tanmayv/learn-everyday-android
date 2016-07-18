package com.tanmayvijayvargiya.factseveryday.helper;

/**
 * Created by tanmayvijayvargiya on 11/07/16.
 */
public interface Presenter<V>{
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}