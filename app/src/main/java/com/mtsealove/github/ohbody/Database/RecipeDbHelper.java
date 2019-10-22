package com.mtsealove.github.ohbody.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.mtsealove.github.ohbody.Database.Recipes.Recipe;

public class RecipeDbHelper extends SQLiteOpenHelper {
    public final static String RecipeTable = "Recipe";

    public RecipeDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public RecipeDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public RecipeDbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + RecipeTable + " (" +
                "RCP_SEQ varchar(45)," +
                " RCP_NM varchar(45), " +
                " RCP_WAY2 varchar(45)," +
                " RCP_PAT2 varchar(45), " +
                "INFO_WGT varchar(45)," +
                " INFO_ENG varchar(45), " +
                "INFO_CAR varchar(45), " +
                "INFO_PRO varchar(45), " +
                "INFO_FAT varchar(45), " +
                "INFO_NA varchar(45)," +
                " HASH_TAG varchar(45), " +
                "ATT_FILE_NO_MAIN varchar(45)," +
                " ATT_FILE_NO_MK varchar(45)," +
                " RCP_PARTS_DTLS varchar(45), " +
                "MANUAL01 varchar(45), " +
                "MANUAL_IMG01 varchar(45), " +
                "MANUAL02 varchar(45)," +
                " MANUAL_IMG02 varchar(45), " +
                "MANUAL03 varchar(45)," +
                " MANUAL_IMG03 varchar(45), " +
                "MANUAL04 varchar(45), " +
                "MANUAL_IMG04 varchar(45), " +
                "MANUAL05 varchar(45)," +
                " MANUAL_IMG05 varchar(45)," +
                " MANUAL06 varchar(45), " +
                "MANUAL_IMG06 varchar(45)," +
                " MANUAL07 varchar(45), " +
                "MANUAL_IMG07 varchar(45), " +
                "MANUAL08 varchar(45)," +
                " MANUAL_IMG08 varchar(45), " +
                "MANUAL09 varchar(45), " +
                "MANUAL_IMG09 varchar(45), " +
                "MANUAL10 varchar(45), " +
                "MANUAL_IMG10 varchar(45)," +
                " MANUAL11 varchar(45), " +
                "MANUAL_IMG11 varchar(45), " +
                "MANUAL12 varchar(45), " +
                "MANUAL_IMG12 varchar(45)," +
                " MANUAL13 varchar(45), " +
                "MANUAL_IMG13 varchar(45), " +
                "MANUAL14 varchar(45), " +
                "MANUAL_IMG14 varchar(45), " +
                "MANUAL15 varchar(45)," +
                " MANUAL_IMG15 varchar(45)," +
                " MANUAL16 varchar(45), " +
                "MANUAL_IMG16 varchar(45)," +
                " MANUAL17 varchar(45)," +
                " MANUAL_IMG17 varchar(45), " +
                "MANUAL18 varchar(45), " +
                "MANUAL_IMG18 varchar(45), " +
                "MANUAL19 varchar(45)," +
                " MANUAL_IMG19 varchar(45)," +
                " MANUAL20 varchar(45), " +
                "MANUAL_IMG20 varchar(45)," +
                "Date varchar(45))";
        db.execSQL(query);
    }

    public void CleanData(SQLiteDatabase db) {
        String query = "delete from " + RecipeDbHelper.RecipeTable;
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

    public void InsertData(SQLiteDatabase db, Recipe recipe) {
        String query = "insert into " + RecipeTable + " values("+
                "'"+recipe.getRCP_SEQ()+"',"+
                "'"+recipe.getRCP_NM()+"',"+
                "'"+recipe.getRCP_WAY2()+"',"+
                "'"+recipe.getRCP_PAT2()+"',"+
                "'"+recipe.getINFO_WGT()+"',"+
                "'"+recipe.getINFO_ENG()+"',"+
                "'"+recipe.getINFO_CAR()+"',"+
                "'"+recipe.getINFO_PRO()+"',"+
                "'"+recipe.getINFO_FAT()+"',"+
                "'"+recipe.getINFO_NA()+"',"+
                "'"+recipe.getHASH_TAG()+"',"+
                "'"+recipe.getATT_FILE_NO_MAIN()+"',"+
                "'"+recipe.getATT_FILE_NO_MK()+"',"+
                "'"+recipe.getRCP_PARTS_DTLS()+ "',"+
                "'"+recipe.getMANUAL01()+ "',"+
                "'"+recipe.getMANUAL_IMG01()+"',"+
                "'"+recipe.getMANUAL02()+"',"+
                "'"+recipe.getMANUAL_IMG02()+"',"+
                "'"+recipe.getMANUAL03()+"',"+
                "'"+recipe.getMANUAL_IMG03()+
                "',"+"'"+recipe.getMANUAL04()+"',"+
                "'"+recipe.getMANUAL_IMG04()+"',"+
                "'"+recipe.getMANUAL05()+"',"+
                "'"+recipe.getMANUAL_IMG05()+"',"+
                "'"+recipe.getMANUAL06()+"',"+
                "'"+recipe.getMANUAL_IMG06()+"',"+
                "'"+recipe.getMANUAL07()+"',"+
                "'"+recipe.getMANUAL_IMG07()+"',"+
                "'"+recipe.getMANUAL08()+"',"+
                "'"+recipe.getMANUAL_IMG08()+"',"+
                "'"+recipe.getMANUAL09()+"',"+
                "'"+recipe.getMANUAL_IMG09()+"',"+
                "'"+recipe.getMANUAL10()+"',"+
                "'"+recipe.getMANUAL_IMG10()+"',"+
                "'"+recipe.getMANUAL11()+"',"+
                "'"+recipe.getMANUAL_IMG11()+"',"+
                "'"+recipe.getMANUAL12()+"',"+
                "'"+recipe.getMANUAL_IMG12()+"',"+
                "'"+recipe.getMANUAL13()+"',"+
                "'"+recipe.getMANUAL_IMG13()+"',"+
                "'"+recipe.getMANUAL14()+"',"+
                "'"+recipe.getMANUAL_IMG14()+"',"+
                "'"+recipe.getMANUAL15()+"',"+
                "'"+recipe.getMANUAL_IMG15()+"',"+
                "'"+recipe.getMANUAL16()+"',"+
                "'"+recipe.getMANUAL_IMG16()+"',"+
                "'"+recipe.getMANUAL17()+"',"+
                "'"+recipe.getMANUAL_IMG17()+"',"+
                "'"+recipe.getMANUAL18()+"',"+
                "'"+recipe.getMANUAL_IMG18()+"',"+
                "'"+recipe.getMANUAL19()+"',"+
                "'"+recipe.getMANUAL_IMG19()+"',"+
                "'"+recipe.getMANUAL20()+"',"+
                "'"+recipe.getMANUAL_IMG20()+"'," +
                "'"+new DateF().getDate()+"')";
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
