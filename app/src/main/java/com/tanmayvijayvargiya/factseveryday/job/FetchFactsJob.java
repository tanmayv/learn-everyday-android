package com.tanmayvijayvargiya.factseveryday.job;

import android.util.Log;

import com.path.android.jobqueue.Params;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FetchFactsJob extends BaseJob{

    @Inject
    LearnEverydayService apiService;

    public FetchFactsJob(Params params) {
        super(params);
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {
        Log.d("Job","FetchFacts Job added.");
    }

    @Override
    public void onRun() throws Throwable {

        Log.d("Job","FetchFacts Job Running Now.");
        List<Fact> facts = apiService.getFacts();
        for(Fact fact : facts){
            Log.d("Job Network"," Fact " + fact.getTitle());
        }
    }

    @Override
    protected void onCancel() {

    }
}
