package com.tanmayvijayvargiya.factseveryday.utils;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tanmayvijayvargiya on 15/08/16.
 */
public class StringUtil {

    private static String stringSeperator = "___";
    public static String listToString(List<String> input, @Nullable String stringSeperator){
        StringBuffer buffer = new StringBuffer();
        stringSeperator = stringSeperator == null ? StringUtil.stringSeperator : stringSeperator;
        if(input != null){
            for(String str: input){
                buffer.append(str + stringSeperator);
            }
        }

        return buffer.toString();
    }

    @Nullable
    public static List<String> stringToList(String input, @Nullable String stringSeperator){
        stringSeperator = stringSeperator == null ? StringUtil.stringSeperator : stringSeperator;
        if(input != null){
            String[] strArray = input.split(stringSeperator);
            return Arrays.asList(strArray);
        }
        return null;
    }
}
