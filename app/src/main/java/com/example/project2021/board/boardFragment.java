package com.example.project2021.board;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class boardFragment extends Fragment {
    private static final String TAG = "boardFragment";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private View view;
    private RecyclerView recyclerView;

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
    public void onResume() {
        super.onResume();

        if (firebaseUser != null) {
            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference
                    .orderBy("createdAt", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<PostInfo> postList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new PostInfo(
                                            document.getData().get("contents").toString(),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime())));
                                }
                                recyclerView = view.findViewById(R.id.recyclerView);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                RecyclerView.Adapter mAdapter = new PostAdapter(boardFragment.this, postList);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board,container,false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //게시판 글쓰기 부분
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
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
//        mAdapter.setOnItemLongClicklistener(new RecyclerAdapter_Post.OnItemLongClickListener() {
  //          @Override
  //          public void onItemLongClick(View v, int pos) {
  //              Toast.makeText(getContext(), "아이템선택"+pos, Toast.LENGTH_SHORT).show();
   //         }
 //       });

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