package com.example.project2021.board;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<PostInfo> mDataset = new ArrayList<>();
    private ArrayList<Memberinfo> mMemberList = new ArrayList<>();
    private Fragment fragment;
    private Activity activity;
    private OnPostListener onPostListener;
    private String address, name, type, photoUrl, profile;
    private FirebaseFirestore db;
    private boardFragment boardfragment;
    private FirebaseUser firebaseUser;
    private int prePosition = -1;

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        public CheckBox likeCheck;
        public TextView likeCount, commCount;

        PostViewHolder(CardView v) {
            super(v);
            cardView = v;
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            commCount = v.findViewById(R.id.txt_comNum);
            likeCount = v.findViewById(R.id.txt_HeartNum);
            likeCheck = v.findViewById(R.id.img_heart);
            likeCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getLayoutPosition();
            PostInfo postInfo = mDataset.get(position);
            Memberinfo memberinfo = mMemberList.get(position);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference postRef = db.document("posts/"+postInfo.getId());
            CollectionReference likesRef = postRef.collection("likes");
            Log.d("PostAdapter", "postRef: " + postRef);
            //likeCount.setText(""+postInfo.getLikesCount());

            if(postInfo.isUserLiked()){
                DocumentReference userLikeRef = likesRef.document(postInfo.getLikeId());
                userLikeRef.delete().addOnCompleteListener(task -> {
                    Log.d("firestore", "user removed");
                });

                Log.d("PostAdapter", "isUserLiked: " + postInfo.isUserLiked());

            } else {
                Map<String, Object> likeMap = new HashMap<>();
                likeMap.put("name", firebaseUser.getUid());
                likeMap.put("created_at", new Date());
                likesRef.add(likeMap)
                        .addOnCompleteListener(task -> {
                            Log.d("firestore", "user liked");
                        });

                Log.d("PostAdapter", "isUserLiked: " + postInfo.isUserLiked());
                Log.d("PostAdapter", "likeId: " + postInfo.getLikeId());
            }
            notifyDataSetChanged();
        }
    }


    public void add(PostInfo postInfo){
        mDataset.add(postInfo);
        notifyDataSetChanged();
    }

    public PostAdapter(Fragment fragment, ArrayList<PostInfo> mDataset, ArrayList<Memberinfo> mMemberList) {
        this.mDataset = mDataset;
        this.fragment = fragment;
        this.mMemberList = mMemberList;
    }

    public void setOnPostListener(OnPostListener onPostListener) {
        this.onPostListener = onPostListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        PostViewHolder postViewHolder = new PostViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        cardView.findViewById(R.id.menuImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, postViewHolder.getAdapterPosition());
            }
        });
        return postViewHolder;
    }

    private void showPopup(View v, int position) {
        boardfragment = new boardFragment();
        PopupMenu popup = new PopupMenu(fragment.getActivity(), v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String id = mDataset.get(position).getId();
                switch (item.getItemId()) {
                    case R.id.modify:
                        Intent intent = new Intent(fragment.getActivity(), boardActivity.class);
                        fragment.startActivity(intent);
                        return true;
                    case R.id.delete:
                        db.collection("posts").document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("로그:", "삭제 완료");
                                        Toast.makeText(fragment.getActivity(), "게시글을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(fragment.getActivity(), "게시글을 삭제하지 못하였습니다.", Toast.LENGTH_LONG).show();
                                    }
                                });
                        // onPostListener.onDelete(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post_menu, popup.getMenu());
        popup.show();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.text_post);
        TextView createdAtTextView = cardView.findViewById(R.id.CreatedAtTextView);
        TextView txt_address = cardView.findViewById(R.id.txt_address_post);
        TextView txt_name = cardView.findViewById(R.id.txt_name_post);
        ImageView img_type = cardView.findViewById(R.id.img_type_post);
        ImageView img_profile = cardView.findViewById(R.id.img_profile_post);

        textView.setText(mDataset.get(position).getContents());
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));
        for(int i = 0; i < mMemberList.size(); i++) {
            if (mDataset.get(position).getPublisher().equals(mMemberList.get(i).getId())) {
                txt_address.setText(mMemberList.get(i).getAddress());
                txt_name.setText(mMemberList.get(i).getName());
                String type = mMemberList.get(i).getType();
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
                if (mMemberList.get(i).getPhotoUrl() != null) {
                    Glide.with(cardView).load(mMemberList.get(i).getPhotoUrl()).centerCrop().override(1000).into(img_profile);
                } else {
                    img_profile.setImageResource(R.mipmap.media_avatar);
                }
                if (mDataset.get(position).getLikeId() != null) {
                    if (mDataset.get(position).isUserLiked()) {
                        holder.likeCheck.setChecked(true);
                    }
                }
                holder.likeCount.setText(""+mDataset.get(position).getLikesCount());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void startToast(String msg) {
        Toast.makeText(fragment.getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
