package com.mtsealove.github.ohbody;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

public class DbHelper extends SQLiteOpenHelper {
    public static final String TableName="Person";
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public DbHelper(Context context, String name, int version, SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Query="CREATE TABLE "+TableName+"(" +
                "Height double," +
                "Weight double, " +
                "Age int," +
                "Gender char," +
                "Disease varchar(1000))";
        try{
            db.execSQL(Query);
        }catch (SQLException e){}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void InsertData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease){
        String Query="insert into "+TableName+" values(" +
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

    public void UpdateData(SQLiteDatabase db, float height, float weight, int age, char gender, String disease){
        String Query="update "+TableName+" set "+
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
}
