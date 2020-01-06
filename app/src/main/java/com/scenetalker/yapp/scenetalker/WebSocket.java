package com.scenetalker.yapp.scenetalker;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

public class WebSocket{
    WebSocketClient mWebSocketClient;
    Handler handler;
    String drama_id;
    String episode;
    String user_id;

    public WebSocket() { }

    public WebSocket(Handler handler, String drama_id, String episode, String user_id) {
        this.handler = handler;
        this.drama_id = drama_id;
        this.episode = episode;
        this.user_id = user_id;
    }

    public void connectWebSocket() {
        URI uri;
        Log.i("URI",user_id);
        try {
            uri = new URI("ws://15.164.65.235/ws/chat/" + drama_id + "/" + episode + "/" + user_id + "/");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("에러","URL 에러");
            return;
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.i("받은 메시지",message);
                Message msg = handler.obtainMessage();
                Bundle b = new Bundle();
                b.putString("message", message);
                msg.setData(b);
                handler.sendMessage(msg);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}