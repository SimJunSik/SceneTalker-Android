package com.android.yapp.scenetalker;

import android.media.Image;

public class FeedInfo {

    String username;//닉네임
    String comment;//글
    String comment_time;//-분전
    int comment_num;//댓글 수
    int heart_num;//좋아요 수
    String image;

    public FeedInfo(String username, String comment, String comment_time,int comment_num, int heart_num){
        this.username=username;
        this.comment=comment;
        this.comment_time=comment_time;
        this.comment_num=comment_num;
        this.heart_num=heart_num;
    }
    public FeedInfo(String username, String comment, String comment_time,int comment_num, int heart_num,String image){
        this.username=username;
        this.comment=comment;
        this.comment_time=comment_time;
        this.comment_num=comment_num;
        this.heart_num=heart_num;
        this.image = image;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() { return comment;}

    public void setComment(String comment) { this.comment = comment;}

    public String getComment_time(){return comment_time;}

    public void setComment_time(String comment_time){this.comment_time=comment_time;}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getHeart_num() { return heart_num;}

    public void setHeart_num(int heart_num) { this.heart_num = heart_num;}

    public int getComment_num() { return comment_num;}

    public void setComment_num(int comment_num) { this.comment_num = comment_num;}


}
