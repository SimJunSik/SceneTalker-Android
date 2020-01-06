package com.scenetalker.yapp.scenetalker;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yapp.scenetalker.R;
import com.android.yapp.scenetalker.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class LoginActivity extends BaseActivity{
    ActivityLoginBinding binding;
    TextView login_first_title,second_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        WebSocket socket = new WebSocket();
//        socket.connectWebSocket();
        login_first_title=findViewById(R.id.first_title);
        second_title=findViewById(R.id.second_title);
        System.setProperty("file.encoding","UTF-8");
        Field charset = null;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        charset.setAccessible(true);
        try {
            charset.set(null,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onClickLogin(View view){
        if(binding.idEdit.getText().toString().equals("")) {
            //Toast.makeText(LoginActivity.this,"아이디를 입력 해주세요.",Toast.LENGTH_SHORT).show();
            login_first_title.setText("앗, 회원정보가 없어요!");
            second_title.setText("이메일 주소를 입력해주세요.");
            return;
        } else if(!Utils.emailFooterCheck(binding.idEdit.getText().toString())){
                // Toast.makeText(SignUpActivity.this,"이메일형식을 확인 해주세요.",Toast.LENGTH_SHORT).show();
            login_first_title.setText("앗, 잘못된 정보에요");
            second_title.setText("이메일 형식을 확인해주세요.");
            return;
        }else if(binding.passwordEdit.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this,"비밀번호를 입력 해주세요",Toast.LENGTH_SHORT).show();
            return;
        }else if(binding.passwordEdit.getText().toString().length()<6){
            login_first_title.setText("앗, 회원정보가 없어요!");
            second_title.setText("비밀번호를 6자리 이상 입력해주세요");
        }

        User user = new User(binding.idEdit.getText().toString(),binding.passwordEdit.getText().toString());
        Call<JsonObject> service = NetRetrofit.getInstance().login(user);
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
                Utils.user_key = response.body().get("key").getAsString();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    public void onClickSignUp(View view){
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }
}