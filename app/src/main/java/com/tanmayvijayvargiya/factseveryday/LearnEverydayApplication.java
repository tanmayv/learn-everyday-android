package com.tanmayvijayvargiya.factseveryday;

import android.app.Application;

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
        }

        protected void initSingletons()
        {
            RestAdapterSingleton.initInstance();
            UserSingleton.initInstance(getApplicationContext());
        }

}
