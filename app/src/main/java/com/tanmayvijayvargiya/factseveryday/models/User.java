package com.tanmayvijayvargiya.factseveryday.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 03/07/16.
 */
public class User {


    @SerializedName("_id")
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @SerializedName("name")
    private Name name;

    @SerializedName("emailId")
    private String emailId;

    public List<String> getPublishedFacts() {
        return publishedFacts;
    }

    public void setPublishedFacts(List<String> publishedFacts) {
        this.publishedFacts = publishedFacts;
    }

    public List<String> getFavFacts() {
        return favFacts;
    }

    public void setFavFacts(List<String> favFacts) {
        this.favFacts = favFacts;
    }

    @SerializedName("publishedFacts")

    private List<String> publishedFacts;

    @SerializedName("favFacts")
    private List<String> favFacts;

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
            this.last = last;
        }

        public String fullName(){
            return first + " " + last;
        }
    }

    public void setFullName(String first, String last){
        Name name = new Name();
        name.setFirst(first);
        name.setLast(last);
        this.setName(name);
    }

    private String profilePicUrl;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
