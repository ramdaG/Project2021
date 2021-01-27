package com.example.project2021.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project2021.GpsTracker;
import com.example.project2021.Login.LoginActivity;
import com.example.project2021.MainActivity;
import com.example.project2021.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.squareup.okhttp.internal.framed.ErrorCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "ProfileActivity";
    private FirebaseUser user;
    private String profilePath;
    private Memberinfo memberInfo;
    private RadioButton hotButton, normalButton, iceButton;

    private GpsTracker gpsTracker;

    private Button inputbtn;
    private String strNickname, strProfile, strAddress, strType;
    private TextView Text_address;
    private EditText Nickname;
    private ImageView Profileimage;
    StringBuilder result_address;
    private static final int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        memberInfo = (Memberinfo)getIntent().getSerializableExtra("memberInfo");

        //kakao
        Nickname=findViewById(R.id.Nickname_editText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //이미 등록된 회원정보 가져오기
        if(user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                String name = document.getString("name");
                                Nickname.setText(name);

                                Profileimage = findViewById(R.id.Profileimage);
                                if (document.getString("photoUrl") != null) {
                                    String profile = document.getString("photoUrl");
                                    Glide.with(Profileimage).load(profile).centerCrop().override(1000).into(Profileimage);
                                    Profileimage.setAdjustViewBounds(true);
                                } else {
                                    Profileimage.setImageResource(R.mipmap.media_avatar);
                                }

                                String type = document.getString("type");
                                switch (type) {
                                    case "더위를 많이 타는":
                                        hotButton.setChecked(true);
                                        break;
                                    case "적당한":
                                        normalButton.setChecked(true);
                                        break;
                                    case "추위를 많이 타는":
                                        iceButton.setChecked(true);
                                        break;
                                }

                            }
                        }
                    }
                }
            });
        }

        //TextView Address=findViewById(R.id.txt_address);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strAddress = intent.getStringExtra("address");
        strType = intent.getStringExtra("type");

        Nickname.setText(strNickname);

        //타입 가져오기
        RadioGroup TypeGroup = findViewById(R.id.radioGroup);
        hotButton = findViewById(R.id.hotButton);
        normalButton = findViewById(R.id.normalButton);
        iceButton = findViewById(R.id.iceButton);
        TypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.hotButton:
                        strType = hotButton.getText().toString();
                        break;
                    case R.id.normalButton:
                        strType = normalButton.getText().toString();
                        break;
                    case R.id.iceButton:
                        strType = iceButton.getText().toString();
                        break;
                }
            }
        });

        //주소 가져오기
        gpsTracker = new GpsTracker(ProfileActivity.this);
        Geocoder geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
        List<Address> addresses = null;
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        try {
            addresses = geocoder.getFromLocation(
                    latitude, // 위도
                    longitude, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류");
        }
        if (addresses != null) {
            if (addresses.size()==0) {
                strAddress = "해당되는 주소 정보는 없습니다";
            } else {
                strAddress = addresses.get(0).getAddressLine(0);
            }
        }
        String [] AddressArray = strAddress.split(" ");
        result_address = new StringBuilder();
        for(int i = 1; i < AddressArray.length - 1; i++){
            result_address.append(AddressArray[i]+" ");
        }

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
                    Glide.with(this)
                            .load(profilePath)
                            .centerCrop()
                            .override(1000)
                            .into(Profileimage);
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
        final String address = result_address.toString();
        final String type = strType;
        //final String id;

        if (name.length() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            user = FirebaseAuth.getInstance().getCurrentUser();
            //final DocumentReference documentReference = memberInfo == null ? firebaseFirestore.collection("users").document() : firebaseFirestore.collection("users").document(memberInfo.getName());

            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");

            if(profilePath == null){
                Memberinfo memberInfo = new Memberinfo(name, address, type, user.toString());
                uploader(memberInfo);

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
                                memberInfo = new Memberinfo(name, downloadUri.toString(), address, type, user.toString());
                                uploader(memberInfo);
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


    private void uploader (Memberinfo memberInfo) {
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