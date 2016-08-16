package com.tanmayvijayvargiya.factseveryday.job;

import android.util.Log;

import com.path.android.jobqueue.Params;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.event.FactSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.event.FactUpdatedEvent;
import com.tanmayvijayvargiya.factseveryday.event.UserSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.model.FactModel;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class UpdateFactJob extends BaseJob {
    Fact mFact;
    String factId;
    boolean isFavorite;
    @Inject
    FactModel mFactModel;
    @Inject
    EventBus mEventBus;
    @Inject
    LearnEverydayService apiService;
    @Inject
    UserModel mUserModel;

    public UpdateFactJob(Params params, String factId, boolean isFavorite) {
        super(params);
        this.factId = factId;
        this.isFavorite = isFavorite;
    }

    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    @Override
    public void onAdded() {
        mFact = mFactModel.loadById(factId);
        mFact.setIsFavorite(isFavorite);
        mFact.isSynced = false;
        mFactModel.save(mFact);

        User user = mUserModel.getLoggedInUser();
        Log.d("Fav user ", "BEFORE " + user.getDbFavFactsList());
        List<String> favFacts = user.getFavFacts();
        List<String> writableFavFacts = new LinkedList<String>();
        if(favFacts == null){
            if(mFact.isFavorite) {
                Log.d("Fav user ", mFact.getTitle() + " Added to fav");
                writableFavFacts.add(mFact.get_id());
            }

        }else{
            writableFavFacts.addAll(favFacts);

            boolean contains = favFacts.contains(mFact.get_id());

            if(contains && !mFact.isFavorite){
                Log.d("Fav user ", mFact.getTitle() + " Removed from fav");
                writableFavFacts.remove(mFact.get_id());
            }else{
                if(!contains && mFact.isFavorite){

                    Log.d("Fav user ", mFact.getTitle() + " Added to fav " + mFact.get_id());
                    writableFavFacts.add(mFact.get_id());

                }
            }

        }
        user.setFavFacts((LinkedList<String>)writableFavFacts);
        Log.d("Fav user ", "AFTER DISK " + user.getDbFavFactsList());
        mUserModel.setLoggedInUser(user);
        mEventBus.post(new FactUpdatedEvent(mFact));
    }

    @Override
    public void onRun() throws Throwable {
        mFact = mFactModel.loadById(factId);

        if(mFact != null){
            User user = mUserModel.getLoggedInUser();
            user = apiService.updateUser(user.get_id(),user).execute().body();
            user.setFavFacts(user.getFavFacts());
            if(user != null){
                mFact.isSynced = true;
                mFactModel.save(mFact);
                mEventBus.post(new UserSyncedEvent(user));
            }

        }else{
            mEventBus.post(new FactSyncedEvent(false, mFact));
        }
    }

    @Override
    protected void onCancel() {
        mFact = mFactModel.loadById(factId);
        mEventBus.post(new FactSyncedEvent(false, mFact));
    }
}
