package com.mtsealove.github.ohbody.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.util.logging.Level;

public class PersonalDbHelper extends SQLiteOpenHelper {
    public static final String BodyTable="Body", MealTable="Meal";

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
        Log.e("sql", "oncreate");
        String Query1="CREATE TABLE "+BodyTable+"(" +
                "Height double," +
                "Weight double, " +
                "Age int," +
                "Gender char," +
                "Disease varchar(1000))";

        try{
            db.execSQL(Query1);
        }catch (SQLException e){}

        String Query2="CREATE TABLE "+MealTable+"("+
                "Date varchar(15), "+
                "Meal int, "+
                "Foods varchar(1000), "+
                "Serving_wt varchar(1000) )";
        try {
            db.execSQL(Query2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //신체정보 데이터 삽입
    public void InsertBodyData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease){
        String Query="insert into "+BodyTable+" values(" +
                height+", "+
                weight+", "+
                age+", "+
                "'"+gender+"', "+
                "'"+disease+"')";
        db.beginTransaction();
        try{
            Log.e("쿼리", Query);
            db.execSQL(Query);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //신체정보 데이터 변경
    public void UpdateBodyData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease){
        String Query="update "+BodyTable+" set "+
                "Height="+height+
                ", Weight="+weight+
                ", Age="+age+
                ", Gender='"+gender+"'"+
                ", Disease='"+disease+"'";
        db.beginTransaction();
        try{
            db.execSQL(Query);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    //식사 데이터 추가
    public void InsertMealData(SQLiteDatabase db, String date, int meal, String foods, String serving_wt) {
        //원 데이터 삭제
        String query0="delete from "+MealTable+" where date='"+date+"' and meal="+meal;
        String query="insert into "+MealTable +" values(" +
                "'"+date+"', "+
                meal+", "+
                "'"+foods+"', "+
                "'"+serving_wt+"' "+
                ")";
        db.beginTransaction();
        try {
            db.execSQL(query0);
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
