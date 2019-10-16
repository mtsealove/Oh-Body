package com.mtsealove.github.ohbody;

import android.content.Intent;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class MainActivity extends AppCompatActivity {
    Button menuBtn, mealBtn;
    DrawerLayout drawerLayout;
    TextView shortTV, longTV;
    SlidingUpPanelLayout slidingUpPanelLayout;
    LinearLayout informationLayout, graphLayout, mealLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuBtn = findViewById(R.id.menuBtn);
        mealBtn = findViewById(R.id.mealBtn);
        drawerLayout = findViewById(R.id.drawerLayout);
        shortTV = findViewById(R.id.shortTV);
        longTV = findViewById(R.id.longTV);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        informationLayout=findViewById(R.id.informationLayout);
        graphLayout=findViewById(R.id.graphLayout);
        mealLayout=findViewById(R.id.mealLayout);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDrawer();
            }
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelStateChanged(View view, SlidingUpPanelLayout.PanelState panelState, SlidingUpPanelLayout.PanelState panelState1) {
                //확장되었을 때
                if (panelState1.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    shortTV.setVisibility(View.GONE);
                    longTV.setVisibility(View.VISIBLE);
                } else if (panelState1.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    shortTV.setVisibility(View.VISIBLE);
                    longTV.setVisibility(View.GONE);
                }
            }
        });

        mealBtn.setOnClickListener(MealListener);
        informationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, InformationActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });
        mealLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, MealActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });
        graphLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, GraphActivity.class);
                startActivity(intent);
                CloseDrawer();
            }
        });
    }

    //메뉴 버튼으로 메뉴 열기
    private void OpenDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }
    //메뉴 닫아주기
    private void CloseDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        //슬라이드가 올려져있으면 닫기
        else if(slidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {    //2번 눌러 종료
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPressedTime;

            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
            {
                super.onBackPressed();
            }
            else
            {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
