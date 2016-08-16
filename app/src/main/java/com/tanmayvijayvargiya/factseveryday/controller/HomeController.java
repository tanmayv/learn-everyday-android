package com.tanmayvijayvargiya.factseveryday.controller;

import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class HomeController {
    AppComponent mAppComponent;
    @Inject
    EventBus mEventBus;

    public HomeController(AppComponent appComponent){
        appComponent.inject(this);
        mEventBus.register(this);
    }

    public void onFavClick(Fact fact){

    }

    public void onRefresh(){

    }

}
