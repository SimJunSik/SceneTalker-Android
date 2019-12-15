package com.android.yapp.scenetalker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yapp.scenetalker.databinding.ActivitySignupBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends BaseActivity {
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_GALARY = 2;
    private static final int REQUEST_READ_PERMISSION = 11;
    ActivitySignupBinding binding;
    TextView signup_first_title;
    TextView signup_second_title;

    Bitmap mProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup);

        ImageButton exit = findViewById(R.id.signup_back_btn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        signup_first_title = findViewById(R.id.signup_first_title);
        signup_second_title = findViewById(R.id.signup_second_title);
    }

    public void onClickBack(View view){
        finish();
    }
    public void onClickSignUp(View view){
        if(binding.idEdit.getText().toString().equals("")){
            //Toast.makeText(SignUpActivity.this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("이메일을 입력해주세요.");
            return;
        }else if(binding.nameEdit.getText().toString().equals("")) {
            //Toast.makeText(SignUpActivity.this,"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("닉네임을 입력해주세요.");
            return;

//        }else if(binding.nameEdit.getText().toString().equals("")){
//                signup_first_title.setText("앗, 사용중인 닉네임이에요");
//                signup_second_title.setText("더 좋은 닉네임을 생각할 수 있을거에요.");
//                return;
        }else if(binding.passwordEdit.getText().toString().equals("")){
           // Toast.makeText(SignUpActivity.this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("비밀번호를 입력해주세요.");
            return;
        }else if(binding.passwordCheckEdit.getText().toString().equals("")){
            //Toast.makeText(SignUpActivity.this,"비밀번호 확인을 입력해주세요.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("비밀번호 확인을 입력해주세요.");
            return;
        }else if(!Utils.emailFooterCheck(binding.idEdit.getText().toString())){
           // Toast.makeText(SignUpActivity.this,"이메일형식을 확인 해주세요.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("이메일 형식을 입력해주세요.");
            return;
        }else if(!binding.passwordEdit.getText().toString().equals(binding.passwordCheckEdit.getText().toString())){
            //Toast.makeText(SignUpActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
            signup_first_title.setText("앗, 잘못된 정보에요");
            signup_second_title.setText("비밀번호가 일치하지 않습니다.");
            return;
        }

        User user = new User(binding.idEdit.getText().toString(),binding.nameEdit.getText().toString(),binding.passwordEdit.getText().toString(),binding.passwordCheckEdit.getText().toString());
        Call<JsonObject> service = NetRetrofit.getInstance().signup(user);
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
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onClickModifyProfile(View view){
        requestPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == REQUEST_PHOTO){
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap)extras.get("data");
                this.mProfile = bitmap;
                binding.profileImg.setImageBitmap(bitmap);
            }
            if(requestCode == REQUEST_GALARY){
                try {
                    Uri imageUri = data.getData();
                    String filePath = "";
                    if (imageUri != null) {
                        filePath = FetchPath.getPath(this, imageUri);
                    }
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap image = BitmapFactory.decodeStream(in);
                    in.close();

                    ExifInterface exif = new ExifInterface(filePath);
                    int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                    int exifDegree = Utils.exifOrientationToDegrees(exifOrientation);
                    image = Utils.imageRotate(image,exifDegree);

                    this.mProfile = image;
                    binding.profileImg.setImageBitmap(image);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            ProfileDialog dialog = new ProfileDialog(SignUpActivity.this);
//            dialog.setOnClickListener(new ProfileDialog.CameraOnClickListener() {
//                @Override
//                public void onClick(int flag) {
//                    switch (flag) {
//                        case ProfileDialog.CAMERA_CLCIK:
//                            openCamera();
//                            break;
//                        case ProfileDialog.GALARY_CLCIK:
//                            openGalary();
//                            break;
//                        case ProfileDialog.BASIC_CLCIK:
//                            break;
//                    }
//                }
//
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//            dialog.show();
            openGalary();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
        }else{
            openCamera();
        }
    }
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_PHOTO);
    }
    private void openGalary(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(Intent.createChooser(intent,"Get Album"),REQUEST_GALARY);
    }
}
