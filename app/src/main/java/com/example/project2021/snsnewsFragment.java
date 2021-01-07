package com.example.project2021;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class snsnewsFragment extends Fragment {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;


    public snsnewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_snsnews, container, false);

//        tabLayout = v.findViewById(R.id.tab_layout);
//       // appBarLayout = v.findViewById(R.id.tablayout_id);
//        viewPager = v.findViewById(R.id.view_pager);
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
//
//        //adding fragments
//        adapter.(new snsFragment(), "SNS");
//        adapter.AddFragment(new newsFragment(), "News");
//
//
//        //adapter set up
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);

        // Inflate the layout for this fragment
        return v;
    }}