package com.mtsealove.github.ohbody.View;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.mtsealove.github.ohbody.R;
import com.mtsealove.github.ohbody.RecipeActivity;

public class RecipeView extends LinearLayout {
    TextView foodNmTv;
    String food_cd;
    Context context;
    LinearLayout background;

    public RecipeView(Context context) {
        super(context);
        init(context);
    }

    public RecipeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecipeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RecipeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_recipe, RecipeView.this, false);
        addView(layout);
        foodNmTv = layout.findViewById(R.id.foodNmTv);
        background=layout.findViewById(R.id.background);

        background.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, RecipeActivity.class);
                intent.putExtra("food_cd", food_cd);
                context.startActivity(intent);
            }
        });
    }

    public void SetFoodView(String foodNm, String food_cd) {
        foodNmTv.setText(foodNm);
        this.food_cd = food_cd;
    }

    public String getFood_cd() {
        return food_cd;
    }

    public void setFood_cd(String food_cd) {
        this.food_cd = food_cd;
    }
}
