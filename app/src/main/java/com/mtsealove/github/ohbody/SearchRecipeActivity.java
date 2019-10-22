package com.mtsealove.github.ohbody;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.HealthFood;
import com.mtsealove.github.ohbody.Database.RecipeDbHelper;
import com.mtsealove.github.ohbody.View.RecipeView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchRecipeActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    TimerTask timerTask;
    Timer timer;
    TextView noResultTv;
    EditText searchEt;
    Button searchBtn;
    ScrollView scrollView;
    LinearLayout contentLayout;
    ArrayList<HealthFood> healthFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        searchEt = findViewById(R.id.searchEt);
        searchBtn = findViewById(R.id.searchBtn);
        scrollView = findViewById(R.id.contentScroll);
        contentLayout = findViewById(R.id.contentLayout);
        noResultTv=findViewById(R.id.noResultTv);

        contentLayout.removeView(noResultTv);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Search();
                        break;
                }
                return false;
            }
        });
    }

    private void Search() {
        if(searchEt.getText().toString().length()==0){
            Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            RecipeDbHelper recipeDbHelper=new RecipeDbHelper(this, RecipeDbHelper.RecipeTable, null, 1);
            SQLiteDatabase database=recipeDbHelper.getReadableDatabase();
            String query="select RCP_NM, RCP_SEQ from "+RecipeDbHelper.RecipeTable+" where RCP_NM like '%"+searchEt.getText().toString()+"%'";
            Cursor cursor=database.rawQuery(query, null);

            contentLayout.removeAllViews();
            if(cursor!=null) {
                if(cursor.getCount()!=0) {
                    while(cursor.moveToNext()){
                        RecipeView recipeView=new RecipeView(this);
                        recipeView.SetFoodView(cursor.getString(0), cursor.getString(1));
                        contentLayout.addView(recipeView);
                    }
                } else{
                    contentLayout.addView(noResultTv);
                }
            }
        }
    }

}
