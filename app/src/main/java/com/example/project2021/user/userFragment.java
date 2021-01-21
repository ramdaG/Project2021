package com.example.project2021.user;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.board.PostAdapter;
import com.example.project2021.board.PostInfo;
import com.example.project2021.profile.ProfileActivity;
import com.example.project2021.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class userFragment extends Fragment {

    //private GpsTracker gpsTracker;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "userFragment";
    private View view;
    private RecyclerView recyclerView;

    //RecyclerView mRecyclerView = null ;
    //RecyclerAdapter_Post mAdapter = null ;
    //ArrayList<Post_item> mList;
    //private RecyclerView.LayoutManager mLayoutManager;

    private ImageButton profile_edit;
    ImageButton img_heart;
    TextView Text_address;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //mRecyclerView= getActivity().findViewById(R.id.recycler);
        //RecyclerAdapter_Post adapter = new RecyclerAdapter_Post(mList);
        //mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initDataset();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firebaseUser != null) {
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
            CollectionReference collectionReference = firebaseFirestore.collection("posts");
            collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).get()
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
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                RecyclerView.Adapter mAdapter = new PostAdapter(userFragment.this, postList);
                                recyclerView.setAdapter(mAdapter);

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
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
        /*
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
*/
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

                            String profile = document.getString("photoUrl");
                            ImageView img_profile = view.findViewById(R.id.img_profile);
                            Glide.with(view).load(profile).override(1000).into(img_profile);

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
/*
    private void initDataset() {
        mList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            mList.add(new Post_item(R.id.img_profile, "박소현","인천 가좌동","안녕하세요"));
        }
    }
*/
}