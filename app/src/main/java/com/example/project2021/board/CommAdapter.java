package com.example.project2021.board;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CommAdapter extends RecyclerView.Adapter<CommAdapter.CommViewHolder> {
    private ArrayList<CommInfo> mCommList = new ArrayList<>();
    private ArrayList<PostInfo> mDataset = new ArrayList<>();
    private ArrayList<Memberinfo> mMemberList = new ArrayList<>();
    private Fragment fragment;
    private OnPostListener onPostListener;
    //private String address, name, type, photoUrl, profile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    class CommViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        CommViewHolder(CardView v) {
            super(v);

        }
    }

    public void add(CommInfo commInfo){
        mCommList.add(commInfo);
        notifyDataSetChanged();
    }

    public CommAdapter(Fragment fragment, ArrayList<CommInfo> mCommList, ArrayList<Memberinfo> mMemberList) {
        this.mCommList = mCommList;
        this.mMemberList = mMemberList;
        this.fragment = fragment;
    }


    public CommAdapter(ArrayList<CommInfo> mCommList, ArrayList<Memberinfo> mMemberList) {
        this.mCommList = mCommList;
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
    public CommAdapter.CommViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_comment, parent, false);
        CommViewHolder commViewHolder = new CommViewHolder(cardView);
        return commViewHolder;
    }


    public TextView createdAtTextView, txt_address, txt_name, txt_comment;
    public ImageView img_type, img_menu;

    @Override
    public void onBindViewHolder(@NonNull CommViewHolder holder, int position) {
        //CommInfo commInfo = mCommList.get(position);
        CardView cardView = holder.cardView;
        createdAtTextView = cardView.findViewById(R.id.time_comm);
        txt_comment = cardView.findViewById(R.id.comment_comm);
        txt_address = cardView.findViewById(R.id.address_comm);
        txt_name = cardView.findViewById(R.id.name_comm);
        img_type = cardView.findViewById(R.id.type_comm);
        img_menu = cardView.findViewById(R.id.menuImage_comm);
        //String id = db.collection("posts").document().getId();
        //String comment = db.collection("posts").document(id).collection("comments").document("comment").toString();
        String comment = mCommList.get(position).getComment();
        String name = mCommList.get(position).getName();
        //String address = mCommList.get(position).getAddress();

        for (int i = 0; i < mMemberList.size(); i++) {
            if (name.equals(mMemberList.get(i).getId())) {
                txt_comment.setText(comment);
                createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mCommList.get(position).getCreatedAt()));
                txt_name.setText(mMemberList.get(i).getName());
                txt_address.setText(mMemberList.get(i).getAddress());
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
                if (mCommList.get(position).getId().equals(firebaseUser.getUid())) {
                    img_menu.setVisibility(cardView.VISIBLE);
                    img_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopup(v, position);
                        }
                    });
                } else {
                    img_menu.setVisibility(cardView.INVISIBLE);
                }
            }
        }

        /*
        for(int i = 0; i < mCommList.size(); i++) {
            if (mCommList.get(position).getId().equals(mMemberList.get(i).getId())) {
                holder.txt_address.setText(mMemberList.get(i).getAddress());
                holder.txt_name.setText(mMemberList.get(i).getName());
                String type = mMemberList.get(i).getType();
                switch (type) {
                    case "더위를 많이 타는":
                        holder.img_type.setImageResource(R.mipmap.fire_icon);
                        break;
                    case "적당한":
                        holder.img_type.setImageResource(R.mipmap.water_icon);
                        break;
                    case "추위를 많이 타는":
                        holder.img_type.setImageResource(R.mipmap.ice_icon);
                        break;
                }

                if (mCommList.get(position).getId().equals(firebaseUser.getUid())){
                    holder.img_menu.setVisibility(cardView.VISIBLE);
                    holder.img_menu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopup(v, position);
                        }
                    });
                } else {
                    holder.img_menu.setVisibility(cardView.INVISIBLE);
                }
            }
        }*/
    }

    private void showPopup(View v, int position) {
        //Fragment commentFragment = new commentFragment();
        PopupMenu popup = new PopupMenu(fragment.getActivity(), v);
        String id = mCommList.get(position).getId();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mCommList.get(position).getId().equals(firebaseUser.getUid())) {
                    switch (item.getItemId()) {
                        case R.id.modify:
                            Intent intent = new Intent(fragment.getActivity(), commentActivity.class);
                            intent.putExtra("comments", mCommList.get(position).getComment());
                            intent.putExtra("id", mCommList.get(position).getId());
                            fragment.startActivity(intent);
                            return true;
                        case R.id.delete:
                            //Log.d("PostAdapter", "id : " + id);
                            db.collection("posts").document(id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("로그:", "삭제 완료");
                                            Toast.makeText(fragment.getActivity(), "댓글을 삭제했습니다.", Toast.LENGTH_LONG).show();
                                            notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(fragment.getActivity(), "댓글을 삭제하지 못하였습니다.", Toast.LENGTH_LONG).show();
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

    @Override
    public int getItemCount() {
        return mCommList == null ? 0 : mCommList.size();
    }

    private void startToast(String msg) {
        Toast.makeText(fragment.getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}
