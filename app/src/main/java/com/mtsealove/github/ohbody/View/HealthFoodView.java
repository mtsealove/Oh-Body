package com.mtsealove.github.ohbody.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.mtsealove.github.ohbody.R;

//검색결과
public class HealthFoodView extends LinearLayout {
    TextView foodNmTv, effectTv;
    Context context;

    public HealthFoodView(Context context) {
        super(context);
        init(context);
    }

    public HealthFoodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HealthFoodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HealthFoodView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_health_food, HealthFoodView.this, false);
        addView(layout);
        foodNmTv = layout.findViewById(R.id.foodNmTv);
        effectTv = layout.findViewById(R.id.effectTv);
    }

    public void SetContent(String foodNm, String effect) {
        foodNmTv.setText(foodNm);
        effectTv.setText(effect);
    }
}
