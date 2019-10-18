package com.mtsealove.github.ohbody;

import android.app.Person;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.DateF;
import com.mtsealove.github.ohbody.Database.InnerDbHelper;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;
import com.mtsealove.github.ohbody.View.FoodSelectView;
import com.mtsealove.github.ohbody.View.MealView;

import java.util.ArrayList;

public class SearchFoodActivity extends AppCompatActivity {
    InnerDbHelper innerDbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    ListView SuggestLv;
    EditText SearchEt;
    Button confirmBtn;
    public static ArrayList<FoodSelectView> foodSelectViews;
    public static LinearLayout foodsLayout;
    int Meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
        SuggestLv = findViewById(R.id.suggestLv);
        SearchEt = findViewById(R.id.searchEt);
        foodsLayout = findViewById(R.id.foodsLayout);
        confirmBtn = findViewById(R.id.confirmBtn);

        foodSelectViews = new ArrayList<>();
        innerDbHelper = new InnerDbHelper(this, "data", null, 1);
        database = innerDbHelper.getReadableDatabase();
        Intent intent=getIntent();
        Meal=intent.getIntExtra("Meal", MealView.RequestBreakfast);
        SearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력값이 없으면 뷰 숨김
                if (charSequence.length() == 0) {
                    SuggestLv.setVisibility(View.GONE);
                } else {
                    SuggestLv.setVisibility(View.VISIBLE);
                    Suggest(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Done();
            }
        });
        ReadData();
    }

    //검색어 입력 시 추천
    private void Suggest(CharSequence nameCS) {
        final ArrayList<String> foodList = new ArrayList<>();
        final ArrayList<Integer> servingList = new ArrayList<>();
        final ArrayList<Integer> food_cdList=new ArrayList<>();
        String name = nameCS.toString();
        String query = "select desc_kor, food_cd, serving_wt from data where desc_kor like '" + name + "%'";
        //중복 제거
        for(FoodSelectView foodSelectView: foodSelectViews) {
            query+=" and not desc_kor='"+foodSelectView.getFoodName()+"'";
        }
        cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            foodList.add(cursor.getString(0));
            food_cdList.add(cursor.getInt(1));
            servingList.add(cursor.getInt(2));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, foodList);
        SuggestLv.setAdapter(arrayAdapter);
        //선택 이벤트
        SuggestLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchEt.setText("");
                FoodSelectView foodSelectView = new FoodSelectView(SearchFoodActivity.this, foodList.get(i), servingList.get(i), food_cdList.get(i));
                foodSelectViews.add(foodSelectView);
                SuggestLv.setVisibility(View.GONE);
                foodsLayout.addView(foodSelectView);
            }
        });
    }

    //완료 버튼 클릭 시
    private void Done() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("완료")
                .setMessage("저장하시겠습니까?")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String result="";
                for(FoodSelectView foodSelectView:foodSelectViews){
                    result+=foodSelectView.getFoodName()+"\n";
                }
                //변경된 데이터가 없으면 종료
                if(result.length()==0){
                    finish();
                    return;
                }
                result.substring(0, result.length()-3);
                Intent intent=new Intent();
                intent.putExtra("foods", result);
                setResult(RESULT_OK, intent);
                InsertData();
                finish();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    //데이터베이스 삽입
    private void InsertData() {
        PersonalDbHelper dbHelper=new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        DateF dateF=new DateF();
        //데이터를 모두 지우고
        dbHelper.CleanMeal(database, dateF.getDate(), Meal);
        //새 데이터를 다시 삽입
        for(FoodSelectView foodSelectView:foodSelectViews) {
            dbHelper.InsertMealData(database, dateF.getDate(), Meal, foodSelectView.getFoodName(), foodSelectView.getAmount(), foodSelectView.getFood_cd());
        }
    }

    //기존 생성된 데이터 추가
    private void ReadData() {
        PersonalDbHelper dbHelper=new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        SQLiteDatabase database=dbHelper.getReadableDatabase();
        DateF dateF=new DateF();
        String query="select desc_kor, serving_wt, food_cd from "+PersonalDbHelper.MealTable+
                " where Date='"+dateF.getDate()+"' and "+
                "Meal="+Meal;
        Cursor cursor=database.rawQuery(query, null);
        if(cursor!=null) {
            if(cursor.getCount()!=0){
                while(cursor.moveToNext()){
                       FoodSelectView foodSelectView=new FoodSelectView(this, cursor.getString(0), cursor.getInt(1), cursor.getInt(2));
                       foodSelectViews.add(foodSelectView);
                       foodsLayout.addView(foodSelectView);
                }
            }
        }
    }
}
