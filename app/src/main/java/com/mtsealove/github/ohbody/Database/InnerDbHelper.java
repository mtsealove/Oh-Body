package com.mtsealove.github.ohbody.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.*;

//Asset에서 사용할 DB 클래스
public class InnerDbHelper extends SQLiteOpenHelper {

    public InnerDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "food_search.db", factory, version);
        setDB(context);
    }

    public InnerDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, "food_search.db", factory, version, errorHandler);
        setDB(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public InnerDbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, "food_search.db", version, openParams);
        setDB(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void setDB(Context ctx) {
        File folder = new File("/data/data/" + ctx.getPackageName() + "/databases/");
        if (folder.exists()) {

        } else {
            Log.e("sqlite", "파일 생성");
            folder.mkdirs();
        }
        AssetManager assetManager = ctx.getResources().getAssets();
        File outfile = new File("/data/data/" + ctx.getPackageName() + "/databases/food_search.db");
        InputStream is = null;
        FileOutputStream fo = null;
        long filesize = 0;
        try {
            is = assetManager.open("food_search.db", AssetManager.ACCESS_BUFFER);
            filesize = is.available();
            if (outfile.length() <= 0) {
                byte[] tempdata = new byte[(int) filesize];
                is.read(tempdata);
                is.close();
                outfile.createNewFile();
                fo = new FileOutputStream(outfile);
                fo.write(tempdata);
                fo.close();
                Log.e("sqlite", "파일 복사");
            } else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
