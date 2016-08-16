package com.tanmayvijayvargiya.factseveryday.event;

import com.tanmayvijayvargiya.factseveryday.vo.Fact;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FactUpdatedEvent {

    private Fact fact;

    public FactUpdatedEvent(Fact fact) {
        this.fact = fact;
    }

    public Fact getFact() {
        return fact;
    }
}
