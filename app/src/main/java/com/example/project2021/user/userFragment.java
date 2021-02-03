package com.example.project2021.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.MainActivity;
import com.example.project2021.board.PostAdapter;
import com.example.project2021.board.PostInfo;
import com.example.project2021.board.boardFragment;
import com.example.project2021.profile.Memberinfo;
import com.example.project2021.profile.ProfileActivity;
import com.example.project2021.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class userFragment extends Fragment {

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "userFragment";
    private RecyclerView recyclerView;
    private ImageView img_profile;
    private ImageButton profile_edit;
    private ArrayList<PostInfo> postList;
    private ArrayList<Memberinfo> memberList;
    private PostAdapter postAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
        postList = new ArrayList<>();
        memberList = new ArrayList<>();
        postAdapter = new PostAdapter(userFragment.this, postList, memberList);
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
                            if (document.getString("publisher").equals(firebaseUser.getUid())) {
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
                                                        postAdapter.notifyDataSetChanged();
                                                        //recyclerView.setAdapter(postAdapter);
                                                        Log.d(TAG, likeDocument.getId());

                                                    } else {
                                                        postInfo.setUserLiked(false);
                                                        postAdapter.notifyDataSetChanged();
                                                        //recyclerView.setAdapter(postAdapter);
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
                                        recyclerView.setAdapter(postAdapter);
                                    }
                                });

                                postList.add(postInfo);
                                postAdapter.notifyDataSetChanged();
                            }
                        }
                        recyclerView.setAdapter(postAdapter);
                    }
                }
            });
        }
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        recyclerView = view.findViewById(R.id.user_recyclerView);

        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.icon_arrow_left_1);

        //유저 프로필
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String address = document.getString("address");

                            String name = document.getString("name");
                            TextView Text_name = view.findViewById(R.id.txt_name);
                            Text_name.setText(name);

                            img_profile = view.findViewById(R.id.img_profile);
                            if(document.getString("photoUrl") != null) {
                                String profile = document.getString("photoUrl");
                                Glide.with(view).load(profile).centerCrop().override(1000).into(img_profile);
                                img_profile.setAdjustViewBounds(true);
                            }else{
                                img_profile.setImageResource(R.mipmap.media_avatar);
                            }

                            String type = document.getString("type");
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
                            TextView addinfo1 = view.findViewById(R.id.addinfo1);
                            addinfo1.setText("# 나는 "+address+"에 살고있어요");
                            TextView addinfo2 = view.findViewById(R.id.addinfo2);
                            addinfo2.setText("# 나는 "+type+" 타입이에요");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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
}