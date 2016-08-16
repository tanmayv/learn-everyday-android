package com.tanmayvijayvargiya.factseveryday.job;

import android.util.Log;

import com.path.android.jobqueue.Params;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.event.FetchFactsEvent;
import com.tanmayvijayvargiya.factseveryday.model.FactModel;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FetchFactsJob extends BaseJob{

    @Inject
    LearnEverydayService apiService;
    @Inject
    FactModel mFactModel;
    @Inject
    UserModel mUserModel;
    @Inject
    EventBus mEventBus;

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
        List<Fact> facts = apiService.getFacts().execute().body();
        User user = mUserModel.getLoggedInUser();
        Log.d("Job","FetchFacts Job Running Now." + user.getFavFacts().toString());

        if(user != null){
            List<String> favs = user.getFavFacts();

            if(favs != null && facts != null) {
                for(Fact fact: facts){
                    fact.isSynced = true;
                    if(favs.contains(fact.get_id())){
                        fact.isFavorite = true;
                        Log.d("Feed","Fav of user " + fact.getTitle());
                    }
                }
            }
        }
        mFactModel.saveAll(facts);
        mEventBus.post(new FetchFactsEvent(true));
    }

    @Override
    protected void onCancel() {
        mEventBus.post(new FetchFactsEvent(false));
    }
}
