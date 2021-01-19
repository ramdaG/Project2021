package com.example.project2021.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.GpsTracker;
import com.example.project2021.MainActivity;
import com.example.project2021.board.PostAdapter;
import com.example.project2021.board.PostInfo;
import com.example.project2021.board.boardFragment;
import com.example.project2021.profile.Memberinfo;
import com.example.project2021.profile.ProfileActivity;
import com.example.project2021.R;
import com.example.project2021.board.Post_item;
import com.example.project2021.board.RecyclerAdapter_Post;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class userFragment extends Fragment {

    private GpsTracker gpsTracker;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private static final String TAG = "userFragment";

    private View view;
    private RecyclerView recyclerView;


    RecyclerView mRecyclerView = null ;
    RecyclerAdapter_Post mAdapter = null ;
    ArrayList<Post_item> mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton profile_edit;
    ImageButton img_heart;
    TextView Text_address;


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


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            String address = document.getString("address");
                            Text_address = view.findViewById(R.id.txt_address);
                            Text_address.setText(address);

                            String name = document.getString("name");
                            TextView Text_name = view.findViewById(R.id.txt_name);
                            Text_name.setText(name);

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
                            recyclerView = view.findViewById(R.id.recyclerView);

                            ArrayList<PostInfo> postList = new ArrayList<>();
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            RecyclerView.Adapter mAdapter = new PostAdapter(userFragment.this, postList);
                            recyclerView.setAdapter(mAdapter);
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

    private void initDataset() {
        mList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            mList.add(new Post_item(R.id.img_profile, "박소현","인천 가좌동","안녕하세요"));
        }
    }

}