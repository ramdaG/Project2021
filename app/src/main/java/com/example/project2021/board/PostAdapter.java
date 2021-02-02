package com.example.project2021.board;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    private String address, name, type, photoUrl, profile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boardFragment boardfragment;
    private FirebaseUser firebaseUser;
    int strlikeCount, strcommCount;

    PostAdapter(ArrayList<PostInfo> list) {
        this.mDataset = list;
    }

    PostAdapter.OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(PostAdapter.PostViewHolder holder, View view, int position);
    }

    class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        public CheckBox likeCheck;
        public TextView likeCount, commCount;
        protected TextView textView, createdAtTextView, txt_address, txt_name;
        protected ImageView img_type, img_profile, img_menu;
        public ImageButton comment;
        PostAdapter.OnItemClickListener listener;
        private int prePosition = -1;
        Intent intent = new Intent(fragment.getActivity(), commentActivity.class);

        PostViewHolder(CardView v) {
            super(v);
            cardView = v;
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            commCount = v.findViewById(R.id.txt_comNum);
            likeCount = v.findViewById(R.id.txt_HeartNum);
            comment = v.findViewById(R.id.img_com);
            likeCheck = v.findViewById(R.id.img_heart);
            likeCheck.setOnClickListener(this::onClick);
            comment.setOnClickListener(this::onClick2);
            textView = cardView.findViewById(R.id.text_post);
            createdAtTextView = cardView.findViewById(R.id.CreatedAtTextView);
            txt_address = cardView.findViewById(R.id.txt_address_post);
            txt_name = cardView.findViewById(R.id.txt_name_post);
            img_type = cardView.findViewById(R.id.img_type_post);
            img_profile = cardView.findViewById(R.id.img_profile_post);
            img_menu = cardView.findViewById(R.id.menuImage);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(PostAdapter.PostViewHolder.this, cardView, position);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                }
            });

        }

        public void setOnItemClickListener(PostAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            final int position = getLayoutPosition();
            PostInfo postInfo = mDataset.get(position);

            DocumentReference postRef = db.document("posts/"+postInfo.getId());
            CollectionReference likesRef = postRef.collection("likes");
            //Log.d("PostAdapter", "postRef: " + postRef);

            if(postInfo.isUserLiked()){
                DocumentReference userLikeRef = likesRef.document(postInfo.getLikeId());
                userLikeRef.delete().addOnCompleteListener(task -> {
                    Log.d("firestore", "user removed");
                });
                likeCheck.setChecked(false);
                Log.d("PostAdapter", "isUserLiked: " + postInfo.isUserLiked());

            } else {
                Map<String, Object> likeMap = new HashMap<>();
                likeMap.put("name", firebaseUser.getUid());
                likeMap.put("created_at", new Date());
                likesRef.add(likeMap)
                        .addOnCompleteListener(task -> {
                            Log.d("firestore", "user liked");
                        });

                //Log.d("PostAdapter", "isUserLiked: " + postInfo.isUserLiked());
                //Log.d("PostAdapter", "likeId: " + postInfo.getLikeId());
            }
            notifyDataSetChanged();
        }

        public void onClick2(View v){
            final int position = getLayoutPosition();
            PostInfo postInfo = mDataset.get(position);

            for (int i = 0; i < mMemberList.size(); i++){
                if (postInfo.getPublisher().equals(mMemberList.get(i).getId())){
                    name = mMemberList.get(i).getName();
                    address = mMemberList.get(i).getAddress();
                    type = mMemberList.get(i).getType();
                    photoUrl = mMemberList.get(i).getPhotoUrl();
                }
            }

            intent.putExtra("contents", postInfo.getContents());
            intent.putExtra("id", postInfo.getId());
            intent.putExtra("publisher", postInfo.getPublisher());
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("type", type);
            intent.putExtra("photoUrl", photoUrl);


            fragment.startActivity(intent);
            //Log.d("PostAdapter", "like1 : " + strlikeCount);
        }

        public void setItem(PostInfo item) {

            textView.setText(item.getContents());
            createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getCreatedAt()));

            for(int i = 0; i < mMemberList.size(); i++) {
                if (item.getPublisher().equals(mMemberList.get(i).getId())) {
                    txt_address.setText(mMemberList.get(i).getAddress());
                    txt_name.setText(mMemberList.get(i).getName());
                    String txt_type = mMemberList.get(i).getType();

                    switch (txt_type) {
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
                    if (item.getLikeId() != null) {
                        if (item.isUserLiked()) {
                            likeCheck.setChecked(true);
                        } else { likeCheck.setChecked(false); }
                    }
                    strlikeCount = item.getLikesCount();
                    strcommCount = item.getCommentCount();
                    intent.putExtra("likeCount", strlikeCount);
                    intent.putExtra("commentCount", strcommCount);
                    intent.putExtra("likecheck", item.isUserLiked());
                    intent.putExtra("likeId", item.getLikeId());
                    likeCount.setText(""+item.getLikesCount());
                    commCount.setText(""+item.getCommentCount());
                    //Log.d("PostAdapter", "like2 : " + strlikeCount);

                    if (item.getPublisher().equals(firebaseUser.getUid())){
                        img_menu.setVisibility(cardView.VISIBLE);

                        img_menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //showPopup(v, getItemViewType());
                                showPopup2(v, item);
                            }
                        });
                    } else {
                        img_menu.setVisibility(cardView.INVISIBLE);
                    }
                }
            }
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


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
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

        return postViewHolder;
    }

    /*
    private void showPopup(View v, int position) {
        boardfragment = new boardFragment();
        PopupMenu popup = new PopupMenu(fragment.getActivity(), v);
        String id = mDataset.get(position).getId();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mDataset.get(position).getPublisher().equals(firebaseUser.getUid())) {
                    switch (item.getItemId()) {
                        case R.id.modify:
                            Intent intent = new Intent(fragment.getActivity(), boardActivity.class);
                            intent.putExtra("contents", mDataset.get(position).getContents());
                            intent.putExtra("id", mDataset.get(position).getId());
                            fragment.startActivity(intent);
                            return true;
                        case R.id.delete:
                            Log.d("PostAdapter", "id : " + id);
                            db.collection("posts").document(id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("로그:", "삭제 완료");
                                            Toast.makeText(fragment.getActivity(), "게시글을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                            notifyDataSetChanged();
                                            changeItem(, position);
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
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post_menu, popup.getMenu());
        popup.show();
        notifyDataSetChanged();
    }
*/

    private void showPopup2(View v, PostInfo postInfo) {
        boardfragment = new boardFragment();
        PopupMenu popup = new PopupMenu(fragment.getActivity(), v);
        String id = postInfo.getId();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (postInfo.getPublisher().equals(firebaseUser.getUid())) {
                    switch (item.getItemId()) {
                        case R.id.modify:
                            Intent intent = new Intent(fragment.getActivity(), boardActivity.class);
                            intent.putExtra("contents", postInfo.getContents());
                            intent.putExtra("id", postInfo.getId());
                            fragment.startActivity(intent);
                            return true;
                        case R.id.delete:
                            Log.d("PostAdapter", "id : " + id);
                            db.collection("posts").document(id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("로그:", "삭제 완료");
                                            Toast.makeText(fragment.getActivity(), "게시글을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                            notifyDataSetChanged();
                                            FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                            ft.detach(fragment).attach(fragment).commit();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(fragment.getActivity(), "게시글을 삭제하지 못하였습니다.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                            return true;
                        default:
                            return false;
                    }
                }
                return true;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        PostInfo postList = mDataset.get(position);
        holder.setItem(postList);
        holder.setOnItemClickListener(listener);

    }

    public void addItem(PostInfo item) {
        mDataset.add(item);
    }

    public void addItems(ArrayList<PostInfo> mDataset) {
        this.mDataset = mDataset;
    }

    public PostInfo getItem(int position) {
        return mDataset.get(position);
    }

    public void changeItem(PostInfo item, int position){
        mDataset.set(position,item);
        notifyItemChanged(position);
    }

    public void setOnItemClickListener(PostAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
