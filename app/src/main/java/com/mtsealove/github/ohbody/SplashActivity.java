package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;

public class SplashActivity extends AppCompatActivity {
    PersonalDbHelper personalDbHelper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        personalDbHelper = new PersonalDbHelper(this, PersonalDbHelper.BodyTable, null, 1);
        database= personalDbHelper.getReadableDatabase();


        Handler handler = new Handler();
        if(IsFirst()){
            handler.postDelayed(InformationRun, 700);
        } else {
            handler.postDelayed(MainRun, 700);
        }
    }

    //처음 접속했는지 확인
    private boolean IsFirst() {
        String query="select count(*) from "+ PersonalDbHelper.BodyTable;
        cursor=database.rawQuery(query, null);
        if(cursor!=null){
            cursor.moveToNext();
            int count=cursor.getInt(0);
            Log.e("데이터 수", String.valueOf(count));
            //데이터가 하나라도 존재하면 처음이 아님
            cursor.close();
            if(count!=0) return false;
        }
        //아닐 경우에 첫 접속
        return true;
    }

    private Runnable InformationRun=new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, InformationActivity.class);
            startActivity(intent);
            finish();
        }
    };

    private Runnable MainRun=new Runnable() {
        @Override
        public void run() {
            Intent intent=new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
