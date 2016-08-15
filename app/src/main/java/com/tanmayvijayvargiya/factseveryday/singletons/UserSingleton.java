package com.tanmayvijayvargiya.factseveryday.singletons;

import android.content.Context;

import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.vo.User;

/**
 * Created by tanmayvijayvargiya on 09/07/16.
 */
public class UserSingleton {
    public interface Callback{
        void success(User user);
        void error(Throwable e);
    }
    public static UserSingleton instance;

    private User user;
    private UserSingleton(Context context){
        String userId = SharedPreferencesManager.getLoggedInUserId(context);
        if(userId != null) {

        }

    }
    public static synchronized UserSingleton getInstance(){
        return instance;
    }

    public void getLoggedInUser(Context context, final Callback callback){
        if(user != null){
            callback.success(user);
        }else{

        }
    }
    public void setLoggedInUser(User user){
        if(user != null)
            this.user = user;
    }


    public static void initInstance(Context context){
        instance = new UserSingleton(context);
    }
}
