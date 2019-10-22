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
import com.mtsealove.github.ohbody.Database.InnerDbHelper;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;
import com.mtsealove.github.ohbody.View.MealView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        PersonalDbHelper dbHelper = new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        PersonalDbHelper nutrientDbHelper = new PersonalDbHelper(this, PersonalDbHelper.NutrientTable, null, 1);
        InnerDbHelper innerDbHelper = new InnerDbHelper(this, "food_search.db", null, 1);
        SQLiteDatabase innerDb = innerDbHelper.getReadableDatabase();
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        SQLiteDatabase nutrientDb = nutrientDbHelper.getWritableDatabase();
        DateF dateF = new DateF();
        Cursor cursor, cursor1;
        //데이터를 조회하여 배치

        for (int i = 0; i < 3; i++) {
            ArrayList<String> food_cds = new ArrayList<>();
            ArrayList<Integer> serving_wts = new ArrayList<>();
            int tmpAsk;
            MealView mealView;
            switch (i) {
                case 0:
                    tmpAsk = MealView.RequestBreakfast;
                    mealView = mealBreakfast;
                    break;
                case 1:
                    tmpAsk = MealView.RequestLunch;
                    mealView = mealLunch;
                    break;
                default:
                    tmpAsk = MealView.RequestDinner;
                    mealView = mealDinner;
            }

            String query1 = "select desc_kor, serving_wt, food_cd from " + PersonalDbHelper.MealTable +
                    " where Date='" + dateF.getDate() + "' and " +
                    "Meal=" + tmpAsk;
            Log.e("query", query1);
            cursor = database.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    String result = "";
                    while (cursor.moveToNext()) {
                        result += cursor.getString(0) + " " + cursor.getInt(1) + "g\n";
                        //식품코드 데이터에 추가
                        food_cds.add(cursor.getString(2));
                        //식사량 추가
                        serving_wts.add(cursor.getInt(1));
                    }
                    result = result.substring(0, result.length() - 1);
                    mealView.setContent(result);
                }
                cursor.close();
            }
            //식품코드를 통해 섭취한 영양소 갱신
            //데이터 초기화
            nutrientDbHelper.CleanNutrient(nutrientDb, dateF.getDate(), tmpAsk);
            for (int j = 0; j < food_cds.size(); j++) {
                String food_cd = food_cds.get(j);
                double protein = 0;
                double carbohydrate = 0;
                double fat = 0;
                double natrium = 0;
                double sugar = 0;
                double trans = 0;
                double sfa = 0;
                double cholesterol = 0;
                double x = 1;
                String query = "select Protein, Carbohyate, Fat, Natrium, Sugar, Trans, Saturated_Fatty_Acid, Cholesterol, serving_wt " +
                        "from data where food_cd='" + food_cd + "'";
                cursor1 = innerDb.rawQuery(query, null);
                if (cursor1 != null) {
                    if (cursor1.getCount() != 0) {
                        while (cursor1.moveToNext()) {
                            //모든 데이터가 없을 수도 있음
                            try{
                                double serving_wt = serving_wts.get(j);
                                x = serving_wt / cursor1.getInt(8);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                protein = Double.parseDouble(cursor1.getString(0)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                carbohydrate = Double.parseDouble(cursor1.getString(1)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                fat = Double.parseDouble(cursor1.getString(2)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                natrium = Double.parseDouble(cursor1.getString(3)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                sugar = Double.parseDouble(cursor1.getString(4)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                trans = Double.parseDouble(cursor1.getString(5)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                sfa = Double.parseDouble(cursor1.getString(6)) * x;
                            } catch (Exception e) {
                            }
                            try {
                                cholesterol = Double.parseDouble(cursor1.getString(7)) * x;
                            } catch (Exception e) {
                            }

                        }
                        nutrientDbHelper.SetNutrient(nutrientDb, tmpAsk, protein, carbohydrate, fat, natrium, sugar, trans, sfa, cholesterol);
                    }
                    cursor1.close();
                }
            }
        }
        nutrientDbHelper.close();
        nutrientDb.close();
        innerDbHelper.close();
        innerDb.close();
        dbHelper.close();
        database.close();
    }

    //데이터를 갱신하면 영양소 데이터도 갱신
    private void SetNutrient() {
        PersonalDbHelper dbHelper = new PersonalDbHelper(this, PersonalDbHelper.NutrientTable, null, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetData();
        }
    }
}
