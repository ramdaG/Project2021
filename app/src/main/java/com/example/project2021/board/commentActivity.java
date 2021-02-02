package com.example.project2021.board;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project2021.R;

import java.util.ArrayList;

public class commentActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        String getContent = getIntent().getStringExtra("contents");
        String getId = getIntent().getStringExtra("id");
        String getPublisher = getIntent().getStringExtra("publisher");
        String getName = getIntent().getStringExtra("name");
        String getAddress = getIntent().getStringExtra("address");
        String getType = getIntent().getStringExtra("type");
        String getPhotoUrl = getIntent().getStringExtra("photoUrl");
        int getLikeCount = getIntent().getIntExtra("likeCount", 0);
        int getCommCount = getIntent().getIntExtra("commentCount", 0);
        Boolean setUserLiked = getIntent().getBooleanExtra("likecheck", true);
        String getLikeId = getIntent().getStringExtra("likeId");


        Log.d("commentActivity","getContents : " + getContent);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment2, commentFragment.newInstance(getContent, getId, getPublisher, getName, getAddress, getType, getPhotoUrl, getLikeCount, getCommCount, setUserLiked, getLikeId));
        transaction.commit();
    }
}
