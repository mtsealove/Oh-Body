package com.mtsealove.github.ohbody.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

//개인정보 관련 DB 클래스
public class PersonalDbHelper extends SQLiteOpenHelper {
    public static final String BodyTable = "Body", MealTable = "Meal", WalkTable = "Walk", NutrientTable = "Nutrient";

    public PersonalDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public PersonalDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public PersonalDbHelper(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //신체정보 db 설정
        String Query1 = "CREATE TABLE " + BodyTable + "(" +
                "Height double," +
                "Weight double, " +
                "Age int," +
                "Gender char," +
                "Disease varchar(1000)," +
                "Activity int," +
                "Btime varchar(20)," +
                "Ltime varchar(20)," +
                "Dtime varchar(20))";

        try {
            db.execSQL(Query1);
        } catch (SQLException e) {
        }

        //식사 db 설정
        String Query2 = "CREATE TABLE " + MealTable + "(" +
                "Date varchar(15), " +
                "Meal int, " +
                "desc_kor varchar(45)," +
                "serving_wt int, " +
                "food_cd int)";
        try {
            db.execSQL(Query2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //걸음 db 설정
        String Query3 = "CREATE TABLE " + WalkTable + "(" +
                "Date varchar(15), " +
                "WalkCount int" +
                ")";
        try {
            db.execSQL(Query3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //영양소 db 설정
        String Query4 = "CREATE TABLE " + NutrientTable + "(" +
                "Date varchar(15), " +
                "Meal int, " +
                "Protein double, " +
                "Carbohydrate double, " +
                "Fat double, " +
                "Natrium double, " +
                "Sugar double, " +
                "Trans double, " +
                "SFA double, " +
                "Cholesterol double)";
        try {
            db.execSQL(Query4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //신체정보 데이터 삽입
    public void InsertBodyData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease, int Activity, String Btime, String Ltime, String Dtime) {
        String Query = "insert into " + BodyTable + " values(" +
                height + ", " +
                weight + ", " +
                age + ", " +
                "'" + gender + "', " +
                "'" + disease + "'," +
                Activity + ", " +
                "'" + Btime + "', " +
                "'" + Ltime + "', " +
                "'" + Dtime + "')";
        db.beginTransaction();
        try {
            Log.e("쿼리", Query);
            db.execSQL(Query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //신체정보 데이터 변경
    public void UpdateBodyData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease, int Activity, String Btime, String Ltime, String Dtime) {
        String Query = "update " + BodyTable + " set " +
                "Height=" + height +
                ", Weight=" + weight +
                ", Age=" + age +
                ", Gender='" + gender + "'" +
                ", Disease='" + disease + "'" +
                ", Activity=" + Activity +
                ", Btime='" + Btime + "'" +
                ", Ltime='" + Ltime + "'" +
                ", Dtime='" + Dtime + "'";
        db.beginTransaction();
        try {
            db.execSQL(Query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //식사 원 데이터 삭제
    public void CleanMeal(SQLiteDatabase db, String date, int meal) {
        String query0 = "delete from " + MealTable + " where date='" + date + "' and meal=" + meal;
        db.beginTransaction();
        try {
            db.execSQL(query0);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //식사 데이터 추가
    public void InsertMealData(SQLiteDatabase db, String date, int meal, String food, int serving_wt, String food_cd) {
        String query = "insert into " + MealTable + " values(" +
                "'" + date + "', " +
                meal + ", " +
                "'" + food + "', " +
                serving_wt + ", " +
                "'" + food_cd + "')";
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //걸음 데이터 생성
    public void CreateWalkData(SQLiteDatabase db) {
        DateF dateF = new DateF();
        String query = "insert into " + WalkTable + " values('" + dateF.getDate() + "', 0)";
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //걸음 데이터 업데이트
    public void UpdateWalkData(SQLiteDatabase db, int walk_count) {
        DateF dateF = new DateF();
        String query = "update " + WalkTable + " set WalkCount=" + walk_count + " where Date='" + dateF.getDate() + "'";
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //영양소 초기화
    public void CleanNutrient(SQLiteDatabase db, String date, int meal) {
        String query = "delete from " + NutrientTable + " where Date='" + date + "' and Meal=" + meal;
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //영양소 설정
    public void SetNutrient(SQLiteDatabase db, int meal, double protein, double carbohydrate, double fat, double natrium, double sugar, double trans, double sfa, double cholesterol) {
        DateF dateF = new DateF();
        String query = "insert into " + NutrientTable + " values('" + dateF.getDate() + "', " +
                meal + "," +
                protein + ", " +
                carbohydrate + ", " +
                fat + ", " +
                natrium + ", " +
                sugar + ", " +
                trans + ", " +
                sfa + ", " +
                cholesterol +
                ")";
        db.beginTransaction();
        try {
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

}
