package com.mtsealove.github.ohbody;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.HealthFood;
import com.mtsealove.github.ohbody.Database.HealthFoodDB;
import com.mtsealove.github.ohbody.View.HealthFoodView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchHealthFoodActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_search_health_food);
        searchEt = findViewById(R.id.searchEt);
        searchBtn = findViewById(R.id.searchBtn);
        scrollView = findViewById(R.id.contentScroll);
        contentLayout = findViewById(R.id.contentLayout);
        noResultTv=findViewById(R.id.noResultTv);

        contentLayout.removeView(noResultTv);

        StartCheckData();
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

    private void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("로딩");
        progressDialog.setMessage("데이터를 로딩중입니다");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //데이터 로딩이 안됬으면 다이얼로그 출력
    private void StartCheckData() {
        if (!HealthFoodDB.Loaded) {
            setProgressDialog();
            //계속 체크
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (HealthFoodDB.Loaded) {
                        progressDialog.dismiss();
                        timer.cancel();
                    }
                }
            };
            //타이머 수행
            timer = new Timer();
            timer.schedule(timerTask, 0, 1000);
        }
    }

    //검색
    private void Search() {
        if (searchEt.getText().toString().length() == 0) {
            Toast.makeText(this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
        } else {
            String word = searchEt.getText().toString();
            //화면 표시 모두 제거
            contentLayout.removeAllViews();
            healthFoods = getHealthFood(word);
            if(healthFoods.size()==0) {//검색 결과가 없을 경우
                contentLayout.addView(noResultTv);
            }else {
                for(final HealthFood healthFood: healthFoods) {
                    //레이아웃에 뷰 추가
                    HealthFoodView healthFoodView=new HealthFoodView(this);
                    healthFoodView.SetContent(healthFood.getAPLC_RAWMTRL_NM(), healthFood.getFNCLTY_CN());
                    contentLayout.addView(healthFoodView);
                    //클릭 리스너 추가
                    healthFoodView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(SearchHealthFoodActivity.this, HealthFoodDetailActivity.class);
                            intent.putExtra("HealthFood", healthFood);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    //일치하는 데이터 리스트 반환
    private ArrayList<HealthFood> getHealthFood(String word) {
        ArrayList<HealthFood> healthFoods = new ArrayList<>();
        //데이터 검색
        for (HealthFood healthFood : HealthFoodDB.healthFoods) {
            //신청원료 또는 효능이 일치할 경우
            if (healthFood.getAPLC_RAWMTRL_NM().contains(word)
                    || healthFood.getFNCLTY_CN().contains(word)) {
                healthFoods.add(healthFood);
            }
        }
        return healthFoods;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
