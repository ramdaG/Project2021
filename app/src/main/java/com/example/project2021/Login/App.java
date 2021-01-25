package com.example.project2021.Login;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

public class App extends Application {
    private static App self;
    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        FirebaseApp.initializeApp(this);
        KakaoSDK.init(new KakaoAdapter() {
            @Override
            public IApplicationConfig getApplicationConfig() {
                return new IApplicationConfig() {
                    @Override
                    public Context getApplicationContext() {
                        return self;
                    }
                };
            }
        });

    }
}