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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2021.OnPostListener;
import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.example.project2021.snsnews.ViewpagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private PostAdapter postAdapter;
    private ArrayList<PostInfo> postList;

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
        view = inflater.inflate(R.layout.fragment_board, container, false);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        PostUpdate();

        /*
            CollectionReference collectionReference_users = firebaseFirestore.collection("users");
            collectionReference_users.orderBy("name", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               ArrayList<Memberinfo> memberinfos = new ArrayList<>();
                               String address = null, name = null, type = null, profile = null;
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   Log.d(TAG, document.getId() + " => " + document.getData());

                                   memberinfos.add(new Memberinfo(
                                           name = document.getData().get("name").toString(),
                                           //profile = document.getData().get("profile").toString(),
                                           address = document.getData().get("address").toString(),
                                           type = document.getData().get("type").toString()));
                               }
                               TextView Text_name = view.findViewById(R.id.txt_name);
                               Text_name.setText(name);
                               TextView Text_address = view.findViewById(R.id.txt_address);
                               Text_address.setText(address);
                               ImageView img_type = view.findViewById(R.id.img_type);
                               switch (type) {
                                   case "더위를 많이 타는":
                                       img_type.setImageResource(R.mipmap.fire_icon);
                                       break;
                                   case "적당한":
                                       img_type.setImageResource(R.mipmap.water_icon);
                                       break;
                                   case "추위를 많이 타는":
                                       img_type.setImageResource(R.mipmap.ice_icon);
                                       break;
                               }
                           }
                       }
                   });
            */

    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(String id) {
            firebaseFirestore.collection("posts")
                    .document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "게시글을 삭제했습니다", Toast.LENGTH_LONG).show();
                            PostUpdate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "게시글 삭제에 실패했습니다", Toast.LENGTH_LONG).show();
                        }
                    });

        }

        @Override
        public void onModify(String id){
            myStartActivity(boardActivity.class, id);
        }
    };


    //실시간 업데이트
    public void PostUpdate() {
        if (firebaseUser != null) {
            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference
                    .orderBy("createdAt", Query.Direction.DESCENDING).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                postList = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new PostInfo(
                                            document.getData().get("contents").toString(),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),
                                            document.getId()));
                                }
                                recyclerView = view.findViewById(R.id.recyclerView);

                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                PostAdapter postAdapter = new PostAdapter(boardFragment.this, postList);
                                recyclerView.setAdapter(postAdapter);

                                postAdapter.notifyDataSetChanged();

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void myStartActivity(Class c, String id) {
        Intent intent = new Intent(getContext(), c);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}