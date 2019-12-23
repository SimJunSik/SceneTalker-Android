package com.android.yapp.scenetalker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {
    TextView mypage_username ;
    TextView mypage_email;
    ImageButton mypageback;
    ImageButton passwordchange;
    ImageButton nicknamechange;
    ImageButton mywrite;
    ImageButton mylike;
    NicknameDialog nicknameDialog;
    ProfileDialog profileDialog;
    String username;
    String user_email;


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
        mypage_email = findViewById(R.id.email);
        profile_img_change = findViewById(R.id.profile_img_change);
        mywrite = findViewById(R.id.mywrite);
        mylike = findViewById(R.id.mylike);

        nicknameDialog = new NicknameDialog(this);
        profileDialog = new ProfileDialog(this);

        setUserInfo();

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

    private void setUserInfo(){
        String user_key = Utils.user_key;
        Log.i("토큰", user_key);
        Token token = new Token(user_key);
        Call<JsonObject> service = NetRetrofit.getInstance().getUser(token);
        service.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                if(response.message() != null) {
                    Log.i("에러 결과", response.toString());
                }
                if(response.body() == null){
                    return;
                }
                Log.i("결과",response.body().toString());
                JSONParser parser = new JSONParser();
                Object obj = null;
                try {
                    obj = parser.parse(response.body().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JSONObject jsonObj = (JSONObject) obj;

                username = jsonObj.get("username").toString();
                user_email = jsonObj.get("email").toString();

                mypage_username.setText(username);
                mypage_email.setText(user_email);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
