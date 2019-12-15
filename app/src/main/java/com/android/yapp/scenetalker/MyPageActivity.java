package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {
    TextView mypage_username ;
    ImageButton mypageback;
    ImageButton passwordchange;
    ImageButton nicknamechange;
    ImageButton mywrite;
    ImageButton mylike;
    NicknameDialog nicknameDialog;
    ProfileDialog profileDialog;


    ImageButton profile_img_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        mypageback = findViewById(R.id.mypage_back_btn);
        mypageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        passwordchange = findViewById(R.id.password_change);
        nicknamechange = findViewById(R.id.nickname_change);
        mypage_username = findViewById(R.id.mypage_username);
        profile_img_change = findViewById(R.id.profile_img_change);
        mywrite = findViewById(R.id.mywrite);
        mylike = findViewById(R.id.mylike);

        nicknameDialog = new NicknameDialog(this);
        profileDialog = new ProfileDialog(this);

        passwordchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PasswordChangeActivity.class);
                startActivity(intent);
            }
        });

        nicknamechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nicknameDialog.callFunction(mypage_username);
            }
        });

        profile_img_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDialog.callFunction();
            }
        });
        mywrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyWrittenActivity.class);
                startActivity(intent);
            }
        });
        mylike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyLikeActivity.class);
                startActivity(intent);
            }
        });
    }
}
