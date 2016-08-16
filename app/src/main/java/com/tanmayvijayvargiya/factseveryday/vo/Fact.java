package com.tanmayvijayvargiya.factseveryday.vo;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tanmayvijayvargiya.factseveryday.model.AppDatabase;
import com.tanmayvijayvargiya.factseveryday.utils.Validate;
import com.tanmayvijayvargiya.factseveryday.utils.ValidationFailedException;

/**
 * Created by tanmayvijayvargiya on 02/07/16.
 */
@Table(database = AppDatabase.class)
public class Fact extends BaseModel implements Validate {

    @Column
    @PrimaryKey
    @SerializedName("_id")
    private String _id;

    @Column
    @SerializedName("title")
    private String title;

    @Column
    @SerializedName("content")
    private String content;

    @Column
    @SerializedName("bannerUrl")
    private String bannerUrl;

    @SerializedName("comments")
    private String[] comments;

    @Column
    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("author")
    private Author author;

    @Column
    public boolean isFavorite = false;

    @Column
    public boolean isSynced = false;

    @Override
    public void validate() {
        if(get_id() == null || get_id() =="")
            throw new ValidationFailedException("Fact Id should not be empty");
        if(getTitle() == null || getTitle() == ""){
            throw new ValidationFailedException("Fact title should not be empty ");
        }
    }

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


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setIsSynced(boolean isSynced) {
        this.isSynced = isSynced;
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
}
