package com.example.project2021.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.home.Comment_item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class commentFragment extends Fragment {

    RecyclerView recyclerView = null;
    RecyclerAdapter_Post_Comm mAdapter = null;
    ArrayList<Post_Comm_item> mList;
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
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        mList = new ArrayList<Post_Comm_item>();
        for(int i = 0; i < 10; i++){
            mList.add(new Post_Comm_item(R.id.img_type, "박소현","오늘너무추워요가나다라마바사아", currentTime));
        }

        mAdapter = new RecyclerAdapter_Post_Comm(mList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

}
