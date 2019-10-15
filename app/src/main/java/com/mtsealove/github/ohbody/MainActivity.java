package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuBtn=findViewById(R.id.menuBtn);
        mealBtn=findViewById(R.id.mealBtn);
        drawerLayout=findViewById(R.id.drawerLayout);
        shortTV=findViewById(R.id.shortTV);
        longTV=findViewById(R.id.longTV);
        slidingUpPanelLayout=findViewById(R.id.sliding_layout);

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
                if(panelState1.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    shortTV.setVisibility(View.GONE);
                    longTV.setVisibility(View.VISIBLE);
                } else if(panelState1.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    shortTV.setVisibility(View.VISIBLE);
                    longTV.setVisibility(View.GONE);
                }
            }
        });

        mealBtn.setOnClickListener(MealListener);
    }

    //메뉴 버튼으로 메뉴 열기
    private void OpenDrawer() {
        if(!drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.openDrawer(GravityCompat.START);
    }

    private View.OnClickListener MealListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this, MealActivity.class);
            startActivity(intent);
        }
    };
}
