package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.HealthFood;
import com.mtsealove.github.ohbody.Database.HealthFoodDB;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;
import com.mtsealove.github.ohbody.Database.RestAPI;
import com.mtsealove.github.ohbody.Services.WalkService;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    PersonalDbHelper personalDbHelper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        personalDbHelper = new PersonalDbHelper(this, PersonalDbHelper.BodyTable, null, 1);
        database = personalDbHelper.getReadableDatabase();

        GetRest();
        StartService();
        Handler handler = new Handler();
        if (IsFirst()) {
            handler.postDelayed(InformationRun, 700);
        } else {
            handler.postDelayed(MainRun, 700);
        }

    }


    //처음 접속했는지 확인
    private boolean IsFirst() {
        String query = "select count(*) from " + PersonalDbHelper.BodyTable;
        cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToNext();
            int count = cursor.getInt(0);
            Log.e("데이터 수", String.valueOf(count));
            //데이터가 하나라도 존재하면 처음이 아님
            cursor.close();
            if (count != 0) return false;
        }
        //아닐 경우에 첫 접속
        return true;
    }

    private Runnable InformationRun = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, InformationActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private Runnable MainRun = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    //서비스 시작
    private void StartService() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }
        if (!isWhiteListing) {
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);
        }

        Intent serviceIntent;
        if (WalkService.serviceIntent == null) {
            serviceIntent = new Intent(this, WalkService.class);
            startService(serviceIntent);
        } else {
            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
        }
    }

    //스플래시 화면에서 데이터 미리 받아오기
    private void GetRest() {
        RestAPI restAPI = new RestAPI();
        Call<ResponseBody> call = restAPI.getRetrofitService().GetFoodList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    HealthFoodDB.healthFoods = new ArrayList<>();
                    try {
                        //파싱 전체 결과
                        String result = response.body().string();
                        JSONObject object = new JSONObject(result);
                        JSONObject I_0040 = new JSONObject(object.getString("I-0040"));
                        JSONArray row = I_0040.getJSONArray("row");
                        //객체 추가
                        for (int i = 0; i < row.length(); i++) {
                            JSONObject last = row.getJSONObject(i);
                            String PRMS_DT = last.getString("PRMS_DT");
                            String BSSH_NM = last.getString("BSSH_NM");
                            String APLC_RAWMTRL_NM = last.getString("APLC_RAWMTRL_NM");
                            String HF_FNCLTY_MTRAL_RCOGN_NO = last.getString("HF_FNCLTY_MTRAL_RCOGN_NO");
                            String INDUTY_NM = last.getString("INDUTY_NM");
                            String IFTKN_ATNT_MATR_CN = last.getString("IFTKN_ATNT_MATR_CN");
                            String ADDR = last.getString("ADDR");
                            String FNCLTY_CN = last.getString("FNCLTY_CN");
                            String DAY_INTK_CN = last.getString("DAY_INTK_CN");
                            HealthFood healthFood = new HealthFood(PRMS_DT, BSSH_NM, APLC_RAWMTRL_NM, HF_FNCLTY_MTRAL_RCOGN_NO, INDUTY_NM, IFTKN_ATNT_MATR_CN, ADDR, FNCLTY_CN, DAY_INTK_CN);
                            HealthFoodDB.healthFoods.add(healthFood);
                        }
                        //완료 데이터 추가
                        HealthFoodDB.Loaded = true;
                        Log.e("RestApi", "데이터 파싱 완료");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
