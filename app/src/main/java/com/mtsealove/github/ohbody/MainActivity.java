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
import com.mtsealove.github.ohbody.Database.Recipes.RecipeDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    Button menuBtn, mealBtn;
    DrawerLayout drawerLayout;
    TextView shortTV, tipTv;
    LinearLayout informationLayout, graphLayout, mealLayout, recipeLayout;
    PersonalDbHelper dbHelper;
    SQLiteDatabase database;
    Cursor cursor;
    double RequireKcal;
    Nutrient RecommendNutrient;
    double protein = 0, carbohydrate = 0, fat = 0, natrium = 0, sugar = 0, trans = 0, sfa = 0, cholesterol = 0;
    ArrayList<String> Require, TooMuch; //필요&결핍 필드명
    String suggestFoodCode;

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
        tipTv = findViewById(R.id.tipTv);
        recipeLayout=findViewById(R.id.recipeLayout);

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
        recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, SearchRecipeActivity.class);
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
        cursor.close();
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
            cursor.close();
        }
        return 0;
    }

    ArrayList<Double> Scope = new ArrayList<>();

    //오늘 먹은 칼로리 반환
    private double GetKCal() {
        //식품 코드리스트
        ArrayList<String> foodCds = new ArrayList<>();
        ArrayList<Integer> serving_wts = new ArrayList<>();
        DateF dateF = new DateF();
        dbHelper = new PersonalDbHelper(this, PersonalDbHelper.MealTable, null, 1);
        database = dbHelper.getReadableDatabase();
        String query = "select food_cd, serving_wt from " + PersonalDbHelper.MealTable + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);

        //리스트에 코드 추가
        if (cursor != null) {
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    foodCds.add(cursor.getString(0));
                    serving_wts.add(cursor.getInt(1));
                }
            }
        }
        //칼로리의 합
        double kcal = 0;
        //섭취한 식품에 따른 칼로리 계산
        InnerDbHelper innerDbHelper = new InnerDbHelper(this, "food_search.db", null, 1);
        database = innerDbHelper.getReadableDatabase();
        int w = 0;
        for (String food_cd : foodCds) {
            Log.e("food_cd", String.valueOf(food_cd));
            String kcalQuery = "select kcal, serving_wt from data where food_cd='" + food_cd + "'";
            cursor = database.rawQuery(kcalQuery, null);
            if (cursor != null) {
                if (cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {
                        double x = (double) (serving_wts.get(w)) / (double) (cursor.getInt(1));
                        Log.e("배수: ", String.valueOf(serving_wts.get(w)));
                        Log.e("칼로리: ", String.valueOf(cursor.getDouble(0) * (x)));
                        Scope.add(x);
                        Log.e("배수:", String.valueOf(x));
                        kcal += cursor.getDouble(0) * (x);
                        w++;
                    }
                }
            }
        }
        cursor.close();
        return kcal;
    }

    /*
    오늘의 tip
    병이 없는 경우 영양소 중 별로 섭취하지 않은 음식 추천
    지병을 가진 경우 관련 음식 추천
     */
    private String GetTodayTip() {
        //InnerDbHelper innerDbHelper = new InnerDbHelper(this, "food_search.db", null, 1);
        //SQLiteDatabase database = innerDbHelper.getReadableDatabase();
        RecipeDbHelper recipeDbHelper = new RecipeDbHelper(this, RecipeDbHelper.RecipeTable, null, 1);
        SQLiteDatabase database = recipeDbHelper.getReadableDatabase();

        Cursor cursor1;
        int state = 0;
        //String query = "select desc_kor from data where not protein='N/A' and not carbohyate='N/A' and not fat='N/A' ";
        String query = "select RCP_NM, RCP_SEQ from " + RecipeDbHelper.RecipeTable + " ";
        String result = "";
        //모든 경우에 대해 양수면 결핍 음수면 과다
        //3대 영양소에 대해 8개의 경우 존재
        //double 형식데이터이기에 아마 동일값 존재는 어려울듯
        if (protein > 0 && carbohydrate > 0 && fat > 0) {   //모두 결핌
            //식사를 해야됨 이정도면
            query += "order by INFO_ENG desc, INFO_CAR desc, INFO_PRO desc, INFO_FAT desc";
        } else if (protein > 0 && carbohydrate > 0 && fat <= 0) {//지방 과다 나머지 결핍
            if (protein / 2 > carbohydrate / 6)   //과다한 정도 판단
                query += "order by INFO_PRO desc, INFO_CAR desc";
            else
                query += "order by INFO_CAR desc, INFO_PRO desc";
            query += ", INFO_FAT asc";
            state = 1;
        } else if (protein > 0 && carbohydrate <= 0 && fat > 0) {//탄수화물 과다 나머지 결핍
            if (protein > fat)
                query += "order by INFO_PRO desc, INFO_FAT desc";
            else query += "order by INFO_FAT desc, INFO_PRO desc";
            query += ", INFO_CAR asc";
            state = 2;
        } else if (protein > 0 && carbohydrate <= 0 && fat <= 0) {  //탄수화물/지방 과다 단백질 결핍
            query += "order by INFO_PRO desc";
            if (carbohydrate / 6 > fat / 2) query += ", INFO_CAR asc, INFO_FAT asc";
            else query += ", INFO_FAT asc, INFO_CAR asc";
            state = 3;
        } else if (protein <= 0 && carbohydrate > 0 && fat > 0) {   //단백질 과다
            query += "order by INFO_PRO asc";
            if (carbohydrate / 6 > fat / 2) query += ", INFO_CAR desc, INFO_FAT desc";
            else query += ", INFO_FAT desc, INFO_CAR desc";
            state = 4;
        } else if (protein <= 0 && carbohydrate > 0 && fat <= 0) {  //단백질/지방 과다
            query += "order by INFO_CAR desc";
            if (protein > fat) query += ", INFO_PRO asc, INFO_FAT asc";
            else query += ", INFO_FAT asc, INFO_PRO asc";
            state = 5;
        } else if (protein <= 0 && carbohydrate <= 0 && fat > 0) {  //단백질/탄수화물 과다
            query += "order by INFO_FAT desc";
            if (protein / 2 < carbohydrate / 6) query += ", INFO_PRO asc, INFO_CAR asc";
            else query += ", INFO_CAR asc, INFO_PRO asc";
            state = 6;
        } else if (protein <= 0 && carbohydrate <= 0 && fat <= 0) { //모두 과다
            query += "order by INFO_ENG asc, INFO_CAR asc, INFO_PRO asc, INFO_FAT asc";
            state = 7;
        }

        switch (state) {
            case 0:
                result += "식사를 거르지 마세요.\n영양소 높은 ";
                break;
            case 1:
                result += "지방이 많은 음식을 많이 드셨네요.\n지방이 적은 ";
                break;
            case 2:
                result += "탄수화물을 많이 먹었어요.\n탄수화물이 적은";
                break;
            case 3:
                result += "단백질이 부족해요.\n단백질이 많은 ";
                break;
            case 4:
                result += "탄수화물과 지방이 너무 적어요.\n맛있는 ";
                break;
            case 5:
                result += "탄수화물이 부족해요.\n탄수화물로 가득찬 ";
                break;
            case 6:
                result += "지방이 적어요.\n기름기있는 ";
                break;
            case 7:
                result += "식사를 너무 많이 하셨어요.\n열량이 적은 ";
        }
        cursor1 = database.rawQuery(query, null);
        if (cursor1 != null) {
            if (cursor1.getCount() != 0) {
                //상위 30개 중 선택
                int index = (int) (Math.random() * 30)+1;
                cursor1.move(index);
                result += cursor1.getString(0).replace("\\,", "");
                suggestFoodCode = cursor1.getString(1);
            }
        }
        cursor1.close();
        result += "은(는) 어때요?";

        return result;
    }

    //섭취가 부족한 영향소 포함 음식 추천
    private void GetNutrient() {
        //데이터 초기
        protein = 0;
        carbohydrate = 0;
        fat = 0;
        natrium = 0;
        sugar = 0;
        trans = 0;
        sfa = 0;
        cholesterol = 0;
        PersonalDbHelper dbHelper = new PersonalDbHelper(this, PersonalDbHelper.NutrientTable, null, 1);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor;

        DateF dateF = new DateF();
        String query = "select protein, carbohydrate, fat, natrium, sugar, trans, sfa, cholesterol from " + PersonalDbHelper.NutrientTable
                + " where Date='" + dateF.getDate() + "'";
        cursor = database.rawQuery(query, null);
        if (cursor != null&&cursor.getCount() != 0) {
                int i = 0;
                while (cursor.moveToNext()) {
                    //모든 값을 더하기
                    protein += cursor.getDouble(0) * Scope.get(i);
                    carbohydrate += cursor.getDouble(1) * Scope.get(i);
                    fat += cursor.getDouble(2) * Scope.get(i);
                    natrium += cursor.getDouble(3) * Scope.get(i);
                    sugar += cursor.getDouble(4) * Scope.get(i);
                    trans += cursor.getDouble(5) * Scope.get(i);
                    sfa += cursor.getDouble(6) * Scope.get(i);
                    cholesterol += cursor.getDouble(7) * Scope.get(i);
                    i++;
                }

            cursor.close();
        }
        //전체 데이터의 평균 계산
        protein = RecommendNutrient.getProtein() - protein;
        carbohydrate = RecommendNutrient.getCarbohydrate() - carbohydrate;
        fat = RecommendNutrient.getFat() - fat;
        natrium = RecommendNutrient.getNatrium() - natrium;
        sugar = RecommendNutrient.getSugar() - sugar;
        trans = RecommendNutrient.getTrans() - trans;
        sfa = RecommendNutrient.getSFA() - sfa;
        cholesterol = RecommendNutrient.getCholesterol() - cholesterol;

        System.out.println("단백질: " + protein);
        System.out.println("탄수화물: " + carbohydrate);
        System.out.println("지방: " + fat);
        System.out.println("나트륨: " + natrium);
        System.out.println("당: " + sugar);
        System.out.println("트랜스: " + trans);
        System.out.println("포화지방산: " + sfa);
        System.out.println("콜레스테롤: " + cholesterol);
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
        shortTV.setText("오늘은 " + GetWalkCount() + "걸음을 걸었네요\n필요 에너지량은 " + GetEER()
                + "kcal 이예요\n칼로리는 " + GetKCal() + "kcal 섭취했어요\n" +
                "걸음으로 " + (GetWalkCount() * 0.04) + "칼로리를 소모했어요");
        GetNutrient();
        tipTv.setText(GetTodayTip());
        tipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                intent.putExtra("food_cd", suggestFoodCode);
                startActivity(intent);
            }
        });
    }

}
