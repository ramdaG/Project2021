package com.example.project2021.board;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    private ArrayList<PostInfo> arrayList;
    private ArrayList<Memberinfo> memberList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Memberinfo memberinfo;


    public static boardFragment newInstance() {
        boardFragment boardfrgment = new boardFragment();
        return boardfrgment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // recyclerView.setAdapter(postAdapter);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_board, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_post);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        PostUpdate();

        //게시판 글쓰기 부분
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), boardActivity.class));
            }
        });

        //검색
        recyclerView = view.findViewById(R.id.recyclerView_post);
        EditText searchText = view.findViewById(R.id.searchEditText);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("로그", "검색 버튼 눌러짐");
                String text = searchText.getText().toString();
                search(text);
            }
        });

        postAdapter = new PostAdapter(boardFragment.this, postList, memberList);
        recyclerView.setAdapter(postAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PostUpdate();
        recyclerView.setAdapter(postAdapter);
        //postAdapter.notifyDataSetChanged();
    }


    //실시간 업데이트
    public void PostUpdate() {
        postList = new ArrayList<>();
        memberList = new ArrayList<>();
        postAdapter = new PostAdapter(boardFragment.this, postList, memberList);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            CollectionReference collectionReference1 = firebaseFirestore.collection("users");
            collectionReference1
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            final Memberinfo memberinfo = new Memberinfo(
                                    document.getString("name"),
                                    document.getString("photoUrl"),
                                    document.getString("address"),
                                    document.getString("type"),
                                    document.getId());
                            memberList.add(memberinfo);
                        }
                    }
                }
            });

            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            final PostInfo postInfo = new PostInfo(
                                    //postList.add(postInfo = new PostInfo(
                                    document.getString("contents"),
                                    document.getString("publisher"),
                                    new Date(document.getDate("createdAt").getTime()),
                                    document.getId());
                            recyclerView.setAdapter(postAdapter);

                            // 좋아요 기능
                            DocumentReference postRef = document.getReference();
                            final CollectionReference likesRef = postRef.collection("likes");
                            likesRef
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            int likesCount = value.size();
                                            postInfo.setLikesCount(likesCount);
                                            likesRef.whereEqualTo("name", firebaseUser.getUid())
                                                    .get().addOnCompleteListener(task2 -> {
                                                if (task2.getResult().size() > 0) {
                                                    DocumentSnapshot likeDocument = task2.getResult().getDocuments().get(0);
                                                    postInfo.setLikeId(likeDocument.getId());
                                                    postInfo.setUserLiked(true);
                                                    recyclerView.setAdapter(postAdapter);
                                                    Log.d(TAG, likeDocument.getId());

                                                } else {
                                                    postInfo.setUserLiked(false);
                                                    recyclerView.setAdapter(postAdapter);
                                                }

                                            });
                                        }
                                    });

                            //댓글 카운트
                            final CollectionReference commentRef = postRef.collection("comments");
                            commentRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    int commentsCount = value.size();
                                    postInfo.setCommentCount(commentsCount);
                                }
                            });

                            postList.add(postInfo);
                            postAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(postAdapter);
                        }


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    //검색 기능
    private void search(String editable) {
        firebaseFirestore.collection("posts")
                .whereEqualTo("contents", editable.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "눌러짐");
                            postList.clear();
                            memberList.clear();

                            postList = new ArrayList<PostInfo>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e(TAG, document.getId() + " => " + document.getData());

                                CollectionReference collectionReference1 = firebaseFirestore.collection("users");
                                collectionReference1
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                final Memberinfo memberinfo = new Memberinfo(
                                                        document.getString("name"),
                                                        document.getString("photoUrl"),
                                                        document.getString("address"),
                                                        document.getString("type"),
                                                        document.getId());
                                                memberList.add(memberinfo);
                                            }
                                        }
                                    }
                                });

                                CollectionReference collectionReference = firebaseFirestore.collection("posts");
                                collectionReference
                                        .orderBy("createdAt", Query.Direction.DESCENDING)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            postList.clear();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (document.getString("contents").equals(editable)) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    final PostInfo postInfo = new PostInfo(
                                                            //postList.add(postInfo = new PostInfo(
                                                            document.getString("contents"),
                                                            document.getString("publisher"),
                                                            new Date(document.getDate("createdAt").getTime()),
                                                            document.getId());

                                                    // 좋아요 기능
                                                    DocumentReference postRef = document.getReference();
                                                    final CollectionReference likesRef = postRef.collection("likes");
                                                    likesRef
                                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                                    int likesCount = value.size();
                                                                    postInfo.setLikesCount(likesCount);
                                                                    likesRef.whereEqualTo("name", firebaseUser.getUid())
                                                                            .get().addOnCompleteListener(task2 -> {
                                                                        if (task2.getResult().size() > 0) {
                                                                            DocumentSnapshot likeDocument = task2.getResult().getDocuments().get(0);
                                                                            postInfo.setLikeId(likeDocument.getId());
                                                                            postInfo.setUserLiked(true);
                                                                            recyclerView.setAdapter(postAdapter);
                                                                            Log.d(TAG, likeDocument.getId());

                                                                        } else {
                                                                            postInfo.setUserLiked(false);
                                                                            recyclerView.setAdapter(postAdapter);
                                                                        }

                                                                    });
                                                                }
                                                            });
                                                    postList.add(postInfo);
                                                    postAdapter = new PostAdapter(boardFragment.this, postList, memberList);
                                                    postAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            recyclerView.setAdapter(postAdapter);
                                        }
                                    }
                                });


                                /* Memberinfo member = new Memberinfo(
                                        document.getString("name"),
                                        document.getString("photoUrl"),
                                        document.getString("address"),
                                        document.getString("type"),
                                        document.getId());
                                memberList.add(member);

                                    }
                                    postAdapter = new PostAdapter(boardFragment.this, postList, memberList);
                                    recyclerView.setAdapter(postAdapter);

                                } */
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

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