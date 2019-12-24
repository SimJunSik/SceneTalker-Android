package com.android.yapp.scenetalker;

public class NewPassword {
    private String new_password1;
    private String new_password2;

    public NewPassword(String new_password1, String new_password2) {
        this.new_password1 = new_password1;
        this.new_password2 = new_password2;
    }

    public String getNew_password1() {
        return new_password1;
    }

    public void setNew_password1(String new_password1) {
        this.new_password1 = new_password1;
    }

    public String getNew_password2() {
        return new_password2;
    }

    public void setNew_password2(String new_password2) {
        this.new_password2 = new_password2;
    }
}
