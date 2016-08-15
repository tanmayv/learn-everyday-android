package com.tanmayvijayvargiya.factseveryday.di.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
@Module
public class ActivityModule {
    final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    public Context activityContext() {
        return mActivity;
    }
}