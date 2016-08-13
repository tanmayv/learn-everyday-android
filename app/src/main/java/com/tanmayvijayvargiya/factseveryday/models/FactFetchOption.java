package com.tanmayvijayvargiya.factseveryday.models;

/**
 * Created by tanmayvijayvargiya on 12/08/16.
 */
public class FactFetchOption {
    private String timestampFrom;
    private int limit;

    public FactFetchOption(String timestampFrom, int limit){
        this.timestampFrom = timestampFrom;
        this.limit = limit;
    }
}
