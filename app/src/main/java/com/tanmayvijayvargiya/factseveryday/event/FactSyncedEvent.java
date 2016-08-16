package com.tanmayvijayvargiya.factseveryday.event;

import com.tanmayvijayvargiya.factseveryday.vo.Fact;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FactSyncedEvent {
    private Fact fact;
    private boolean isSynced;

    public boolean isSynced() {
        return isSynced;
    }

    public FactSyncedEvent(boolean isSynced, Fact fact) {
        this.fact = fact;
        this.isSynced = isSynced;
    }

    public Fact getFact() {
        return fact;
    }
}
