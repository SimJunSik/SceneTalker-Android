package com.scenetalker.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.yapp.scenetalker.R;

public class PasswordChangeEmailActivity extends AppCompatActivity {
    Button pwchange_email_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_find_email);
        pwchange_email_btn = findViewById(R.id.change_pwd_btn2);

        pwchange_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PasswordResetActivity.class);
                startActivity(intent);
            }
        });
    }
}
