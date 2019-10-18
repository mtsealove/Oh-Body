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
        //데이터를 조회하여 배치

        for(int i=0; i<3; i++) {
            int tmpAsk;
            MealView mealView;
            switch (i) {
                case 0:tmpAsk=MealView.RequestBreakfast;
                mealView=mealBreakfast;
                break;
                case 1: tmpAsk=MealView.RequestLunch;
                mealView=mealLunch;
                break;
                default: tmpAsk=MealView.RequestDinner;
                mealView=mealDinner;
            }

            String query1 = "select desc_kor, serving_wt from " + PersonalDbHelper.MealTable +
                    " where Date='" + dateF.getDate() + "' and " +
                    "Meal=" + tmpAsk;
            Log.e("query", query1);
            cursor = database.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    String result = "";
                    while (cursor.moveToNext()) {
                        result += cursor.getString(0) + " " + cursor.getInt(1) + "g\n";
                    }
                    result = result.substring(0, result.length() - 1);
                    mealView.setContent(result);
                }
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
