package com.example.project2021.home.charts;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.home.HomeCommentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomDialog_Incheon extends Dialog {

    private EditText et_text;
    private Context mContext;

    RadioGroup radioGroup,radioGroupLike;
    //RadioButton rb_Coat,rb_Long,rb_Short;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseFirestore mFirestore;
    Date date;
    RecyclerView recyclerView;


    String uid;
    String mCoat = "Coat",mLong = "Long",mShort = "Short", mCold = "Cold", mGood="Good", mHot="Hot";
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
        radioGroupLike = findViewById(R.id.rGroup2);
        recyclerView = findViewById(R.id.user_recyclerView);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirestore = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, et_text.getText().toString(), Toast.LENGTH_SHORT).show();
                String testTxt = et_text.getText().toString();

                if(testTxt.getBytes().length <= 0){
                    Toast.makeText(getContext(), "코멘트를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {

                Map<String, Object> testMap = new HashMap<>();
                testMap.put("content", testTxt );
                testMap.put("user",uid);
                testMap.put("date", new Timestamp(new Date()));


                mFirestore.collection("comments_Incheon").add(testMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(),"added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String error = e.getMessage();
                        Toast.makeText(getContext(),"Error : "+error,Toast.LENGTH_SHORT).show();
                    }
                });

                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_Coat:System.out.println("a");
                        myRef.child("Charts").child("incheon").child(mCoat).child(uid).setValue("");
                        break;
                    case R.id.rb_Long:System.out.println("b");
                        myRef.child("Charts").child("incheon").child(mLong).child(uid).setValue("");
                        break;
                    case R.id.rb_Short:System.out.println("c");
                        myRef.child("Charts").child("incheon").child(mShort).child(uid).setValue("");
                        break;
                }

                switch (radioGroupLike.getCheckedRadioButtonId()){
                    case R.id.rb_Cold:
                        myRef.child("Select").child("incheon").child(mCold).child(uid).setValue("");
                        break;
                    case R.id.rb_Good:
                        myRef.child("Select").child("incheon").child(mGood).child(uid).setValue("");
                        break;
                    case R.id.rb_Hot:
                        myRef.child("Select").child("incheon").child(mHot).child(uid).setValue("");
                        break;
                }

                }

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

    public CustomDialog_Incheon(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }
}