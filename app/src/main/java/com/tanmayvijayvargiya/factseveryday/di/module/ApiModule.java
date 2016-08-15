package com.tanmayvijayvargiya.factseveryday.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanmayvijayvargiya.factseveryday.services.LearnEverydayService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
@Module
public class ApiModule {

    @Provides
    @Singleton
    public LearnEverydayService provideApiService(){
        RestAdapter restAdapter;
        String SERVER_URL = "http://52.66.112.184:8080/";
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        Gson gson = new GsonBuilder()
                .create();

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .build();
        return restAdapter.create(LearnEverydayService.class);

    }
}
