package com.android.yapp.scenetalker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingActivity extends AppCompatActivity {
    private RecyclerView recyclerView=null;
    private ChattingAdapter chattingAdapter=null;
    private List<ChattingInfo> dataList=null;

    private LottieAnimationView[] sweet_potato_lotties;
    private LottieAnimationView[] saida_lotties;

    private WebSocket webSocket;
    private String drama_id;
    private String episode;
    private String user_id;
    private String drama_title;


    private int lottie_sweet_potato_count = 0;
    private int lottie_saida_count = 0;

    Cider_Pass_Dialog cider_pass_dialog;
    Chattingroom_Exit_Dialog chattingroom_exit_dialog;
    SweetPotato_Pass_Dialog sweetPotato_pass_dialog;
    Chattingroom_Notify_Dialog chattingroom_notify_dialog;

    String soda_count;
    String sweet_potato_count;


    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chattingroom);
        init();
        setRecyclerView();
        setCurrentInfo();
        cider_pass_dialog = new Cider_Pass_Dialog(this);
        sweetPotato_pass_dialog = new SweetPotato_Pass_Dialog(this);
        chattingroom_notify_dialog = new Chattingroom_Notify_Dialog(this);
        chattingroom_exit_dialog = new Chattingroom_Exit_Dialog(this);
        chattingroom_notify_dialog.callFunction();
        chattingroom_notify_dialog.delayTime(3000);

        title=(TextView)findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        for(int i=0;i<sweet_potato_lotties.length;i++){
            String lottie_sweet_potato_id = "lottie_action_sweet_potato" + Integer.toString(i+1);
            int res_sweet_potato_id = getResources().getIdentifier(lottie_sweet_potato_id, "id", getPackageName());
            sweet_potato_lotties[i] = findViewById(res_sweet_potato_id);

            String lottie_saida_id = "lottie_action_saida" + Integer.toString(i+1);
            int res_saida_id = getResources().getIdentifier(lottie_saida_id, "id", getPackageName());
            saida_lotties[i] = findViewById(res_saida_id);
        }

        FloatingActionButton sweetpotato = findViewById(R.id.sweetpotato);
        sweetpotato.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
               // sweet_potato_lotties[lottie_sweet_potato_count].setAnimation("sweet_potato2.json");
                sweet_potato_lotties[lottie_sweet_potato_count].setAnimation("sweet_potato2.json");

                sweet_potato_lotties[lottie_sweet_potato_count].playAnimation();
                lottie_sweet_potato_count = (lottie_sweet_potato_count+1)%sweet_potato_lotties.length;
                send_count("potato");
            }
        });

        FloatingActionButton cider = findViewById(R.id.cider);
        cider.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                saida_lotties[lottie_saida_count].setAnimation("saida2.json");
                saida_lotties[lottie_saida_count].playAnimation();
                lottie_saida_count = (lottie_saida_count+1)%saida_lotties.length;
                Log.i("사이다", Integer.toString(lottie_saida_count));
                send_count("soda");
            }
        });
        Button button = findViewById(R.id.chattingbtn);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                EditText editText = findViewById(R.id.Edit);
                if(editText.getText().toString().length() != 0){
                    String message = editText.getText().toString();
                    JSONObject object = new JSONObject();
                    try {
                        object.put("type","chat_message");
                        object.put("message",message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    webSocket.mWebSocketClient.send(object.toString());
                    editText.setText("");
                    ChatMessage chatMessage = new ChatMessage(Utils.user_key, message, drama_id, episode);
                    Call<JsonObject> service = NetRetrofit.getInstance().saveChat(chatMessage);
                    service.enqueue(new retrofit2.Callback<JsonObject>() {
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

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
        ImageButton close=findViewById(R.id.closebtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chattingroom_exit_dialog.callFunction();
             // finish();
            }
        });
    }

    private void init(){
        drama_title = getIntent().getStringExtra("drama_title");
        TextView title = findViewById(R.id.title);
        title.setText(drama_title+" 실시간 톡방");
        recyclerView = findViewById(R.id.recyclerview2);
        dataList = new ArrayList<ChattingInfo>();
        sweet_potato_lotties = new LottieAnimationView[5];
        saida_lotties = new LottieAnimationView[5];
    }

    private void set_count(){
        Call<JsonObject> service = NetRetrofit.getInstance().getCurrentCount(drama_id, episode);
        service.enqueue(new retrofit2.Callback<JsonObject>() {
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

                soda_count = jsonObj.get("soda_count").toString();
                sweet_potato_count = jsonObj.get("sweet_potato_count").toString();
                Log.i("결과",soda_count + " " + sweet_potato_count);

                TextView soda_count_text = findViewById(R.id.soda_count);
                TextView sweet_potato_count_text = findViewById(R.id.sweet_potato_count);

                soda_count_text.setText(soda_count);
                sweet_potato_count_text.setText(sweet_potato_count);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setCurrentInfo(){
        drama_id = getIntent().getStringExtra("drama_id");
        episode = getIntent().getStringExtra("episode");
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

                user_id = jsonObj.get("pk").toString();

                webSocket = new WebSocket(handler, drama_id, episode, user_id);
                webSocket.connectWebSocket();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
        set_count();
    }
    private void setRecyclerView(){
        chattingAdapter = new ChattingAdapter(getApplicationContext(),R.layout.chattingitem,dataList);
        chattingAdapter.setHasStableIds(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chattingAdapter);
    }
    private void send_count(String kind){
        JSONObject object = new JSONObject();
        try {
            object.put("type","count");
            object.put("kind",kind);
            object.put("count",1);
        }catch (Exception e){
            e.printStackTrace();
        }
        webSocket.mWebSocketClient.send(object.toString());
    }

    class FABClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){

        }
    }

    @SuppressLint("HandlerLeak")
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            final String message = bundle.getString("message");
            JSONParser p = new JSONParser();
            try {
                JSONObject obj = (JSONObject)p.parse(message);

                String sender = obj.get("sender").toString();
                String message_text = obj.get("message").toString();


                if (sender.equals("AdminServer")){
                    // Log.i("AdminServer", message_text);
                    set_count();
                    String kind = obj.get("kind").toString();

                    if(kind.equals("soda")&&(Integer.parseInt(soda_count) %10 == 0) && (Integer.parseInt(soda_count) > 0)){
                        cider_pass_dialog.callFunction();
                        cider_pass_dialog.getCider_pass_textview().setText(soda_count);
                        cider_pass_dialog.delayTime(700);
                    }
                    if(kind.equals("potato")&&(Integer.parseInt(sweet_potato_count) %10 == 0) &&(Integer.parseInt(sweet_potato_count) > 0)){
                        sweetPotato_pass_dialog.callFunction();
                        sweetPotato_pass_dialog.getSweetpotato_pass_textview().setText(sweet_potato_count);
                        sweetPotato_pass_dialog.delayTime(700);
                    }
                }
                else {
                    dataList.add(new ChattingInfo(sender, message_text));
                    chattingAdapter.notifyItemInserted(dataList.size());
                    recyclerView.scrollToPosition(dataList.size()-1);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
}