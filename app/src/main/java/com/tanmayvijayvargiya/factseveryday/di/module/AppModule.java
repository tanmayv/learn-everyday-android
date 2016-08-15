package com.tanmayvijayvargiya.factseveryday.di.module;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.tanmayvijayvargiya.factseveryday.LearnEverydayApplication;
import com.tanmayvijayvargiya.factseveryday.job.BaseJob;
import com.tanmayvijayvargiya.factseveryday.model.UserModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
@Module
public class AppModule {
    private final LearnEverydayApplication mApp;

    public AppModule(LearnEverydayApplication app){
        this.mApp  = app;
    }

    @Provides
    @Singleton
    public Context context(){
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    @Singleton
    public JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApp)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.getAppComponent());
                        }
                    }
                })
                .build();
        return new JobManager(mApp, config);
    }

    @Provides
    @Singleton
    public NotificationManagerCompat notificationCompat() {
        return NotificationManagerCompat.from(mApp);
    }

    @Provides
    @Singleton
    public UserModel provideUserModel(){
        return  new UserModel(mApp.getAppComponent());
    }
}
