package com.mtsealove.github.ohbody.View;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mtsealove.github.ohbody.MealActivity;
import com.mtsealove.github.ohbody.R;
import com.mtsealove.github.ohbody.SearchFoodActivity;

public class MealView extends LinearLayout {
    Context context;
    TextView MealTypeTv, MealContentTv;
    public final static int Breakfast = 0, Lunch = 1, Dinner = 2;
    private int MealType;

    public MealView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MealView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MealView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public MealView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_meal, MealView.this, false);
        MealTypeTv = layout.findViewById(R.id.catTV);
        MealContentTv = layout.findViewById(R.id.contentTV);
        addView(layout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
    }

    //식사 종류 설정
    public void setMealType(int Type) {
        switch (Type) {
            case Breakfast:
                MealTypeTv.setText("아침 식사");
                break;
            case Lunch:
                MealTypeTv.setText("점심 식사");
                break;
            case Dinner:
                MealTypeTv.setText("저녁 식사");
                break;
        }
        MealType = Type;
    }

    //식사 내용 설정
    public void setContent(String content) {
        MealContentTv.setText(content);
    }

    //메뉴 검색 화면으로 이동
    public static final int RequestBreakfast=200, RequestLunch=300, RequestDinner=400;
    private void Search() {
        Intent intent=new Intent(context, SearchFoodActivity.class);
        switch (MealType) {
            case Breakfast:
                intent.putExtra("Meal", RequestBreakfast);
                ((MealActivity)context).startActivityForResult(intent, RequestBreakfast);
                break;
            case Lunch:
                intent.putExtra("Meal", RequestLunch);
                ((MealActivity)context).startActivityForResult(intent, RequestLunch);
                break;
            case Dinner:
                intent.putExtra("Meal", RequestDinner);
                ((MealActivity)context).startActivityForResult(intent, RequestDinner);
                break;
        }

    }
}
