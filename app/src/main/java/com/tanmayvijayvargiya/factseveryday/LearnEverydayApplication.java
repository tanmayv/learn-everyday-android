package com.tanmayvijayvargiya.factseveryday;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.singletons.RestAdapterSingleton;
import com.tanmayvijayvargiya.factseveryday.singletons.UserSingleton;

/**
 * Created by tanmayvijayvargiya on 09/07/16.
 */
public class LearnEverydayApplication extends Application{


        @Override
        public void onCreate()
        {
            super.onCreate();

            // Initialize the singletons so their instances
            // are bound to the application process.
            initSingletons();
            isUpdateCheck();

        }

        protected void initSingletons()
        {
            RestAdapterSingleton.initInstance();
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
                        Log.d("Shit","RESET BITCH " + lastVersionCode);
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
