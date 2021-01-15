package com.example.project2021.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.profile.ProfileActivity;
import com.example.project2021.R;
import com.example.project2021.board.Post_item;
import com.example.project2021.board.RecyclerAdapter_Post;

import java.util.ArrayList;

public class userFragment extends Fragment {

    RecyclerView mRecyclerView = null ;
    RecyclerAdapter_Post mAdapter = null ;
    ArrayList<Post_item> mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton profile_edit;
    ImageButton img_heart;

    public static userFragment newInstance(String param1, String param2) {
        userFragment fragment = new userFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView= getActivity().findViewById(R.id.recycler);
        RecyclerAdapter_Post adapter = new RecyclerAdapter_Post(mList);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerAdapter_Post(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemLongClicklistener(new RecyclerAdapter_Post.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                Toast.makeText(getContext(), "아이템선택"+pos, Toast.LENGTH_SHORT).show();
            }
        });


        //프로필 수정
        profile_edit = view.findViewById(R.id.profile_edit);
        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initDataset() {
        mList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            mList.add(new Post_item(R.id.img_profile, "박소현","인천 가좌동","안녕하세요"));
        }
    }
}