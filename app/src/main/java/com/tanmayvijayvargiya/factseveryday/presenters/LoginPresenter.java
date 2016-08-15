package com.tanmayvijayvargiya.factseveryday.presenters;

import android.util.Log;

import com.tanmayvijayvargiya.factseveryday.models.User;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;
import com.tanmayvijayvargiya.factseveryday.singletons.UserSingleton;
import com.tanmayvijayvargiya.factseveryday.views.LoginActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by tanmayvijayvargiya on 04/07/16.
 */
public class LoginPresenter {
    private LoginActivity view;
    private LearnEverydayService apiService;
    public LoginPresenter(LoginActivity view){
        this.view = view;
        apiService = LearnEverydayService.getInstance();
    }

    public void init(){

    }
    public void signUp(final String firstName, final String lastName, String email){
        User user = new User();
        user.setFullName(firstName,lastName);
        user.setEmailId(email);


        apiService.getApi()
                .createUser(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        String userid = user.get_id();
                        if(userid != null) {
                            SharedPreferencesManager.setLoggedInUserid(view, userid);
                            SharedPreferencesManager.setLoggedInUserName(view, firstName + " " + lastName);
                            UserSingleton.getInstance().setLoggedInUser(user);
                            view.navigateToHome(user);
                        }
                        else
                            Log.e("Shit", "Cannot create User.");

                    }
                });
    }
}
