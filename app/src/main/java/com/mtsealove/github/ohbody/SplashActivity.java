package com.mtsealove.github.ohbody;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.*;
import com.mtsealove.github.ohbody.Database.Recipes.Recipe;
import com.mtsealove.github.ohbody.Database.Recipes.RecipeDB;
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
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    PersonalDbHelper personalDbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    String tag = getClass().getSimpleName();
    boolean HealthFoodLoad = false, Recipe0Load = false, Recipe1Load = false;
    String progressMsg="데이터를 업데이트 중입니다\n업데이트는 약 5분정도 소요됩니다";

    ProgressDialog progressDialog;
    Timer timer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        StartService();

        handler = new Handler();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("업데이트");
        progressDialog.setMessage(progressMsg);
        progressDialog.show();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (HealthFoodLoad && Recipe0Load && Recipe1Load) {
                    timer.cancel();
                    progressDialog.dismiss();

                    if (IsFirst()) {
                        handler.postDelayed(InformationRun, 700);
                    } else {
                        handler.postDelayed(MainRun, 700);
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);

        UpdateHealthFood();
        UpdateRecipeDB();
    }


    //처음 접속했는지 확인
    private boolean IsFirst() {
        personalDbHelper = new PersonalDbHelper(this, PersonalDbHelper.BodyTable, null, 1);
        database = personalDbHelper.getReadableDatabase();
        String query = "select count(*) from " + PersonalDbHelper.BodyTable;
        cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToNext();
            int count = cursor.getInt(0);
            Log.e("데이터 수", String.valueOf(count));
            //데이터가 하나라도 존재하면 처음이 아님
            cursor.close();
            personalDbHelper.close();
            database.close();
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
            //서비스가 이미 실행중
        }
    }

    //건강 db 업데이트 필요 여부를 체크
    private void UpdateHealthFood() {
        HealthFoodDbHelper dbHelper = new HealthFoodDbHelper(this, HealthFoodDbHelper.HealthFoodTable, null, 1);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "select Date from " + HealthFoodDbHelper.HealthFoodTable;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            //데이터가 존재할 경우
            if (cursor.getCount() != 0) {
                cursor.moveToNext();
                //업데이트 날짜를 받아옴
                String date = cursor.getString(0);
                String currentDate = new DateF().getDate();
                //같은 달이면 업데이트 안함
                if (currentDate.substring(0, 5).equals(date.substring(0, 5))) {
                    Log.d(tag, "건강식품 업데이트 불필요");
                    GetHealthDB();
                } else {    //다른 달이면 업데이트
                    GetRest();
                }
            } else {
                GetRest();
            }
        } else {
            GetRest();
        }
        cursor.close();
        database.close();
        dbHelper.close();
    }

    //레시피 db업데이트 여부 확인
    private void UpdateRecipeDB() {
        RecipeDbHelper recipeDbHelper = new RecipeDbHelper(this, RecipeDbHelper.RecipeTable, null, 1);
        SQLiteDatabase database = recipeDbHelper.getReadableDatabase();
        String query = "select Date from " + RecipeDbHelper.RecipeTable;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToNext();
            String date = cursor.getString(0);
            String currentDate = new DateF().getDate();
            if (currentDate.substring(0, 5).equals(date.substring(0, 5))) {
                Recipe0Load = true;
                Recipe1Load = true;
                Log.d(tag, "레시피 업데이트 불필요");
            } else {//업데이트
                GetRecipes0();
            }
        } else { //업데이트
            GetRecipes0();
        }
        cursor.close();
        database.close();
        recipeDbHelper.close();
    }

    //데이터 가져오기
    private void GetHealthDB() {
        HealthFoodDbHelper dbHelper = new HealthFoodDbHelper(this, HealthFoodDbHelper.HealthFoodTable, null, 1);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String query = "select * from " + HealthFoodDbHelper.HealthFoodTable;
        Cursor cursor = database.rawQuery(query, null);
        HealthFoodDB.healthFoods = new ArrayList<>();
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    HealthFood healthFood = new HealthFood(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9));
                    HealthFoodDB.healthFoods.add(healthFood);
                }
                HealthFoodLoad = true;
                HealthFoodDB.Loaded = true;
            }
        }
        cursor.close();
        database.close();
        dbHelper.close();
    }

    //스플래시 화면에서 데이터 미리 받아오기
    private void GetRest() {
        progressDialog.setMessage(progressMsg+"\n"+"건강식품 데이터를 불러오고 있습니다");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(tag, "건강식품 db 불러오기");
                RestAPI restAPI = new RestAPI();
                Call<ResponseBody> call = restAPI.getRetrofitService().GetFoodList();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            HealthFoodDbHelper dbHelper = new HealthFoodDbHelper(SplashActivity.this, HealthFoodDbHelper.HealthFoodTable, null, 1);
                            SQLiteDatabase database = dbHelper.getWritableDatabase();
                            HealthFoodDB.healthFoods = new ArrayList<>();
                            String date = new DateF().getDate();
                            try {
                                //파싱 전체 결과
                                String result = response.body().string();
                                JSONObject object = new JSONObject(result);
                                JSONObject I_0040 = new JSONObject(object.getString("I-0040"));
                                JSONArray row = I_0040.getJSONArray("row");
                                dbHelper.CleanData(database);
                                //객체 추가
                                for (int i = 0; i < row.length(); i++) {
                                    JSONObject last = row.getJSONObject(i);
                                    String PRMS_DT = last.getString("PRMS_DT");
                                    String BSSH_NM = last.getString("BSSH_NM");
                                    String APLC_RAWMTRL_NM = last.getString("APLC_RAWMTRL_NM");
                                    progressDialog.setMessage(progressMsg+"\n"+APLC_RAWMTRL_NM);
                                    String HF_FNCLTY_MTRAL_RCOGN_NO = last.getString("HF_FNCLTY_MTRAL_RCOGN_NO");
                                    String INDUTY_NM = last.getString("INDUTY_NM");
                                    String IFTKN_ATNT_MATR_CN = last.getString("IFTKN_ATNT_MATR_CN");
                                    String ADDR = last.getString("ADDR");
                                    String FNCLTY_CN = last.getString("FNCLTY_CN");
                                    String DAY_INTK_CN = last.getString("DAY_INTK_CN");
                                    HealthFood healthFood = new HealthFood(PRMS_DT, BSSH_NM, APLC_RAWMTRL_NM, HF_FNCLTY_MTRAL_RCOGN_NO, INDUTY_NM, IFTKN_ATNT_MATR_CN, ADDR, FNCLTY_CN, DAY_INTK_CN, date);
                                    dbHelper.InsertData(database, healthFood);
                                    HealthFoodDB.healthFoods.add(healthFood);
                                }
                                //완료 데이터 추가
                                HealthFoodDB.Loaded = true;
                                HealthFoodLoad = true;
                                Log.e("RestApi", "데이터 파싱 완료");
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                database.close();
                                dbHelper.close();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }).start();
    }

    private void GetRecipes0() {
        Log.e(tag, "레시피0");
        progressDialog.setMessage(progressMsg+"\n"+"레시피 데이터를 받아오고 있습니다");
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAPI restAPI = new RestAPI();
                Call<ResponseBody> call = restAPI.getRetrofitService().GetRecipe0();
                final RecipeDbHelper recipeDbHelper = new RecipeDbHelper(SplashActivity.this, RecipeDbHelper.RecipeTable, null, 1);
                final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            RecipeDB.recipeArrayList = new ArrayList<>();
                            recipeDbHelper.CleanData(database);
                            try {
                                String body = response.body().string();
                                JSONObject root = new JSONObject(body);
                                JSONObject COOKRCP01 = new JSONObject(root.getString("COOKRCP01"));
                                JSONArray row = COOKRCP01.getJSONArray("row");
                                for (int i = 0; i < row.length(); i++) {
                                    Recipe recipe = new Recipe(row.getJSONObject(i));
                                    progressDialog.setMessage(progressMsg+"\n"+recipe.getRCP_NM());
                                    RecipeDB.recipeArrayList.add(recipe);
                                    recipeDbHelper.InsertData(database, recipe);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } finally {
                                database.close();
                                recipeDbHelper.close();
                                Recipe0Load = true;
                                GetRecipe1();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }).start();
    }

    private void GetRecipe1() {
        Log.e(tag, "레시피1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RecipeDbHelper recipeDbHelper = new RecipeDbHelper(SplashActivity.this, RecipeDbHelper.RecipeTable, null, 1);
                final SQLiteDatabase database = recipeDbHelper.getWritableDatabase();
                RestAPI restAPI = new RestAPI();
                Call<ResponseBody> call = restAPI.getRetrofitService().GetRecipe1();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String body = response.body().string();
                                JSONObject root = new JSONObject(body);
                                JSONObject COOKRCP01 = new JSONObject(root.getString("COOKRCP01"));
                                JSONArray row = COOKRCP01.getJSONArray("row");
                                for (int i = 0; i < row.length(); i++) {

                                    Recipe recipe = new Recipe(row.getJSONObject(i));
                                    progressDialog.setMessage(progressMsg+"\n"+recipe.getRCP_NM());
                                    RecipeDB.recipeArrayList.add(recipe);
                                    recipeDbHelper.InsertData(database, recipe);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            } finally {
                                database.close();
                                recipeDbHelper.close();
                                RecipeDB.Loaded = true;

                                Recipe1Load = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer = null;
    }
}
