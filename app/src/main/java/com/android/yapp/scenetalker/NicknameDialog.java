package com.android.yapp.scenetalker;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NicknameDialog {
    private Context context;

    public NicknameDialog(Context context) {
        this.context = context;
    }
    public void callFunction(final TextView changed_nickname){
        final Dialog dlg = new Dialog(context);
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.nickname_change_dialog);
        dlg.show();

        final EditText nickname_edit = (EditText)dlg.findViewById(R.id.nickname_edit);
        final Button edit_ok_btn = dlg.findViewById(R.id.nickname_dialog_ok_btn);
        final Button edit_cancel_btn = dlg.findViewById(R.id.nickname_dialog_cancel_btn);

        edit_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String new_username = nickname_edit.getText().toString();
                Call<JsonObject> service = NetRetrofit.getInstance().putUsername(new_username);
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

                        String result = jsonObj.get("result").toString();
                        if(result.equals("ok")){
                            changed_nickname.setText(new_username);
                            dlg.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
        edit_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

    }
}
