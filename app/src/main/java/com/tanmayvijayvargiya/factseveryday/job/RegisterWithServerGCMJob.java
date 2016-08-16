package com.tanmayvijayvargiya.factseveryday.job;

import com.path.android.jobqueue.Params;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;

import javax.inject.Inject;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class RegisterWithServerGCMJob extends BaseJob {
    String token;
    @Inject
    LearnEverydayService apiService;
    public RegisterWithServerGCMJob(Params params, String token) {
        super(params);
        this.token = token;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        apiService.registerGcmClient(token).execute();
    }

    @Override
    protected void onCancel() {

    }
}
