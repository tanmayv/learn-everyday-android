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
public class FetchUserJob extends BaseJob {

    String userId, userName, userEmail;
    @Inject
    LearnEverydayService apiService;
    @Inject
    UserModel userModel;
    @Inject
    EventBus eventBus;

    User user;
    @Override
    public void inject(AppComponent appComponent) {
        super.inject(appComponent);
        appComponent.inject(this);
    }

    public FetchUserJob(Params params, String userName, String userEmail) {
        super(params);
        this.userName = userName;
        this.userEmail = userEmail;
    }

    @Override
    public void onAdded() {
        user = new User();
        user.setFullName(userName,"");
        user.setEmailId(userEmail);
        user.set_id("TEMPID");
        userModel.setLoggedInUser(user);

    }

    @Override
    public void onRun() throws Throwable {
        user = userModel.getLoggedInUser();
        user.set_id(null);
        String username = user.getName().fullName();
        user= apiService.createUser(user).execute().body();
        user.setFavFacts(user.getFavFacts());
        user.setFullName(username,"");
        userModel.setLoggedInUser(user);
        Log.d("User", "Firing Event");
        eventBus.post(new UserSyncedEvent(user));
    }

    @Override
    protected void onCancel() {
        Log.d("Event","Cancelled event");
    }
}
