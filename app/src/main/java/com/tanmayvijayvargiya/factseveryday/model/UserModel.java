package com.tanmayvijayvargiya.factseveryday.model;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
public class UserModel {
    private AppComponent mAppComponent;

    public UserModel(AppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
        mAppComponent.inject(this);
    }

    public void setLoggedInUser(User user){

        List<User> oldUsers =  new Select().from(User.class).queryList();
        user.validate();
        for(User user1 : oldUsers){
            user1.delete();
        }
        user.save();
    }

    public User getLoggedInUser(){
        return new Select().from(User.class).querySingle();
    }

    public void logoutCurrentUser(){
        List<User> users = new Select().from(User.class).queryList();
        for(User user1 : users){
            user1.delete();
        }
    }
}
