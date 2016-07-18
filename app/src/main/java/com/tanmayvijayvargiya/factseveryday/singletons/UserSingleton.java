package com.tanmayvijayvargiya.factseveryday.singletons;

import android.content.Context;
import android.util.Log;

import com.tanmayvijayvargiya.factseveryday.models.User;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;
import com.tanmayvijayvargiya.factseveryday.services.SharedPreferencesManager;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        if(userId != null){
            LearnEverydayService.getInstance().getApi().getUser(userId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            user = new User();
                        }

                        @Override
                        public void onNext(User u) {
                            Log.d("Magic", "User fetched From Server - " + u.getName().fullName());
                            user = u;
                        }
                    });
        }else{
            user = new User();
        }
    }
    public static synchronized UserSingleton getInstance(){
        return instance;
    }

    public void getLoggedInUser(Context context, final Callback callback){
        if(user != null){
            callback.success(user);
        }else{
            String userId = SharedPreferencesManager.getLoggedInUserId(context);
            LearnEverydayService.getInstance().getApi()
                    .getUser(userId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    })
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            callback.error(e);
                        }

                        @Override
                        public void onNext(User u) {
                            if(callback != null){
                                callback.success(user);
                                user = u;
                            }
                        }
                    });
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
