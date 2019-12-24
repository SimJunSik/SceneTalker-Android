package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeActivity extends AppCompatActivity {
    Button pwchagnebutton;
    ImageButton pwchange_back_button;
    TextView pwforget;
    EditText original_password;
    EditText new_password1;
    EditText new_password2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_change);
        pwchagnebutton = findViewById(R.id.change_pwd_btn1);
        pwchange_back_button = findViewById(R.id.pwchange_back_btn);
        original_password = findViewById(R.id.original_password_text);
        new_password1 = findViewById(R.id.new_password1_text);
        new_password2 = findViewById(R.id.new_password2_text);

        pwchange_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pwchagnebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_password1_text = new_password1.getText().toString();
                String new_password2_text = new_password2.getText().toString();

                NewPassword newPassword = new NewPassword(new_password1_text, new_password2_text);

                Call<JsonObject> service = NetRetrofit.getInstance().changeUserPassword(newPassword);
                service.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Gson gson = new Gson();
                        if(response.body() == null){
                            return;
                        }
                        Log.i("코드",""+response.code());

                        Log.i("결과",response.body().toString());
                        JSONParser parser = new JSONParser();
                        Object obj = null;
                        try {
                            obj = parser.parse(response.body().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        JSONObject jsonObj = (JSONObject) obj;

                        String result = jsonObj.get("detail").toString();

                        if(result.equals("New password has been saved.")){
                            Toast.makeText(PasswordChangeActivity.this,"비밀번호가 변경되었습니다.",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PasswordChangeActivity.this,"새 비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
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
