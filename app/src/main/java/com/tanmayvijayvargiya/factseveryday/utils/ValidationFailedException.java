package com.tanmayvijayvargiya.factseveryday.utils;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException() {
    }

    public ValidationFailedException(String message){
        super(message);
    }
}
