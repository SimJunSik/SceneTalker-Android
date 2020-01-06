package com.scenetalker.yapp.scenetalker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.yapp.scenetalker.R;

public class PasswordFindActivity extends AppCompatActivity {
    ImageButton pwfind_cancel_button;
    Button pwfind_ok_button;
    PasswordCheckDialog PasswordCheckDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_find);

        pwfind_cancel_button = findViewById(R.id.pwfind_cancel_btn);
        pwfind_ok_button = findViewById(R.id.pwfind_ok_btn);
        final PasswordCheckDialog passwordCheckDialog = new PasswordCheckDialog(this);

        pwfind_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pwfind_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordCheckDialog.callFunction();


            }
        });
    }
}
