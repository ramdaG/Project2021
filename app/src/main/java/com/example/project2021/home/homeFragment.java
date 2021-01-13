package com.example.project2021.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project2021.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.github.mikephil.charting.animation.Easing.*;

public class homeFragment extends Fragment {
    Context ct;
    RecyclerView mRecyclerView = null ;
    RecyclerAdapter_Comment mAdapter = null ;
    ArrayList<Comment_item> mList;
    private RecyclerView.LayoutManager mLayoutManager;

    PieChart pieChart;
    int[] color = new int[]{ R.color.blue_1,
            R.color.blue_2,R.color.blue_3};

    TextView vote;
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView= getActivity().findViewById(R.id.recycler);
        RecyclerAdapter_Comment adapter = new RecyclerAdapter_Comment(mList);
        mRecyclerView.setAdapter(adapter);

        vote = getActivity().findViewById(R.id.txt_vote);
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dlg = new CustomDialog(ct);
                dlg.show();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerAdapter_Comment(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        //piechart
        pieChart = view.findViewById(R.id.pieChart);

        PieDataSet pieDataSet = new PieDataSet(data1(),"chart");
        pieDataSet.setColors(color,getActivity());
        pieDataSet.setSliceSpace(3);
        //pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setUsePercentValues(true);
        pieData.setValueTextSize(10);
        pieChart.setEntryLabelTextSize(12);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(0);
        pieChart.animateY(1000, EaseInOutCubic);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initDataset() {
        String currentTime = new SimpleDateFormat("HH : mm", Locale.getDefault()).format(new Date());
        mList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            mList.add(new Comment_item(R.id.img_type, "박소현","오늘 너무 추워요", currentTime));
        }
    }

    private ArrayList<PieEntry> data1(){
        ArrayList<PieEntry> datavalue = new ArrayList<>();

        datavalue.add(new PieEntry(30,"롱패딩"));
        datavalue.add(new PieEntry(50,"숏패딩"));
        datavalue.add(new PieEntry(20,"코트"));

        return datavalue;
    }
}
