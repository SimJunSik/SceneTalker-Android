package com.android.yapp.scenetalker;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {

    public static RetrofitService getInstance() {
        Retrofit retrofit;
        //String baseUrl = "http://2955713f.ngrok.io/";
        String baseUrl = "http://18a14d71.ngrok.io/";

        if(!Utils.user_key.equals("")) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "Token " + Utils.user_key)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                    .build();
        }else{
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                    .build();
        }
        return retrofit.create(RetrofitService.class);
    }
    private NetRetrofit() {

    }

}
