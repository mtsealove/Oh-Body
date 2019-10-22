package com.mtsealove.github.ohbody.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class HealthFoodDbHelper extends SQLiteOpenHelper {
    public final  static String HealthFoodTable="HealthFood";
    public HealthFoodDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public HealthFoodDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public HealthFoodDbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="create table "+HealthFoodTable
                +"(PRMS_DT varchar(45), " +
                "BSSH_NM varchar(45), " +
                "APLC_RAWMTRL_NM varchar(45)," +
                " HF_FNCLTY_MTRAL_RCOGN_NO varchar(45)," +
                " INDUTY_NM varchar(45)," +
                " IFTKN_ATNT_MATR_CN varchar(45)," +
                " ADDR varchar(45)," +
                " FNCLTY_CN varchar(45)," +
                " DAY_INTK_CN varchar(45)," +
                " Date varchar(45))";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CleanData(SQLiteDatabase db){
        String query="delete from "+HealthFoodTable;
        db.beginTransaction();
        try{
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void InsertData(SQLiteDatabase db, HealthFood healthFood) {
        String query="insert into "+HealthFoodTable+ " values(" +
                "'"+healthFood.getPRMS_DT()+"', "+
                "'"+healthFood.getBSSH_NM()+"', "+
                "'"+healthFood.getAPLC_RAWMTRL_NM()+"', "+
                "'"+healthFood.getHF_FNCLTY_MTRAL_RCOGN_NO()+"', "+
                "'"+healthFood.getINDUTY_NM()+"', "+
                "'"+healthFood.getIFTKN_ATNT_MATR_CN()+"', "+
                "'"+healthFood.getADDR()+"', "+
                "'"+healthFood.getFNCLTY_CN()+"', "+
                "'"+healthFood.getDAY_INTK_CN()+"', "+
                "'"+healthFood.getDate()+"'"+
                ")";
        db.beginTransaction();
        try{
            db.execSQL(query);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
