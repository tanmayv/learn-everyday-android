package com.tanmayvijayvargiya.factseveryday.job;

import android.util.Log;

import com.path.android.jobqueue.Params;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.event.UserSyncedEvent;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class SyncUserJob extends BaseJob {

    @Inject
    LearnEverydayService apiService;

    @Inject
    UserModel userModel;

    @Inject
    EventBus eventBus;
    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    public SyncUserJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        User user = userModel.getLoggedInUser();
        String username = user.getName().fullName();
        user = apiService.updateUser(user.get_id(), user).execute().body();
        user.setFavFacts(user.getFavFacts());
        user.setFullName(username,"");
        userModel.setLoggedInUser(user);
        Log.d("User", "Firing Event");
        eventBus.post(new UserSyncedEvent(user));
    }

    @Override
    protected void onCancel() {

    }
}
