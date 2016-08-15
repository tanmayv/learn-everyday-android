package com.tanmayvijayvargiya.factseveryday.services;

import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by tanmayvijayvargiya on 02/07/16.
 */
public interface LearnEverydayService {

    @GET("/")
    public Observable<List<Fact>> queryFacts(@Query("q") String queryString);

    @GET("/api/facts")
    public List<Fact> getFacts();
    @GET("/api/facts/after/{timestampFrom}")
    public List<Fact> getFactsBetweenTimestamp(@Path("timestampFrom") String timestampFrom);

    @POST("/api/facts")
    public Fact createFact(@Body Fact fact);

    @GET("/api/facts/{factId}")
    public Observable<Fact> getFact(@Path("factId") String factId);

    @POST("/api/facts/{factId}")
    public Observable<Fact> updateFact(@Path("factId") String factId, @Body Fact fact);

    @POST("/api/users")
    public Observable<User> createUser(@Body User user);

    @POST("/api/users/{userId}")
    public Observable<User> updateUser(@Path("userId") String userId, @Body User user);

    @GET("/api/users/{userId}")
    public Observable<User> getUser(@Path("userId") String userId);

    @POST("/api/gcm/register/{regId}")
    public void registerGcmClient(@Path("regId") String registrationId, Callback<String> callback);

}