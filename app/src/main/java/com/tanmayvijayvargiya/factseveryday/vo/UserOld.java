package com.tanmayvijayvargiya.factseveryday.vo;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */


public class UserOld{

    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    private Name name;


    @SerializedName("emailId")
    private String emailId;

    @SerializedName("favFacts")
    private List<String> favFacts;


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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public List<String> getFavFacts() {
        return favFacts;
    }

    public void setFavFacts(List<String> favFacts) {
        this.favFacts = favFacts;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
