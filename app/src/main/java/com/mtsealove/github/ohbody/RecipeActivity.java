package com.mtsealove.github.ohbody;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.mtsealove.github.ohbody.Database.RecipeDbHelper;

public class RecipeActivity extends AppCompatActivity {
    TextView foodNmTv;
    LinearLayout contentLayout;
    String food_cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        food_cd = getIntent().getStringExtra("food_cd");
        foodNmTv = findViewById(R.id.foodNmTv);
        contentLayout = findViewById(R.id.contentLayout);

        init();
    }

    //레시피 검색
    private void init() {
        RecipeDbHelper recipeDbHelper = new RecipeDbHelper(this, RecipeDbHelper.RecipeTable, null, 1);
        SQLiteDatabase database = recipeDbHelper.getReadableDatabase();
        String query = "select * from " + RecipeDbHelper.RecipeTable + " where RCP_SEQ='" + food_cd + "'";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToNext();
            String title = cursor.getString(1);
            foodNmTv.setText(title);

            for (int i = 2; i < 54; i++) {
                String text = "";
                switch (i) {
                    case 2: text="조리방법: ";
                    break;
                    case 3: text="요리 종류: ";
                    break;
                    case 4: text="중량(1인분): ";
                    break;
                    case 5: text="열량(kcal): ";
                    break;
                    case 6: text="탄수화물:";
                    break;
                    case 7: text="단백질: ";
                    break;
                    case 8: text="지방: ";
                    break;
                    case 9: text="나트륨: ";
                    break;
                    case 10:
                    case 11:
                        continue;
                    case 13: text="재료정보: ";
                    break;

                }
                if (cursor.getString(i).length() != 0) {    //이미지 처리
                    if(cursor.getString(i).contains("http")){
                        ImageView imageView=new ImageView(this);
                        Glide.with(this).load(cursor.getString(i)).into(imageView);
                        contentLayout.addView(imageView);
                    } else {    //텍스트 처리
                        TextView textView = new TextView(this);
                        textView.setTextColor(Color.WHITE);
                        textView.setTextSize(18);
                        textView.setText(text + cursor.getString(i));
                        contentLayout.addView(textView);
                    }

                }
            }

        }
    }
}
