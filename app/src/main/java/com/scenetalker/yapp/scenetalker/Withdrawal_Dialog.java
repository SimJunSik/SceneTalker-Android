package com.scenetalker.yapp.scenetalker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.yapp.scenetalker.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Withdrawal_Dialog {
    private Context context;
    public Withdrawal_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.withdrawal_layout);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();


        dlg.show();



        final Button notify_exit_button_yes= dlg.findViewById(R.id.withdrawal_dilaog_button_yes);
        notify_exit_button_yes.setOnClickListener(new View.OnClickListener() {

            Intent intent;

            @Override
            public void onClick(View view) {
                dlg.dismiss();
                ((Activity) context).finish();

                Call<JsonObject> service = NetRetrofit.getInstance().withdrawal();
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


                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                Intent intent=new Intent(view.getContext(),LoginActivity.class);
                view.getContext().startActivity(intent);

            }
        });

        final Button notify_exit_button_no = dlg.findViewById(R.id.withdrawal_dilaog_button_no);
        notify_exit_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();

            }
        });
    }


}
