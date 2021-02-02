package com.example.project2021.board;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
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
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class commentFragment extends Fragment {
    private static final String TAG = "commentFragment";
    private RecyclerView recyclerView;
    private CommentAdapter mAdapter;
    private ArrayList<CommInfo> mList = new ArrayList<>();
    private ArrayList<PostInfo> postList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Memberinfo memberinfo;
    private CommInfo commInfo;
    private PostInfo postInfo;
    private ArrayList<Memberinfo> memberList = new ArrayList<>();
    private String getId;
    Button Comment_Save;
    EditText commText;
    TextView commentNum;
    int getCommCount;

    public static Fragment newInstance
            (String getContent, String getId, String getPublisher, String getName, String getAddress, String getType, String getPhotoUrl, int getLikeCount, int getCommCount, boolean setUserLiked) {
        Bundle args = new Bundle();
        args.putString("contents", getContent);
        args.putString("id", getId);
        args.putString("publisher", getPublisher);
        args.putString("name", getName);
        args.putString("address", getAddress);
        args.putString("type", getType);
        args.putString("photoUrl", getPhotoUrl);
        args.putInt("likeCount", getLikeCount);
        args.putInt("commentCount", getCommCount);
        args.putBoolean("likecheck", setUserLiked);

        //og.d(TAG, "setUserliked1 : "+setUserLiked);
        commentFragment fragment = new commentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView_comm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        commentUpdate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        //포스트 유저
        ImageView post_profile = view.findViewById(R.id.img_profile_PC);
        ImageView post_type = view.findViewById(R.id.img_type_PC);
        TextView post_name = view.findViewById(R.id.txt_name_PC);
        TextView post_address = view.findViewById(R.id.txt_address_PC);
        TextView post_text = view.findViewById(R.id.addinfo2);
        commentNum = view.findViewById(R.id.txt_comNum1);
        TextView heartNum = view.findViewById(R.id.txt_HeartNum1);
        CheckBox heartButton = view.findViewById(R.id.img_heart1);


        Bundle bundle = getArguments();
        if(getArguments() != null){
            //Log.d("commentFragment","bundle.getContents : " + bundle.getString("contents"));
            //Log.d("commentFragment","bundle.getId : " + bundle.getString("id"));
            //Log.d("commentFragment","bundle.getPublisher : " + bundle.getString("publisher"));
            String getContents = bundle.getString("contents");
            getId = bundle.getString("id");
            String getPublisher = bundle.getString("publisher");
            String getName = bundle.getString("name");
            String getAddress = bundle.getString("address");
            String getType =  bundle.getString("type");
            String getPhotoUrl =  bundle.getString("photoUrl");
            int getLikeCount = bundle.getInt("likeCount");
            getCommCount = bundle.getInt("commentCount");
            boolean setUserLiked = bundle.getBoolean("likecheck");
            //Log.d(TAG, "setUserliked2 : "+bundle.getBoolean("likecheck"));
            heartButton.setChecked(setUserLiked);

            post_text.setText(getContents);
            post_name.setText(getName);
            post_address.setText(getAddress);
            commentNum.setText(""+getCommCount);
            heartNum.setText(""+getLikeCount);

            switch (getType) {
                case "더위를 많이 타는":
                    post_type.setImageResource(R.mipmap.fire_icon);
                    break;
                case "적당한":
                    post_type.setImageResource(R.mipmap.water_icon);
                    break;
                case "추위를 많이 타는":
                    post_type.setImageResource(R.mipmap.ice_icon);
                    break;
            }
            if (getPhotoUrl != null) {
                Glide.with(view).load(getPhotoUrl).centerCrop().override(1000).into(post_profile);
            } else {
                post_profile.setImageResource(R.mipmap.media_avatar);
            }


            // 좋아요
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference postRef = firebaseFirestore.document("posts/"+getId);
                    CollectionReference likesRef = postRef.collection("likes");

                    if(setUserLiked){
                        String id = likesRef.whereEqualTo("name", firebaseUser.getUid()).get().getResult().toString();
                        Log.d(TAG, "id :" +id);
                        DocumentReference userLikeRef = likesRef.document(id);
                        userLikeRef.delete().addOnCompleteListener(task -> {
                            Log.d("firestore", "user removed");
                        });
                        heartButton.setChecked(false);
                    } else {
                        Map<String, Object> likeMap = new HashMap<>();
                        likeMap.put("name", firebaseUser.getUid());
                        likeMap.put("created_at", new Date());
                        likesRef.add(likeMap)
                                .addOnCompleteListener(task -> {
                                    Log.d("firestore", "user liked");
                                });
                    }
                    //int likesCount = likesRef.get().getResult().size();
                    //heartNum.setText(""+likesCount);
                }
            });
        }


        //코멘트 유저
        commText = view.findViewById(R.id.editText);
        TextView commName = view.findViewById(R.id.comm_txt_name);
        TextView commAddress = view.findViewById(R.id.comm_txt_address);
        ImageView commType = view.findViewById(R.id.comm_img_type);
        Comment_Save = view.findViewById(R.id.comm_save);

        if (firebaseUser != null) {
            CollectionReference collectionReference1 = firebaseFirestore.collection("users");
            collectionReference1
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                            final Memberinfo memberinfo = new Memberinfo(
                                    document.getString("name"),
                                    document.getString("photoUrl"),
                                    document.getString("address"),
                                    document.getString("type"),
                                    document.getId());
                            memberList.add(memberinfo);
                        }
                    }

                    for (int i = 0; i < memberList.size(); i++){
                        if (firebaseUser.getUid().equals(memberList.get(i).getId())){
                            commName.setText(memberList.get(i).getName());
                            commAddress.setText(memberList.get(i).getAddress());
                            String type = memberList.get(i).getType();
                            switch (type) {
                                case "더위를 많이 타는":
                                    commType.setImageResource(R.mipmap.fire_icon);
                                    break;
                                case "적당한":
                                    commType.setImageResource(R.mipmap.water_icon);
                                    break;
                                case "추위를 많이 타는":
                                    commType.setImageResource(R.mipmap.ice_icon);
                                    break;
                            }
                        }
                    }

                }
            });
        }
            return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        commentUpdate();
        recyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
    }

    private void commentUpdate() {
        memberList = new ArrayList<>();
        mAdapter = new CommentAdapter(mList);
        DocumentReference postRef = firebaseFirestore.document("posts/" + getId);
        CollectionReference commentRef = postRef.collection("comments");
        commentRef.get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               recyclerView.setAdapter(mAdapter);
           }
        });
        Comment_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Comment_Text = commText.getText().toString();

                Map<String, Object> commentMap = new HashMap<>();
                commentMap.put("name", firebaseUser.getUid());
                commentMap.put("comment", Comment_Text);
                commentMap.put("created_at", new Date());
                commentRef.add(commentMap);
                commText.setText("");

                commentRef.orderBy("created_at", Query.Direction.ASCENDING)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "comment : " + document.getId() + " => " + document.getData());
                                final CommInfo commInfo = new CommInfo(
                                        document.getString("name"),
                                        document.getString("comment"),
                                        document.getDate("created_at"),
                                        document.getId());
                                mList.add(commInfo);
                                int commentsCount = task.getResult().size();
                                commentNum.setText(""+commentsCount);
                                recyclerView.setAdapter(mAdapter);
                            }
                        }
                    }
                });
            }
        });

        commentRef.orderBy("created_at", Query.Direction.ASCENDING)
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            mList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "comment : " + document.getId() + " => " + document.getData());
                                final CommInfo commInfo = new CommInfo(
                                        document.getString("name"),
                                        document.getString("comment"),
                                        document.getDate("created_at"),
                                        document.getId());
                                mList.add(commInfo);

                                if (document.getString("name").equals(firebaseUser.getUid())) {
                                    Log.d(TAG, "name : " + document.getString("name") + "/ Uid : " + firebaseUser.getUid());
                                    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                                        @Override
                                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                                            return false;
                                        }

                                        @Override
                                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                            String id = mList.get(viewHolder.getLayoutPosition()).getCommentId();
                                            commentRef.document(id).delete();
                                            mList.remove(viewHolder.getLayoutPosition());
                                            int commentsCount = mList.size();
                                            commentNum.setText(""+commentsCount);
                                            mAdapter.notifyItemRemoved(viewHolder.getLayoutPosition());
                                        }
                                    };
                                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                                    itemTouchHelper.attachToRecyclerView(recyclerView);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        Log.d(TAG, "mList : " + mList.size());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

}
