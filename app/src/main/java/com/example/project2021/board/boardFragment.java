package com.example.project2021.board;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2021.R;
import com.example.project2021.snsnews.ViewpagerAdapter;
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
        mList.add(new Post_item(R.id.img_profile, "이유주", "인천 간석동", "추운데 코트 입어도 되나요? 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 가나다라마바사아자차카타파하 "));
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


        //검색
        TextView searchText = view.findViewById(R.id.searchEditText);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.getText();

            }
        });

        //롱클릭이벤트로 게시글 수정/삭제
        mAdapter.setOnItemLongClicklistener(new RecyclerAdapter_Post.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int pos) {
                Toast.makeText(getContext(), "아이템선택"+pos, Toast.LENGTH_SHORT).show();
            }
        });

/*
        mAdapter.setOnItemLongClickListener(new RecyclerAdapter_Post.ViewHolder.OnItemLongClickListener() {
                    String[] params = {mTime};
                    Cursor cursor = (Cursor) dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, "date=?", params, null, null, null);
                cursor.moveToPosition(pos);
                    final int id = cursor.getInt(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry._ID));

                builder.setTitle("일정 삭제");
                builder.setMessage("일정을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            int deletedCount = db.delete(MemoContract.MemoEntry.TABLE_NAME,MemoContract.MemoEntry._ID+"="+id,null);

                            if(deletedCount==0)
                            {
                                Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                getMemoCursor();
                                Toast.makeText(getActivity(), "일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });


*/
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}