package com.example.project2021.home.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project2021.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class CustomDialog_Jeju extends Dialog {

    private EditText et_text;
    private Context mContext;

    RadioGroup radioGroup;
    //RadioButton rb_Coat,rb_Long,rb_Short;
    FirebaseDatabase database;
    DatabaseReference myRef;

    String uid;
    String mCoat = "Coat",mLong = "Long",mShort = "Short";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customdialog);

        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        et_text = findViewById(R.id.put_text); //comment

        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        radioGroup = findViewById(R.id.rGroup);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, et_text.getText().toString(), Toast.LENGTH_SHORT).show();

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_Coat:System.out.println("a");
                        myRef.child("Charts").child("Jeju").child(mCoat).child(uid).setValue("");
                        break;
                    case R.id.rb_Long:System.out.println("b");
                        myRef.child("Charts").child("Jeju").child(mLong).child(uid).setValue("");
                        break;
                    case R.id.rb_Short:System.out.println("c");
                        myRef.child("Charts").child("Jeju").child(mShort).child(uid).setValue("");
                        break;
                }


                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "취소하셨습니다", Toast.LENGTH_SHORT).show();
                cancel();
            }
        });

    }

    public CustomDialog_Jeju(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
}