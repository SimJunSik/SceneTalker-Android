package com.android.yapp.scenetalker;

public class User {
    String username;
    String email;
    String profile_image_url;
    String password1;
    String password2;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String first_name,String username,String password1,String password2){
        this.email = first_name;
        this.username = username;
        this.password1 = password1;
        this.password2 = password2;
    }
    public User(String username,String password){
        this.email = username;
        this.password = password;
    }
    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
