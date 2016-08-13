package com.tanmayvijayvargiya.factseveryday.services;

import com.tanmayvijayvargiya.factseveryday.models.Fact;
import com.tanmayvijayvargiya.factseveryday.models.User;
import com.tanmayvijayvargiya.factseveryday.singletons.RestAdapterSingleton;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by tanmayvijayvargiya on 02/07/16.
 */
public class LearnEverydayService {

    private static final String SERVER_URL = "http://52.66.112.184:8080/api";
    private Api mApi;
    private static LearnEverydayService instance;

    public static LearnEverydayService getInstance(){
        if(instance == null){
            instance = new LearnEverydayService();
        }
        return instance;
    }

    private LearnEverydayService() {

        mApi = RestAdapterSingleton.getInstance().getRestAdapter().create(Api.class);
    }


    public  Api getApi() {

        return mApi;
    }

    public interface Api {

        @GET("/facts")
        public Observable<List<Fact>> getFacts();
        @GET("/facts/after/{timestampFrom}")
        public Observable<List<Fact>> getFactsBetweenTimestamp(@Path("timestampFrom") String timestampFrom);

        @POST("/facts")
        public Observable<Fact> createFact(@Body Fact fact);

        @GET("/facts/{factId}")
        public Observable<Fact> getFact(@Path("factId") String factId);

        @POST("/facts/{factId}")
        public Observable<Fact> updateFact(@Path("factId") String factId, @Body Fact fact);

        @POST("/users")
        public Observable<User> createUser(@Body User user);

        @POST("/users/{userId}")
        public Observable<User> updateUser(@Path("userId") String userId, @Body User user);

        @GET("/users/{userId}")
        public Observable<User> getUser(@Path("userId") String userId);

        @POST("/gcm/register/{regId}")
        public void registerGcmClient(@Path("regId") String registrationId, Callback<String> callback);

    }
}