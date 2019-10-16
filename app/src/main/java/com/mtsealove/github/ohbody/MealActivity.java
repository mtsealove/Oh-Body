package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.DateF;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;
import com.mtsealove.github.ohbody.View.MealView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MealActivity extends AppCompatActivity {
    TextView dateTV;
    MealView mealBreakfast, mealLunch, mealDinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        dateTV = findViewById(R.id.dateTV);
        initDate();

        mealBreakfast = findViewById(R.id.mealBreakfast);
        mealLunch = findViewById(R.id.mealLunch);
        mealDinner = findViewById(R.id.mealDinner);
        mealBreakfast.setMealType(MealView.Breakfast);
        mealLunch.setMealType(MealView.Lunch);
        mealDinner.setMealType(MealView.Dinner);
        GetData();
    }

    //날짜 설정
    private void initDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM월 dd일 EE요일", Locale.KOREAN);

        dateTV.setText(dateFormat.format(date));
    }

    //저장된 데이터 읽기
    private void GetData() {
        PersonalDbHelper dbHelper=new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        DateF dateF=new DateF();
        Cursor cursor;
        //아침 조회 쿼리
        String query1="select Foods, Serving_wt from "+PersonalDbHelper.MealTable+
                " where Date='"+dateF.getDate()+"' and "+
                "Meal="+MealView.RequestBreakfast;
        Log.e("query", query1);
        cursor=database.rawQuery(query1, null);
        if(cursor!=null) {
            if(cursor.getCount()!=0){
                String content="";
                cursor.moveToNext();
                String[] foods=cursor.getString(0).split(";");
                String[] Serving_wts=cursor.getString(1).split(";");
                for(int i=0; i<foods.length; i++) {
                    content+=foods[i]+" "+Serving_wts[i]+"\n";
                }
                content=content.substring(0, content.length()-1);
                mealBreakfast.setContent(content);
            }
        }
        cursor=null;
        //점심 조회
        String query2="select Foods, Serving_wt from "+PersonalDbHelper.MealTable+
                " where Date='"+dateF.getDate()+"' and "+
                "Meal="+MealView.RequestLunch;
        cursor=database.rawQuery(query2, null);
        if(cursor!=null) {
            if(cursor.getCount()!=0){
                String content="";
                cursor.moveToNext();
                String[] foods=cursor.getString(0).split(";");
                String[] Serving_wts=cursor.getString(1).split(";");
                for(int i=0; i<foods.length; i++) {
                    content+=foods[i]+" "+Serving_wts[i]+"\n";
                }
                content=content.substring(0, content.length()-1);
                mealLunch.setContent(content);
            }
        }
        cursor=null;
        //저녁 조회
        String query3="select Foods, Serving_wt from "+PersonalDbHelper.MealTable+
                " where Date='"+dateF.getDate()+"' and "+
                "Meal="+MealView.RequestDinner;
        cursor=database.rawQuery(query3, null);
        if(cursor!=null) {
            if(cursor.getCount()!=0){
                String content="";
                cursor.moveToNext();
                String[] foods=cursor.getString(0).split(";");
                String[] Serving_wts=cursor.getString(1).split(";");
                for(int i=0; i<foods.length; i++) {
                    content+=foods[i]+" "+Serving_wts[i]+"\n";
                }
                content=content.substring(0, content.length()-1);
                mealDinner.setContent(content);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetData();
        }
    }
}
