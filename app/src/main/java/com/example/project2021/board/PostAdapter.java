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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.OnPostListener;
import com.example.project2021.R;
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
import com.google.firebase.firestore.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Fragment fragment;
    private Activity activity;
    private OnPostListener onPostListener;
    private String address, name, type, likeId, profile;
    private FirebaseFirestore db;
    private boardFragment boardfragment;

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        public CheckBox likeCheck;
        public TextView likeCount, commCount;
        PostViewHolder(CardView v) {
            super(v);
            cardView = v;
            likeCount = v.findViewById(R.id.txt_HeartNum);
            likeCheck = v.findViewById(R.id.img_heart);
            likeCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getLayoutPosition();
            PostInfo postInfo = mDataset.get(position);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference postRef = db.document("posts/"+postInfo.getId());
            CollectionReference likesRef = postRef.collection("likes");

            if(postInfo.isUserLiked()){
                DocumentReference userLikeRef = likesRef.document(postInfo.getLikeId());
                userLikeRef.delete().addOnCompleteListener(task -> {
                    Log.d("firestore", "user removed");
                });
            } else {
                Map<String, Object> likeMap = new HashMap<>();
                likeMap.put("name", postInfo.getId());
                likeMap.put("created_at", new Date());
                likesRef.add(likeMap)
                        .addOnCompleteListener(task -> {
                            Log.d("firestore", "user liked");
                        });
            }
        }
    }

    public PostAdapter(Fragment fragment, ArrayList<PostInfo> mDataset) {
        this.mDataset = mDataset;
        this.fragment = fragment;
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

        final PostViewHolder postViewHolder = new PostViewHolder(cardView);
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
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostInfo postInfo = mDataset.get(position);
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.text_post);
        textView.setText(mDataset.get(position).getContents());

        TextView createdAtTextView = cardView.findViewById(R.id.CreatedAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));

        holder.likeCount.setText(String.valueOf(postInfo.getLikesCount()));
        holder.likeCheck.setChecked(postInfo.isUserLiked());

        //닉네임, 주소, 타입 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        //DocumentReference docRef = db.document("users/"+postInfo.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            address = document.getString("address");
                            TextView Text_address = cardView.findViewById(R.id.txt_address_post);
                            Text_address.setText(address);

                            name = document.getString("name");
                            TextView Text_name = cardView.findViewById(R.id.txt_name_post);
                            Text_name.setText(name);

                            type = document.getString("type");
                            ImageView img_type = cardView.findViewById(R.id.img_type_post);
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
                            profile = document.getString("photoUrl");
                            ImageView img_profile = cardView.findViewById(R.id.img_profile_post);
                            Glide.with(cardView).load(profile).override(1000).into(img_profile);

                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void startToast(String msg) {
        Toast.makeText(fragment.getActivity(), msg, Toast.LENGTH_LONG).show();
    }

}
