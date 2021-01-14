package com.example.project2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "ProfileActivity";
    private FirebaseUser user;
    private String profilePath;
    private Memberinfo memberInfo;

    private Button inputbtn;
    private String strNickname, strProfile;
    private ImageView Profileimage;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //kakao
        TextView Nickname=findViewById(R.id.Nickname_editText);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");

        Nickname.setText(strNickname);


       // Glide.with(this).load(strProfile).into(Profileimage);

        //입력 버튼
        inputbtn = findViewById(R.id.inputButton);
        inputbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             profileUpdate();
            }
        });

        //누르면 갤러리 이동
        Profileimage = findViewById(R.id.Profileimage);
        Profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    } else {
                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                        startToast("권한을 허용해 주세요");
                    }
                }else{
                    myStartActivity(GalleryActivity.class);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0 : {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    Profileimage.setImageBitmap(bmp);
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("권한을 허용해 주세요");
                }
            }
        }
    }


    private void profileUpdate() {
        final String name = ((EditText) findViewById(R.id.Nickname_editText)).getText().toString();


        if (name.length() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");

            if(profilePath == null){
                Memberinfo memberInfo = new Memberinfo(name);
                uplodar(memberInfo);

            }else{
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                memberInfo = new Memberinfo(name, downloadUri.toString());
                                uplodar(memberInfo);
                            } else {
                                startToast("회원정보를 보내는 것을 실패했습니다.");                                                                    }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("로그", "에러: " + e.toString());
                }
            }

        } else {
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void uplodar (Memberinfo memberInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("회원정보 등록을 성공하였습니다.");
                        myStartActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보 등록에 실패하였습니다.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }



    private void startToast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }
}