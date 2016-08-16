package com.tanmayvijayvargiya.factseveryday.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    public LearnEverydayService provideApiService(){
        String SERVER_URL = "http://52.66.112.184:8080/";

        Gson gson = new GsonBuilder()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(LearnEverydayService.class);

    }
}
