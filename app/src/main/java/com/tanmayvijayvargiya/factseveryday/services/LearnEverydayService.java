package com.tanmayvijayvargiya.factseveryday.services;

import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by tanmayvijayvargiya on 02/07/16.
 */
public interface LearnEverydayService {

    @GET("/")
    public Call<List<Fact>> queryFacts(@Query("q") String queryString);

    @GET("/api/facts")
    public Call<List<Fact>> getFacts();
    @GET("/api/facts/after/{timestampFrom}")
    public Call<List<Fact>> getFactsBetweenTimestamp(@Path("timestampFrom") String timestampFrom);

    @POST("/api/facts")
    public Call<Fact> createFact(@Body Fact fact);

    @GET("/api/facts/{factId}")
    public Call<Fact> getFact(@Path("factId") String factId);

    @POST("/api/facts/{factId}")
    public Call<Fact> updateFact(@Path("factId") String factId, @Body Fact fact);

    @POST("/api/users")
    public Call<User> createUser(@Body User user);

    @POST("/api/users/{userId}")
    public Call<User> updateUser(@Path("userId") String userId, @Body User user);

    @GET("/api/users/{userId}")
    public Call<User> getUser(@Path("userId") String userId);

    @POST("/api/gcm/register/{regId}")
    public Call<Response> registerGcmClient(@Path("regId") String registrationId);

}