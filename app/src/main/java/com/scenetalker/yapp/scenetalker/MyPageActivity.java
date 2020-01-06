package com.scenetalker.yapp.scenetalker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.yapp.scenetalker.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageActivity extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 1;

    TextView mypage_username ;
    TextView mypage_email;
    ImageButton mypageback;
    ImageButton passwordchange;
    ImageButton nicknamechange;
    ImageButton mywrite;
    ImageButton mylike;
    ImageButton mybookmark;
    ImageButton logout;
    ImageButton withdrawal;
    NicknameDialog nicknameDialog;
    ProfileDialog profileDialog;
    CircleImageView mypage_profile_image;
    String username;
    String user_email;
    String user_profile_image_url;
    Logout_Dialog logout_dialog;
    Withdrawal_Dialog withdrawal_dialog;

    ImageButton profile_img_change;

    Context context;

    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);
        context = getApplicationContext();

        checkPermission();

        mypageback = findViewById(R.id.mypage_back_btn);
        mypageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mypage_profile_image = findViewById(R.id.my_profile_img);
        passwordchange = findViewById(R.id.password_change);
        nicknamechange = findViewById(R.id.nickname_change);
        mypage_username = findViewById(R.id.mypage_username);
        mypage_email = findViewById(R.id.email);
        profile_img_change = findViewById(R.id.profile_img_change);
        mywrite = findViewById(R.id.mywrite);
        mylike = findViewById(R.id.mylike);
        mybookmark=findViewById(R.id.mybookmark);
        logout=findViewById(R.id.logout);
        withdrawal=findViewById(R.id.withdrawal);
        nicknameDialog = new NicknameDialog(this);
        profileDialog = new ProfileDialog(this);
        logout_dialog=new Logout_Dialog(this);
        withdrawal_dialog=new Withdrawal_Dialog(this);
        setUserInfo();

        //로그아웃
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_dialog.callFunction();
            }
        });

        //회원탈퇴
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawal_dialog.callFunction();
            }
        });

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
//                profileDialog.callFunction();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
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
        mybookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyBookmarkActivity.class);
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
                if(jsonObj.get("profile_image") == null){
                    user_profile_image_url = "";
                } else {
                    user_profile_image_url = jsonObj.get("profile_image").toString();
                }

                mypage_username.setText(username);
                mypage_email.setText(user_email);
                Glide.with(context).load(user_profile_image_url).error(R.drawable.default_image).into(mypage_profile_image);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            Log.i("이미지", selectedImageUri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Call<JsonObject> service = null;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file2.jpg");
            try {
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.flush();
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"),file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file",file.getName(),fileReqBody);
            service = NetRetrofit.getInstance().changeUserProfileImage(part);

            service.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Gson gson = new Gson();
                    if (response.message() != null) {
                        Log.i("에러 결과", response.toString());
                    }
                    if (response.body() == null) {
                        return;
                    }
                    Log.i("프로필", response.body().toString());
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });

            Glide.with(context).load(selectedImageUri).error(R.drawable.default_image).into(mypage_profile_image);
        }
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    // finish();
                }
            }
        }
    }
}
