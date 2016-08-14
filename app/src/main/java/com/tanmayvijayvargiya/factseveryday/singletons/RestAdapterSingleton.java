package com.tanmayvijayvargiya.factseveryday.singletons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by tanmayvijayvargiya on 09/07/16.
 */
public class RestAdapterSingleton {
    private static RestAdapterSingleton ourInstance = new RestAdapterSingleton();

    private RestAdapter restAdapter;
    private static final String SERVER_URL = "http://52.66.112.184:8080/";

    public static RestAdapterSingleton getInstance() {
        return ourInstance;
    }

    public static void initInstance(){
        ourInstance = new RestAdapterSingleton();
    }

    private RestAdapterSingleton() {

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
    }

    public RestAdapter getRestAdapter(){
        return restAdapter;
    }
}
