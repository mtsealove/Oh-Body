package com.mtsealove.github.ohbody.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mtsealove.github.ohbody.R;
import com.mtsealove.github.ohbody.SearchFoodActivity;

public class FoodSelectView extends RelativeLayout {
    Context context;
    TextView foodTv, countTv;
    ImageView lower, higher, remove;
    int amount = 1, amountT = 1;
    String amountType="g";
    String foodName;
    int food_cd, serving_wt;

    public FoodSelectView(Context context, String name, int amount,  int food_cd) {
        super(context);
        init(context);
        this.amountType = amountType;
        this.amount = amount;
        foodName = name;
        foodTv.setText(name);
        countTv.setText(amount + amountType);
        this.food_cd=food_cd;
    }

    public FoodSelectView(Context context) {
        super(context);
        init(context);
    }

    public FoodSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FoodSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FoodSelectView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_food_select, FoodSelectView.this, false);
        addView(layout);
        foodTv = layout.findViewById(R.id.foodTv);
        countTv = layout.findViewById(R.id.countTv);
        lower = layout.findViewById(R.id.lower);
        higher = layout.findViewById(R.id.higher);
        remove = layout.findViewById(R.id.removeBtn);
        SetAmountT();

        lower.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AmountLower();
            }
        });
        higher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AmountHigher();
            }
        });
        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveThis();
            }
        });
    }

    private void SetAmountT() {
        int amountTmp = amount, count = 0;
        while (amountTmp > 0) {
            Log.e("amount", String.valueOf(amountTmp));
            amountTmp /= 10;
            count++;
        }
        for (int i = 0; i < count; i++)
            amountT *= 10;
    }

    //양을 줄이거나 늘리기
    private void AmountLower() {
        amount -= amountT;
        countTv.setText(amount + amountType);
        if (amount <= 0) {  //0개에 수렴하면
            RemoveThis();
        }
    }

    private void AmountHigher() {
        amount += amountT;
        countTv.setText(amount + amountType);
    }

    private void RemoveThis() {
        SearchFoodActivity.foodSelectViews.remove(this);
        SearchFoodActivity.foodsLayout.removeView(this);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFood_cd() {
        return food_cd;
    }

    public void setFood_cd(int food_cd) {
        this.food_cd = food_cd;
    }
}
