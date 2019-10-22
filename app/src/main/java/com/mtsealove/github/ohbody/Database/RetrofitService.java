package com.mtsealove.github.ohbody.Database;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface RetrofitService {
    @GET("/api/4a18d0622efc443cb99c/I-0040/json/1/500")
    Call<ResponseBody> GetFoodList();

    //1번에 100개의 데이터만 검색되어 2번에 나눠 수행
    @GET("/api/4a18d0622efc443cb99c/COOKRCP01/json/1/1000")
    Call<ResponseBody> GetRecipe0();

    @GET("/api/4a18d0622efc443cb99c/COOKRCP01/json/1001/1300")
    Call<ResponseBody> GetRecipe1();
}
