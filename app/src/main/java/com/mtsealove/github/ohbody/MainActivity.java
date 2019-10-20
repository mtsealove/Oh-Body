package com.mtsealove.github.ohbody;

import android.app.ProgressDialog;
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
import com.mtsealove.github.ohbody.Database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button menuBtn, mealBtn;
    DrawerLayout drawerLayout;
    TextView shortTV, tipTv;
    LinearLayout informationLayout, graphLayout, mealLayout;
    PersonalDbHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    double RequireKcal;
    Nutrient RecommendNutrient;
    double protein = 0, carbohydrate = 0, fat = 0, natrium = 0, sugar = 0, trans = 0, sfa = 0, cholesterol = 0;
    ArrayList<String> Require, TooMuch; //필요&결핍 필드명

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
        tipTv=findViewById(R.id.tipTv);

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
                Intent intent = new Intent(MainActivity.this, SearchHealthFoodActivity.class);
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

        initTips();
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
        ArrayList<String> foodCds = new ArrayList<>();
        DateF dateF = new DateF();
        dbHelper = new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        database = dbHelper.getReadableDatabase();
        String query = "select food_cd from " + PersonalDbHelper.MealTable + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);

        //리스트에 코드 추가
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    foodCds.add(cursor.getString(0));
                }
            }
        }
        //칼로리의 합
        double kcal = 0;
        //섭취한 식품에 따른 칼로리 계산
        InnerDbHelper innerDbHelper = new InnerDbHelper(this, "food_search.db", null, 1);
        database = innerDbHelper.getReadableDatabase();
        for (String food_cd : foodCds) {

            Log.e("food_cd", String.valueOf(food_cd));
            String kcalQuery = "select kcal from data where food_cd='" + food_cd + "'";
            cursor = database.rawQuery(kcalQuery, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        kcal += cursor.getDouble(0);
                    }
                }
            }
        }
        return kcal;
    }

    /*
    오늘의 tip
    병이 없는 경우 영양소 중 별로 섭취하지 않은 음식 추천
    지병을 가진 경우 관련 음식 추천
     */
    private String GetTodayTip() {
        InnerDbHelper innerDbHelper = new InnerDbHelper(this, "food_search.db", null, 1);
        SQLiteDatabase database = innerDbHelper.getReadableDatabase();
        Cursor cursor1;
        int state=0;
        String query = "select desc_kor from data where not protein='N/A' and not carbohyate='N/A' and not fat='N/A' ";
        String result = "";
        //모든 경우에 대해 양수면 결핍 음수면 과다
        //3대 영양소에 대해 8개의 경우 존재
        //double 형식데이터이기에 아마 동일값 존재는 어려울듯
        if (protein > 0 && carbohydrate > 0 && fat > 0) {   //모두 결핌
            //식사를 해야됨 이정도면
            query += "order by kcal desc, protein desc, carbohyate desc, fat desc";
        } else if (protein > 0 && carbohydrate > 0 && fat <= 0) {//지방 과다 나머지 결핍
            if (protein / 2 > carbohydrate / 6)   //과다한 정도 판단
                query += "order by protein desc, carbohyate desc";
            else
                query += "order by carbohyate desc, protein desc";
            query += ", fat asc";
            state=1;
        } else if (protein > 0 && carbohydrate <= 0 && fat > 0) {//탄수화물 과다 나머지 결핍
            if (protein > fat)
                query += "order by protein desc, fat desc";
            else query += "order by fat desc, protein desc";
            query += ", carbohyate asc";
            state=2;
        } else if (protein > 0 && carbohydrate <= 0 && fat <= 0) {  //탄수화물/지방 과다 단백질 결핍
            query += "order by protein desc";
            if (carbohydrate / 6 > fat / 2) query += ", carbohyate asc, fat asc";
            else query += ", fat asc, carbohyate asc";
            state=3;
        } else if (protein <= 0 && carbohydrate > 0 && fat > 0) {   //단백질 과다
            query += "order by protein asc";
            if (carbohydrate / 6 > fat / 2) query += ", carbohyate desc, fat desc";
            else query += ", fat desc, carbohyate desc";
            state=4;
        } else if (protein <= 0 && carbohydrate > 0 && fat <= 0) {  //단백질/지방 과다
            query += "order by carbohyate desc";
            if (protein > fat) query += ", protein asc, fat asc";
            else query += ", fat asc, protein asc";
            state=5;
        } else if (protein <= 0 && carbohydrate <= 0 && fat > 0) {  //단백질/탄수화물 과다
            query += "order by fat desc";
            if (protein / 2 < carbohydrate / 6) query += ", protein asc, carbohyate asc";
            else query += ", carbohyate asc, protein asc";
            state=6;
        } else if (protein <= 0 && carbohydrate <= 0 && fat <= 0) { //모두 과다
            query += "order by kcal asc, fat asc, protein asc, protein asc";
            state=7;
        }

        switch (state){
            case 0: result+="식사를 거르지 마세요.\n영양소 높은 ";
            break;
            case 1: result+="지방이 많은 음식을 많이 드셨네요.\n지방이 적은 ";
            break;
            case 2: result+="탄수화물을 많이 먹었어요.\n탄수화물이 적은";
            break;
            case 3: result+="단백질이 부족해요.\n단백질이 많은 ";
            break;
            case 4: result+="탄수화물과 지방이 너무 적어요.\n맛있는 ";
            break;
            case 5: result+="탄수화물이 부족해요.\n탄수화물로 가득찬 ";
            break;
            case 6: result+="지방이 적어요.\n기름기있는 ";
            break;
            case 7: result+="식사를 너무 많이 하셨어요.\n열량이 적은 ";
        }
        cursor1 = database.rawQuery(query, null);
        if (cursor1 != null) {
            if (cursor1.getCount() != 0) {
                //상위 30개 중 선택
                int index = (int) (Math.random() * 30);
                cursor1.move(index);
                result+=cursor1.getString(0).replace("\\,", "");
            }
        }
        result+="은(는) 어때요?";

        return result;
    }

    //섭취가 부족한 영향소 포함 음식 추천
    private void GetNutrient() {
        PersonalDbHelper dbHelper = new PersonalDbHelper(this, PersonalDbHelper.NutrientTable, null, 1);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        String query = "select protein, carbohydrate, fat, natrium, sugar, trans, sfa, cholesterol from " + PersonalDbHelper.NutrientTable;
        cursor = database.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                int cnt = cursor.getCount();
                while (cursor.moveToNext()) {
                    //모든 값을 더하기
                    protein += cursor.getDouble(0);
                    carbohydrate += cursor.getDouble(1);
                    fat += cursor.getDouble(2);
                    natrium += cursor.getDouble(3);
                    sugar += cursor.getDouble(4);
                    trans += cursor.getDouble(5);
                    sfa += cursor.getDouble(6);
                    cholesterol += cursor.getDouble(7);
                }
                //전체 데이터의 평균 계산
                protein = RecommendNutrient.getProtein() - protein / cnt;
                carbohydrate = RecommendNutrient.getCarbohydrate() - carbohydrate / cnt;
                fat = RecommendNutrient.getFat() - fat / cnt;
                natrium = RecommendNutrient.getNatrium() - natrium / cnt;
                sugar = RecommendNutrient.getSugar() - sugar / cnt;
                trans = RecommendNutrient.getTrans() - trans / cnt;
                sfa = RecommendNutrient.getSFA() - sfa / cnt;
                cholesterol = RecommendNutrient.getCholesterol() - cholesterol / cnt;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTips();
    }

    private void initTips() {
        Require = new ArrayList<>();
        TooMuch = new ArrayList<>();
        RequireKcal = GetEER();
        RecommendNutrient = new Nutrient(RequireKcal); //권장 영양소량 계산
        GetNutrient();
        shortTV.setText("오늘은 " + GetWalkCount() + "걸음을 걸었네요\n필요 에너지량은 " + GetEER()
                + "kcal 이예요\n칼로리는 " + GetKCal() + "kcal 섭취했어요\n" +
                "걸음으로 " + (GetWalkCount() * 0.04) + "칼로리를 소모했어요");
        tipTv.setText(GetTodayTip());
    }

}
