package com.tanmayvijayvargiya.factseveryday.di.component;

import android.content.Context;

import com.path.android.jobqueue.JobManager;
import com.tanmayvijayvargiya.factseveryday.controller.HomeController;
import com.tanmayvijayvargiya.factseveryday.di.module.ApiModule;
import com.tanmayvijayvargiya.factseveryday.di.module.AppModule;
import com.tanmayvijayvargiya.factseveryday.job.FetchFactsJob;
import com.tanmayvijayvargiya.factseveryday.job.FetchUserJob;
import com.tanmayvijayvargiya.factseveryday.job.UpdateFactJob;
import com.tanmayvijayvargiya.factseveryday.model.FactModel;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface AppComponent {

    EventBus eventBus();
    LearnEverydayService getApiService();
    Context appContext();
    UserModel userModel();
    JobManager jobManager();
    FactModel factModel();

    void inject(UserModel userModel);
    void inject(FetchFactsJob fetchFactsJob);
    void inject(FactModel factModel);
    void inject(UpdateFactJob updateFactJob);
    void inject(HomeController homeController);
    void inject(FetchUserJob fetchUserJob);
}
