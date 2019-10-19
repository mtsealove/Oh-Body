package com.mtsealove.github.ohbody.Database;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

//RestApi
public class RestAPI {
    Retrofit retrofit;
    RetrofitService retrofitService;
    OkHttpClient okHttpClient;

    public RestAPI() {
        //통신 연결 클라이언트
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build();
        //데이터를 받아올 API
        retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.foodsafetykorea.go.kr")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitService = retrofit.create(RetrofitService.class);
    }

    //바로 사용할 수 있는 인터페이스 반환
    public RetrofitService getRetrofitService() {
        return retrofitService;
    }
}
