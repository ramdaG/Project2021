package com.example.project2021;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class snsFragment extends Fragment {
    View view;
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
        return view;
    }
}