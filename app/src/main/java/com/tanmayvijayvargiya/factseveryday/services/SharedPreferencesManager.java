package com.tanmayvijayvargiya.factseveryday.services;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */
public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "LEARN_EVERY_DAY_SETTING";


    // properties
    private static final String LOGGED_IN_USERID = "LED_USER_LOGGED_IN";
    private static final String LOGGED_IN_USERNAME = "LED_USER_LOGGED_NAME";
    private static final String CURRENT_VERSION_CODE = "CURRENT_VERSION_CODE";
    // other properties...


    private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getLoggedInUserId(Context context) {
        return getSharedPreferences(context).getString(LOGGED_IN_USERID, null);
    }

    public static void setLoggedInUserid(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LOGGED_IN_USERID, newValue);
        editor.commit();
    }

    public static String getLoggedInUserName(Context context) {
        return getSharedPreferences(context).getString(LOGGED_IN_USERNAME, null);
    }

    public static void setLoggedInUserName(Context context, String newValue) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(LOGGED_IN_USERNAME, newValue);
        editor.commit();
    }

    public static int getCurrentVersionCode(Context context){
        return getSharedPreferences(context).getInt(CURRENT_VERSION_CODE,0);
    }

    public static void setCurrentVersionCode(Context context, int versionCode){
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(CURRENT_VERSION_CODE, versionCode);
        editor.commit();
    }

}
