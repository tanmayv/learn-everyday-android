package com.tanmayvijayvargiya.factseveryday.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tanmayvijayvargiya.factseveryday.LearnEverydayApplication;
import com.tanmayvijayvargiya.factseveryday.di.component.ActivityComponent;
import com.tanmayvijayvargiya.factseveryday.di.component.DaggerActivityComponent;

import java.util.UUID;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mComponent;
    private String sessionId;

    public ActivityComponent getComponent(){
        return mComponent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(getLearnEverydayApp().getAppComponent())
                .build();
    }

    public LearnEverydayApplication getLearnEverydayApp(){
        return (LearnEverydayApplication) getApplication();
    }
    public String getSessionId() {
        return sessionId;
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionId = UUID.randomUUID().toString();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelPendingJobsOnStop();
    }

    abstract void cancelPendingJobsOnStop();


}
