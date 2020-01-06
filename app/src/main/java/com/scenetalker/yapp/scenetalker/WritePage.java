package com.scenetalker.yapp.scenetalker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yapp.scenetalker.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePage extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE = 1;
    private File tempFile;
    int dramaId;
    TextView dramaname;
    EditText write_ed;
    ImageButton image_btn;
    ImageView write_imageView;
    Button finish;
    ImageButton cancel;

    String[] permission_list = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.write_page);
        final Intent intent = new Intent(this.getIntent());

        checkPermission();

        String drama_title = intent.getExtras().getString("name");
        dramaId = intent.getIntExtra("dramaId",-1);
        cancel=findViewById(R.id.cancel_btn);
        finish = (Button) findViewById(R.id.finish_btn);
        image_btn = (ImageButton) findViewById(R.id.image_btn);
        write_ed = (EditText) findViewById(R.id.feed_write_et);
        write_imageView = (ImageView) findViewById(R.id.write_imageview);
        dramaname = (TextView) findViewById(R.id.dramaname);
        dramaname.setText(drama_title);
        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<JsonObject> service = null;
                RequestBody content = RequestBody.create(MediaType.parse("text/plain"), write_ed.getText().toString());
                Bitmap bitmap = null;
                try {
                    bitmap = ((BitmapDrawable) write_imageView.getDrawable()).getBitmap();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(bitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                    try {
                        FileOutputStream fo = new FileOutputStream(file);
                        fo.write(bytes.toByteArray());
                        fo.flush();
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"),file);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),fileReqBody);
                    service = NetRetrofit.getInstance().writePostWithImage(part, content, dramaId);
                }else {
                    service = NetRetrofit.getInstance().writePostOnlyContent(content, dramaId);
                }

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
                        Log.i("결과", response.body().toString());

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",response.body().toString());
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
                //Intent intent = new Intent(WritePage.this,FeedPage.class);
                //finish();
                //startActivityForResult(intent,1);

            }

        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            write_imageView.setImageURI(selectedImageUri);
        }
    }
}