package com.example.project2021.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Memberinfo> mMemberList = new ArrayList<>();
    private ArrayList<CommInfo> commInfo = new ArrayList<>();
    private Fragment fragment;
    private commentFragment commentFragment;
    CommentAdapter(ArrayList<CommInfo> list) {
        this.commInfo = list;
    }

    CommentAdapter.OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(CommentAdapter.ViewHolder holder, View view, int position);
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_post_comment, parent, false);

        return new CommentAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        CommInfo commList = commInfo.get(position);
        holder.setItem(commList);
        holder.setOnItemClickListener(listener);
    }

    public void addItem(CommInfo item) {
        commInfo.add(item);
    }

    public void addItems(ArrayList<CommInfo> commInfo) {
        this.commInfo = commInfo;
    }

    public CommInfo getItem(int position) {
        return commInfo.get(position);
    }


    public void changeItem(CommInfo item, int position){
        commInfo.set(position,item);
        notifyItemChanged(position);
    }


    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return commInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_type;
        protected TextView name;
        protected TextView comment;
        protected TextView time;
        protected TextView address;
        CommentAdapter.OnItemClickListener listener;
        private int prePosition = -1;

        public ViewHolder(final View itemView) {
            super(itemView);

            img_type = itemView.findViewById(R.id.type_comm);
            name = itemView.findViewById(R.id.name_comm);
            address = itemView.findViewById(R.id.address_comm);
            comment = itemView.findViewById(R.id.comment_comm);
            time = itemView.findViewById(R.id.time_comm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(CommentAdapter.ViewHolder.this, itemView, position);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                }
            });
        }

        public void setItem(CommInfo item) {
            comment.setText(item.getComment());
            time.setText(new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(item.getCreated_at()));

            if (firebaseUser != null) {
                CollectionReference collectionReference1 = db.collection("users");
                collectionReference1
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Memberinfo memberinfo = new Memberinfo(
                                        document.getString("name"),
                                        document.getString("photoUrl"),
                                        document.getString("address"),
                                        document.getString("type"),
                                        document.getId());
                                mMemberList.add(memberinfo);
                            }
                            for (int i = 0; i < mMemberList.size(); i++) {
                                if (item.getName().equals(mMemberList.get(i).getId())) {
                                    name.setText(mMemberList.get(i).getName());
                                    address.setText(mMemberList.get(i).getAddress());
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
                                }
                            }
                        }
                    }
                });
            }
        }

        public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}