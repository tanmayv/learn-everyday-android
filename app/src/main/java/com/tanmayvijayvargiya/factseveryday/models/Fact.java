package com.tanmayvijayvargiya.factseveryday.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanmayvijayvargiya on 02/07/16.
 */
public class Fact {

    @SerializedName("_id")
    private String _id;

    @Override
    public boolean equals(Object o) {

        Fact f = (Fact) o;
        Log.d("Shit", "Checking shit man " + f.get_id() + " " + this.get_id());
        if(f.get_id().equals(get_id())){
            return  true;
        }
        else
            return false;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("bannerUrl")
    private String bannerUrl;

    @SerializedName("comments")
    private String[] comments;

    @SerializedName("createdAt")
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("author")
    private Author author;

    public class Author{
        @SerializedName("name")
        private String name;

        @SerializedName("_id")
        private String _id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public class Meta {
        @SerializedName("favs")
        private Integer favs;

        public Integer getFavs() {
            return favs;
        }

        public void setFavs(Integer favs) {
            this.favs = favs;
        }
    }

    public void initAuthor(String name, String userid){
        Author author = new Author();
        author.setName(name);
        author.set_id(userid);
        this.setAuthor(author);
    }

    public boolean isFavorite = false;
}
