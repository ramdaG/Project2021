package com.example.project2021.snsnews;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.project2021.R;


public class snsFragment extends Fragment {
    WebView wv;
    View view;
    //String url = "https://twitter.com/search?q=%EC%98%A4%EB%8A%98%20%EB%82%A0%EC%94%A8&src=typed_query&f=live";

    public snsFragment() {
        // Required empty public constructor
    }


    public static snsFragment newInstance() {
        snsFragment tab1 = new snsFragment();
        return tab1;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sns, container, false);

        wv = view.findViewById(R.id.sns_wv);
        wv.setWebViewClient(new WebViewClient(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());

                return true;
            }
        });
        //WebSettings ws = wv.getSettings();
        //ws.setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.loadUrl("https://twitter.com/search?q=%EC%98%A4%EB%8A%98%20%EB%82%A0%EC%94%A8&src=typed_query&f=live");

        return view;
    }
}