package com.example.project2021.board;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2021.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class boardFragment extends Fragment {

    RecyclerView recyclerView = null;
    RecyclerAdapter_Post mAdapter = null;
    ArrayList<Post_item> mList;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board,container,false);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mList = new ArrayList<Post_item>();
        mList.add(new Post_item(R.id.img_profile, "이유주", "인천 간석동", "추운데 코트 입어도 되나요?"));
        mList.add(new Post_item(R.id.img_profile, "ㅎㅇ", "인천 부평2동", "오후에도 눈 내릴까요 차 막히면 버스 못 타는데"));
        mList.add(new Post_item(R.id.img_profile, "부산여자", "부산 수정동", "부산은 그렇게 많이 안 추워요!!"));
        mList.add(new Post_item(R.id.img_profile, "이유주", "인천 간석동", "추운데 코트 입어도 되나요?"));
        mList.add(new Post_item(R.id.img_profile, "ㅎㅇ", "인천 부평2동", "오후에도 눈 내릴까요 차 막히면 버스 못 타는데"));
        mList.add(new Post_item(R.id.img_profile, "부산여자", "부산 수정동", "부산은 그렇게 많이 안 추워요!!"));
        mList.add(new Post_item(R.id.img_profile, "이유주", "인천 간석동", "추운데 코트 입어도 되나요?"));
        mList.add(new Post_item(R.id.img_profile, "ㅎㅇ", "인천 부평2동", "오후에도 눈 내릴까요 차 막히면 버스 못 타는데"));
        mList.add(new Post_item(R.id.img_profile, "부산여자", "부산 수정동", "부산은 그렇게 많이 안 추워요!!"));

        mAdapter=new RecyclerAdapter_Post(mList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        //게시판 글쓰기 부분
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabboard);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), boardActivity.class));
            }
        });

        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}