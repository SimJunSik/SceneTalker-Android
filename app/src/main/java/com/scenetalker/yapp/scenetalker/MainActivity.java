package com.scenetalker.yapp.scenetalker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.yapp.scenetalker.R;
import com.android.yapp.scenetalker.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements Switch.OnCheckedChangeListener {
    ActivityMainBinding binding;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    OnAirFragment onAirFragment;
    OffAirFragment offAirFragment;

    ImageView goto_mypage;

    TextView firstTitleUsernameText;
    String username;
    String user_profile_image_url;

    String titleText;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        goto_mypage = findViewById(R.id.goto_mypage);
        firstTitleUsernameText = findViewById(R.id.main_first_title2);
        initFragment();
        binding.onAirSwitch.setOnCheckedChangeListener(this);
        goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                startActivity(intent);
            }
        });
        setCurrnetInfo();
        context = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCurrnetInfo();
    }

    public void initFragment(){
        fragmentManager = getSupportFragmentManager();
        onAirFragment = new OnAirFragment();
        offAirFragment = new OffAirFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(binding.frameLayout.getId(),offAirFragment).commitAllowingStateLoss();
    }

    public void getCurrnetUserInfo(String user_key){
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

                if(jsonObj.get("profile_image") == null){
                    user_profile_image_url = "";
                } else {
                    user_profile_image_url = jsonObj.get("profile_image").toString();
                }

                Log.i("유저 정보", user_profile_image_url);
                Glide.with(context).load(user_profile_image_url).error(R.drawable.default_image).into(goto_mypage);

                username = jsonObj.get("username").toString();
                firstTitleUsernameText.setText(username);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getCurrentUserBestDramaInfo(){
        Call<JsonObject> service = NetRetrofit.getInstance().getUserBookmarkBestDrama();
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

                Log.i("베스트 드라마 결과", jsonObj.toString());
                String best_drama_title = jsonObj.get("title").toString();
                String secondTitleText = "";
                if(best_drama_title.equals("")){
                    titleText = "드라마";
                    secondTitleText =  titleText + "에 대해 자유롭게 소통하세요!";
                }
                else {
                    titleText = best_drama_title;
                    if(titleText.length() >= 8 ){
                        titleText = titleText.substring(0,8).trim() + "...";
                    }
                    secondTitleText =  titleText + "이(가) 방영 중 이에요!";
                }
                SpannableStringBuilder ssb = new SpannableStringBuilder(secondTitleText);
                ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                binding.mainSecondTitle.setText(ssb);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setCurrnetInfo(){
        String user_key = Utils.user_key;
        getCurrnetUserInfo(user_key);
        getCurrentUserBestDramaInfo();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        transaction = fragmentManager.beginTransaction();
        if(isChecked){
            transaction.replace(binding.frameLayout.getId(),onAirFragment).commitAllowingStateLoss();
        }else{
            transaction.replace(binding.frameLayout.getId(),offAirFragment).commitAllowingStateLoss();
        }
    }
}
