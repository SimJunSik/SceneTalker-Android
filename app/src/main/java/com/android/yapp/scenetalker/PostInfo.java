package com.android.yapp.scenetalker;

import android.graphics.Bitmap;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class PostInfo {
    String content;


    public PostInfo(String content,MultipartBody.Part image) {
        this.content = content;
    }

    public PostInfo(String content){
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
