package com.example.project2021.board;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.project2021.R;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class boardActivity extends AppCompatActivity {
    private static final String TAG = "boardActivity";
    FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private ArrayList<PostInfo> mDataset = new ArrayList<>();
    private LinearLayout parent;
    private int pathCount, successCount;
    private PostInfo postInfo;
    private EditText postEditText;
    private FirebaseFirestore firebaseFirestore;
    String getContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        findViewById(R.id.check).setOnClickListener(onClickListener);
        postEditText = findViewById(R.id.boardEditText);

        postInfo = (PostInfo)getIntent().getSerializableExtra("postInfo");

        getContents = getIntent().getStringExtra("contents");

        if (getContents != null) {
            TextView preContents = findViewById(R.id.boardEditText);
            preContents.setText(getContents);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    storageUpload();
                    break;
            }
        }
    };

    private void storageUpload() {
        final String contents = ((EditText) findViewById(R.id.boardEditText)).getText().toString();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (getContents != null) {
            String id = getIntent().getStringExtra("id");
            Log.d(TAG, "id : " + id);
            String updateContents = ((EditText) findViewById(R.id.boardEditText)).getText().toString();
            firebaseFirestore.collection("posts").document(id).update("contents", updateContents);
            finish();
        } else {
                if (contents.length() > 0) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    //FirebaseStorage storage = FirebaseStorage.getInstance();
                    //StorageReference storageRef = storage.getReference();

                    final DocumentReference documentReference = postInfo == null ? firebaseFirestore.collection("posts").document() : firebaseFirestore.collection("posts").document(postInfo.getId());
                    final Date date = postInfo == null ? new Date() : postInfo.getCreatedAt();

                    if (pathList.size() == 0) {
                        storeUpload(documentReference, new PostInfo(contents, user.getUid(), date));
                    }
                } else {
                    startToast("글 내용을 입력해 주세요.");
                }
            }
        }


    private void storeUpload(DocumentReference documentReference, PostInfo postInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").add(postInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        startToast("게시글이 등록되었습니다.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        startToast("게시글 등록에 실패했습니다.");
                    }
                });

    }

    private void startToast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c, String media, int requestCode) {
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent, 0);
    }
}

