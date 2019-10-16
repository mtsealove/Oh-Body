package com.mtsealove.github.ohbody;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.mtsealove.github.ohbody.Database.PersonalDbHelper;

public class InformationActivity extends AppCompatActivity {
    EditText HeightEt, WeightEt, AgeEt, DiseaseEt;
    Button GenderBtn, ConfirmBtn;
    TextView titleTv;
    private float Height, Weight;
    private int Age;
    private char Gender = 'e';
    private String Disease;
    PersonalDbHelper personalDbHelper;
    SQLiteDatabase database;
    private Cursor cursor;
    boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        HeightEt = findViewById(R.id.heightEt);
        WeightEt = findViewById(R.id.weightEt);
        AgeEt = findViewById(R.id.ageEt);
        DiseaseEt = findViewById(R.id.diseaseEt);
        GenderBtn = findViewById(R.id.genderBtn);
        ConfirmBtn = findViewById(R.id.confirmBtn);
        titleTv=findViewById(R.id.titleTv);

        GenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetGender();
            }
        });
        ConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInput();
            }
        });

        personalDbHelper = new PersonalDbHelper(this, PersonalDbHelper.BodyTable, null, 1);
        ReadData();
    }

    //성별 선택 메서드
    private void SetGender() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_trans, null);
        Button btnM = view.findViewById(R.id.btnM);
        Button btnF = view.findViewById(R.id.btnF);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = 'F';
                GenderBtn.setText("여성");
                dialog.cancel();
            }
        });
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gender = 'M';
                GenderBtn.setText("남성");
                dialog.cancel();
            }
        });
    }

    //입력 확인
    private void CheckInput() {
        String height = HeightEt.getText().toString();
        String weight = WeightEt.getText().toString();
        String age = AgeEt.getText().toString();
        //모두 길이가 정상적으로 입력되었는지 확인
        if (height.length() == 0) {
            Toast.makeText(this, "키를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (weight.length() == 0) {
            Toast.makeText(this, "몸무게를 입력학세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (age.length() == 0) {
            Toast.makeText(this, "나이를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        } else if (Gender == 'e') {
            Toast.makeText(this, "성별을 선택하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        WriteData(height, weight, age);
    }

    //데이터 작성
    private void WriteData(String height, String weight, String age) {
        Height = Float.parseFloat(height);
        Weight = Float.parseFloat(weight);
        Age = Integer.parseInt(age);
        Disease = DiseaseEt.getText().toString();
        database = personalDbHelper.getWritableDatabase();
        //처음이면 데이터 생성
        if(first){
            personalDbHelper.InsertBodyData(database, Height, Weight, Age, Gender, Disease);
            Toast.makeText(this, "정보가 저장되었습니다", Toast.LENGTH_SHORT).show();
        }

        //아니면 업데이트
        else{
            personalDbHelper.UpdateBodyData(database, Height, Weight, Age, Gender, Disease);
            Toast.makeText(this, "정보가 변경되었습니다", Toast.LENGTH_SHORT).show();
        }
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //데이터가 있다면 읽어오기
    private void ReadData() {
        database= personalDbHelper.getReadableDatabase();
        String Query="select * from "+ PersonalDbHelper.BodyTable;
        cursor=database.rawQuery(Query, null);
        if(cursor!=null) {
            if (cursor.getCount() != 0) {
                titleTv.setText("신체정보 수정");
                first=false;
                cursor.moveToNext();
                Height = cursor.getFloat(0);
                Weight = cursor.getFloat(1);
                Age = cursor.getInt(2);
                Gender = cursor.getString(3).charAt(0);
                Disease = cursor.getString(4);
                HeightEt.setText(Height + "");
                WeightEt.setText(Weight + "");
                AgeEt.setText(Age + "");
                String g;
                if (Gender == 'M') g = "남성";
                else g = "여성";
                GenderBtn.setText(g);
                DiseaseEt.setText(Disease);
            } else {
                first=true;
            }
        }
    }
}
