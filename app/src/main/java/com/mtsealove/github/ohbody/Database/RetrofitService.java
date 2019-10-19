package com.mtsealove.github.ohbody.Database;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface RetrofitService {
    @GET("/api/4a18d0622efc443cb99c/I-0040/json/1/500")
    Call<ResponseBody> GetFoodList();
}
