package com.tanmayvijayvargiya.factseveryday.utils;

import android.util.Log;

import java.util.Date;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class TimeUtil {
    private static long lastReload = 0;
    public static boolean shouldReload(){

        long current = new Date().getTime();

        if((current - lastReload) > 2000){

            Log.d("Time","Reload Allowed " + lastReload);
            lastReload = current;
            return true;
        }else{
            Log.d("Time","Reload Denied" + lastReload);

            lastReload = current;
            return false;
        }
    }

}

