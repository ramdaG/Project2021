package com.example.project2021;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class newsFragment extends Fragment {
    View view;
    public newsFragment() {
        // Required empty public constructor
    }


    public static newsFragment newInstance() {
        newsFragment tab2 = new newsFragment();
        return tab2;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        return view;
    }

}