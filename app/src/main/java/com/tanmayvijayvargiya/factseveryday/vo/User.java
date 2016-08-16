package com.tanmayvijayvargiya.factseveryday.vo;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tanmayvijayvargiya.factseveryday.model.AppDatabase;
import com.tanmayvijayvargiya.factseveryday.utils.StringUtil;
import com.tanmayvijayvargiya.factseveryday.utils.Validate;
import com.tanmayvijayvargiya.factseveryday.utils.ValidationFailedException;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */


@Table(database = AppDatabase.class)
public class User extends BaseModel implements Validate{

    @Column
    @PrimaryKey
    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private Name name;

    @Column(name = "name")
    private String dbName;

    @Column
    @SerializedName("emailId")
    private String emailId;

    @SerializedName("favFacts")
    private List<String> favFacts;

    @Column(name="favFacts")
    private String dbFavFactsList;


    public class Name{
        @SerializedName("first")
        private String first;

        @SerializedName("last")
        private String last;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            last = last == null ? "" : last;
            this.last = last;
        }

        public String fullName(){

            return first + " " + last;
        }
    }

   @Column
    private String profilePicUrl;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
        Name name = new Name();
        name.setFirst(dbName);
        this.setName(name);
    }


    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {

        Log.d("User","User setter running");
        this._id = _id;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }



    public List<String> getFavFacts() {
        return favFacts;
    }

    public void setFavFacts(List<String> favFacts) {
        Log.d("User","User favFacts setter running");
        this.favFacts = favFacts;
        //this.dbFavFactsList = StringUtil.listToString(favFacts,null);
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDbFavFactsList() {
        return dbFavFactsList;
    }

    public void setDbFavFactsList(String dbFavFactsList) {
        this.dbFavFactsList = dbFavFactsList;
        this.favFacts = StringUtil.stringToList(dbFavFactsList, null);
    }

    public void setFullName(String first, String last){
        Name name = new Name();
        name.setFirst(first);
        name.setLast(last);
        this.setName(name);
        this.dbName = name.fullName();
    }

    @Override
    public void validate() {
        if(this.get_id() == null){

            throw new ValidationFailedException("User id is NULL");
        }

        if(this.getName().fullName() == "" ){
            throw new ValidationFailedException("User name is empty");
        }
    }

}
