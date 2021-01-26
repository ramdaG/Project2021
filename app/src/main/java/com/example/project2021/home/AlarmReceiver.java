package com.example.project2021.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AlarmReceiver extends BroadcastReceiver {
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    public void onReceive(Context context, Intent intent) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child("Chart").removeValue();
        System.out.println("실행확인");
    }
}
