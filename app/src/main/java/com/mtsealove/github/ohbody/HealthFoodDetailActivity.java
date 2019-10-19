package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.HealthFood;

public class HealthFoodDetailActivity extends AppCompatActivity {
    HealthFood healthFood;
    TextView foodNmTv, contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_food_detail);
        foodNmTv = findViewById(R.id.foodNmTv);
        contentTv = findViewById(R.id.contentTv);

        //이전 페이지에서 직렬화된 데이터로 받아옴
        healthFood = (HealthFood) getIntent().getSerializableExtra("HealthFood");
        Log.e("HealthFood", healthFood.toString());

        //식품명 표시
        foodNmTv.setText(healthFood.getAPLC_RAWMTRL_NM());

        //화면에 표시할 데이터
        String content="";
        content+="제조사: "+healthFood.getBSSH_NM()+"\n\n";
        content+="효능:\n"+healthFood.getFNCLTY_CN()+"\n\n";
        content+="1일 섭취량:\n"+healthFood.getDAY_INTK_CN();
        if(healthFood.getIFTKN_ATNT_MATR_CN().length()!=0&&(!healthFood.getIFTKN_ATNT_MATR_CN().equals("-")))
            content+="\n\n주의사항:\n"+healthFood.getIFTKN_ATNT_MATR_CN();

        contentTv.setText(content);
    }
}
