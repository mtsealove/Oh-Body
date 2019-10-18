package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.mtsealove.github.ohbody.Database.DateF;
import com.mtsealove.github.ohbody.Database.InnerDbHelper;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;

import java.util.ArrayList;
import java.util.logging.Level;

public class MainActivity extends AppCompatActivity {
    Button menuBtn, mealBtn;
    DrawerLayout drawerLayout;
    TextView shortTV;
    LinearLayout informationLayout, graphLayout, mealLayout;
    PersonalDbHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuBtn = findViewById(R.id.menuBtn);
        mealBtn = findViewById(R.id.mealBtn);
        drawerLayout = findViewById(R.id.drawerLayout);
        shortTV = findViewById(R.id.shortTV);
        informationLayout = findViewById(R.id.informationLayout);
        graphLayout = findViewById(R.id.graphLayout);
        mealLayout = findViewById(R.id.mealLayout);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer();
            }
        });

        mealBtn.setOnClickListener(MealListener);
        informationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });
        mealLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MealActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });
        graphLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });

        shortTV.setText("오늘은 " + GetWalkCount() + "걸음을 걸었네요\n필요 에너지량은 " + GetEER() + "kcal 이예요");
    }

    //메뉴 버튼으로 메뉴 열기
    private void OpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    //메뉴 닫아주기
    private void CloseDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
    }

    private View.OnClickListener MealListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MealActivity.class);
            startActivity(intent);
            CloseDrawer();
        }
    };

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    public void onBackPressed() {
        //드로워가 열려있으면 닫기
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {    //2번 눌러 종료
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //걸음수 반환
    private int GetWalkCount() {
        DateF dateF = new DateF();
        dbHelper = new PersonalDbHelper(this, PersonalDbHelper.WalkTable, null, 1);
        database = dbHelper.getReadableDatabase();
        String query = "select WalkCount from " + PersonalDbHelper.WalkTable + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                return count;
            }
        }
        return 0;
    }

    //필요 에너지량 반환
    private int GetEER() {
        String gender;
        int age, activity;
        float height, weight;
        double result = 0;
        float PA = 1;
        dbHelper = new PersonalDbHelper(this, PersonalDbHelper.BodyTable, null, 1);
        database = dbHelper.getReadableDatabase();
        String query = "select Gender, Age, Height, Activity, Weight from " + PersonalDbHelper.BodyTable;
        cursor = database.rawQuery(query, null);
        //데이터 추출
        if (cursor != null) {
            if (cursor.moveToNext()) {
                gender = cursor.getString(0);
                age = cursor.getInt(1);
                height = cursor.getFloat(2);
                activity = cursor.getInt(3);
                weight = cursor.getFloat(4);
                Log.e("성별", gender);
                Log.e("나이", String.valueOf(age));
                Log.e("height", String.valueOf(height));
                Log.e("activity", String.valueOf(activity));
                Log.e("weight", String.valueOf(weight));

                //여자일 경우
                if (gender.equals("F")) {
                    switch (activity) {
                        case 0:
                            PA = 1.0f;
                            break;
                        case 1:
                            PA = 1.12f;
                            break;
                        case 2:
                            PA = 1.27f;
                            break;
                        case 3:
                            PA = 1.45f;
                            break;
                        default:
                            PA = 1.25f;
                    }
                    result = 354 - 6.91 * age + PA * (9.36 * weight + 726 * (height / 100));
                } else {
                    switch (activity) {
                        case 0:
                            PA = 1.0f;
                            break;
                        case 1:
                            PA = 1.11f;
                            break;
                        case 2:
                            PA = 1.25f;
                            break;
                        case 3:
                            PA = 1.48f;
                            break;
                        default:
                            PA = 1.25f;
                    }
                    result = 662 - 9.53 * age + PA * (15.91 * weight + 539.6 * (height / 100));
                }
                return (int) result;
            }
        }
        return 0;
    }

    //오늘 먹은 칼로리 반환
    private double GetKCal() {
        //식품 코드리스트
        ArrayList<Integer> foodCds = new ArrayList<>();
        DateF dateF = new DateF();
        dbHelper = new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        database = dbHelper.getReadableDatabase();
        String query = "select food_cd from " + PersonalDbHelper.MealTable + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);

        //리스트에 코드 추가
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    foodCds.add(cursor.getInt(0));
                }
            }
        }
        //칼로리의 합
        double kcal=0;

        InnerDbHelper innerDbHelper=new InnerDbHelper(this, "food_search.db", null, 1);
        database=innerDbHelper.getReadableDatabase();
        for(Integer food_cd:foodCds) {

            Log.e("food_cd", String.valueOf(food_cd));
            String kcalQuery="select kcal from data where food_cd="+food_cd;
            cursor=database.rawQuery(kcalQuery, null);
            if(cursor!=null)  {
                if(cursor.getCount()!=0) {
                    while(cursor.moveToNext()) {
                        kcal+=cursor.getDouble(0);
                    }
                }
            }
        }
        return kcal;
    }


    @Override
    protected void onResume() {
        super.onResume();
        shortTV.setText("오늘은 " + GetWalkCount() + "걸음을 걸었네요\n필요 에너지량은 " + GetEER() + "kcal 이예요\n칼로리는 "+GetKCal()+"kcal 섭취했어요");
    }
}
