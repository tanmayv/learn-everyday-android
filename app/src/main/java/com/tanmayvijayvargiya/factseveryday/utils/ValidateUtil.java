package com.tanmayvijayvargiya.factseveryday.utils;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class ValidateUtil {

    public static List<?extends Validate> pruneInvalids(List<?extends Validate> list){
        for(Validate v : list){
            try{
                v.validate();
            }catch (ValidationFailedException e){
                list.remove(v);
            }
        }

        return list;
    }
}
