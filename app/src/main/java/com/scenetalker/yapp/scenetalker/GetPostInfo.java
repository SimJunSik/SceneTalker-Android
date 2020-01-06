package com.scenetalker.yapp.scenetalker;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;

import com.bumptech.glide.RequestBuilder;

public class GetPostInfo {

    String title;
    String id;
    String feed;
    String author;
    String author_name;
    String content;
    String image;
    String[] likes;
    String created_at;
    String updated_at;


    String post_drama_title;
    Bitmap bitmap_image;
    int like_counts;
    int comment_counts;
    boolean is_mine;
    boolean is_liked_by_me;
    String user_profile_image;


    public String getUser_profile_img() {
        return user_profile_image;
    }

    public void setUser_profile_img(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }



    public String getPost_drama_title() {
        return post_drama_title;
    }

    public void setPost_drama_title(String post_drama_title) {
        this.post_drama_title = post_drama_title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setBitmap_image(Bitmap bitmap_image) {
        this.bitmap_image = bitmap_image;
    }

    public Bitmap getBitmap_image() {
        return bitmap_image;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getLike_counts() {
        return like_counts;
    }

    public void setLike_counts(int like_counts) {
        this.like_counts = like_counts;
    }

    public int getComment_counts() {
        return comment_counts;
    }

    public void setComment_counts(int comment_counts) {
        this.comment_counts = comment_counts;
    }

    public boolean isIs_mine() {
        return is_mine;
    }

    public void setIs_mine(boolean is_mine) {
        this.is_mine = is_mine;
    }

    public boolean isIs_liked_by_me() {
        return is_liked_by_me;
    }

    public void setIs_liked_by_me(boolean is_liked_by_me) {
        this.is_liked_by_me = is_liked_by_me;
    }
}
