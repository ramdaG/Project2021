package com.example.project2021.snsnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.project2021.R;

public class NewsItemActivity extends AppCompatActivity {

    WebView wv;

    TextView txTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_item);

        Intent intent = getIntent();
        String link = intent.getStringExtra("Link");

        wv = findViewById(R.id.news_wv);

        wv.getSettings().setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient());  //내 webView에 페이지 보이게

        wv.setWebChromeClient(new WebChromeClient()); //웹 페이지안에 웹다이얼로그

        wv.loadUrl(link);

    }
}