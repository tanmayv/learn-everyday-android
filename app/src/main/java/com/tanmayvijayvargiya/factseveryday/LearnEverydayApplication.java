package com.tanmayvijayvargiya.factseveryday;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.di.component .DaggerAppComponent;
import com.tanmayvijayvargiya.factseveryday.di.module.AppModule;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.singletons.UserSingleton;

/**
* Created by tanmayvijayvargiya on 09/07/16.
*/
public class LearnEverydayApplication extends Application{

    private AppComponent mAppComponent;

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
//            initSingletons();
//            isUpdateCheck();
        FlowManager.init(new FlowConfig.Builder(this).build());
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        isUpdateCheck();
    }

    protected void initSingletons()
    {
        UserSingleton.initInstance(getApplicationContext());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void isUpdateCheck(){
        int lastVersionCode = SharedPreferencesManager.getCurrentVersionCode(this);

        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            if(lastVersionCode == 0) {
                SharedPreferencesManager.setLoggedInUserid(this,null);
                SharedPreferencesManager.setLoggedInUserName(this, null);
                SharedPreferencesManager.setCurrentVersionCode(this,versionCode);
            }
            else
            if(versionCode > lastVersionCode){
                SharedPreferencesManager.setCurrentVersionCode(this,versionCode);
                SharedPreferencesManager.setLoggedInUserid(this, null);
                SharedPreferencesManager.setLoggedInUserName(this,null);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Shit",e.getMessage());
        }


    }

}
