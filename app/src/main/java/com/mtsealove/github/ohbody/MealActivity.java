package com.mtsealove.github.ohbody;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MealActivity extends AppCompatActivity {
    TextView dateTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        dateTV=findViewById(R.id.dateTV);
        initDate();
    }

    private void initDate() {
        //String[] weeks={"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM월 dd일 EE요일", Locale.KOREAN);

        dateTV.setText(dateFormat.format(date));
    }
}
