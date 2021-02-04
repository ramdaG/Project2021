package com.example.project2021.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2021.MainActivity;
import com.example.project2021.R;

public class IntroActivity extends AppCompatActivity {
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        imageView = findViewById(R.id.imageView_intro);
        TextView dup = findViewById(R.id.txt_dup);
        TextView ggong = findViewById(R.id.txt_ggong);
        TextView and = findViewById(R.id.txt_and);

        Animation ani1 = AnimationUtils.loadAnimation(this, R.anim.logo_dup);
        dup.startAnimation(ani1);
        Animation ani2 = AnimationUtils.loadAnimation(this, R.anim.logo_and);
        and.startAnimation(ani2);
        Animation ani3 = AnimationUtils.loadAnimation(this, R.anim.logo_ggong);
        ggong.startAnimation(ani3);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}