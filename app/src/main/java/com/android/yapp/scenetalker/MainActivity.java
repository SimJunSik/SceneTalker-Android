package com.android.yapp.scenetalker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.android.yapp.scenetalker.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity implements Switch.OnCheckedChangeListener {
    ActivityMainBinding binding;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    OnAirFragment onAirFragment;
    OffAirFragment offAirFragment;

    ImageView goto_mypage;

    String titleText = "배가본드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        String secondTitleText =  String.format(getResources().getString(R.string.main_second_title),titleText);
        goto_mypage = findViewById(R.id.goto_mypage);
        SpannableStringBuilder ssb = new SpannableStringBuilder(secondTitleText);
        ssb.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, titleText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.mainSecondTitle.setText(ssb);
        initFragment();
        binding.onAirSwitch.setOnCheckedChangeListener(this);
        goto_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initFragment(){
        fragmentManager = getSupportFragmentManager();
        onAirFragment = new OnAirFragment();
        offAirFragment = new OffAirFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.add(binding.frameLayout.getId(),offAirFragment).commitAllowingStateLoss();
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
