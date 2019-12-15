package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordChangeActivity extends AppCompatActivity {
    Button pwchagnebutton;
    ImageButton pwchange_back_button;
    TextView pwforget;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);
        pwchagnebutton = findViewById(R.id.change_pwd_btn1);
        pwchange_back_button = findViewById(R.id.pwchange_back_btn);

        pwchange_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pwchagnebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PasswordChangeActivity.this,"비밀번호가 변경되었습니다.",Toast.LENGTH_LONG).show();
            }
        });
        pwforget = findViewById(R.id.passwordfind_textview);
        pwforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PasswordFindActivity.class);
                startActivity(intent);
            }
        });

    }
}
