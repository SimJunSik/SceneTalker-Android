package com.scenetalker.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.yapp.scenetalker.R;

public class PasswordResetActivity extends AppCompatActivity {
    Button pwreset_btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset);
        pwreset_btn = findViewById(R.id.change_pwd_btn1);
        pwreset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                Toast.makeText(PasswordResetActivity.this,"비밀번호가 변경되었습니다. 로그인해주세요",Toast.LENGTH_LONG).show();

            }
        });
    }
}
