package com.tanmayvijayvargiya.factseveryday.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanmayvijayvargiya.factseveryday.vo.User;
import com.tanmayvijayvargiya.factseveryday.vo.UserOld;

import java.util.Arrays;
import java.util.LinkedList;
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
    public static LinkedList<String> stringToList(String input, @Nullable String stringSeperator){
        stringSeperator = stringSeperator == null ? StringUtil.stringSeperator : stringSeperator;
        if(input != null){
            String[] strArray = input.split(stringSeperator);
            return new LinkedList<String>(Arrays.asList(strArray));
        }
        return null;
    }

    public static User userFromJson(String input){
//        JsonParser parser = new JsonParser();
//        JsonObject obj = parser.parse(input).getAsJsonObject();
//        User u = new User();
//        String id = obj.get("_id").getAsString();
//        String emailId = obj.get("emailId").getAsString();
//        JsonArray favFactsArray = obj.get("favFacts").getAsJsonArray();
//        Type listType = new TypeToken<List<String>>() {}.getType();
//        List<String> favFacts = new Gson().fromJson(favFactsArray, listType);
//        u.setFavFacts(favFacts);
//        favFacts = u.getFavFacts();
//        if(favFacts == null){
//            Log.d("Parse","Unable to parse fav facts");
//        }else{
//            for(String fav : favFacts){
//                Log.d("Parse","Fav " + fav);
//            }
//        }
//        u.set_id(id);
//        u.setEmailId(emailId);
//        u.setFavFacts(favFacts);
        Gson gson = new GsonBuilder()

                .create();
        Log.d("Parse", "Input User " + input);

        User user = gson.fromJson(input, User.class);
        List<String>favFacts = user.getFavFacts();
        if(favFacts == null){
            Log.d("Parse", "Unable to parse fav facts");
        }else{
            for(String fav : favFacts){
                Log.d("Parse","Fav " + fav);
            }
        }
        UserOld userOld = gson.fromJson(input,UserOld.class);
        favFacts = userOld.getFavFacts();
        if(favFacts == null){
            Log.d("Parse", "Unable to parse fav facts" + userOld.getEmailId());
        }else{
            for(String fav : favFacts){
                Log.d("Parse","Fav " + fav);
            }
        }
        return user;
    }
}
