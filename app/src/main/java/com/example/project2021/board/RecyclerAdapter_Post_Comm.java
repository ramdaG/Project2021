package com.example.project2021.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.profile.Memberinfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RecyclerAdapter_Post_Comm extends RecyclerView.Adapter<RecyclerAdapter_Post_Comm.ViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Memberinfo> mMemberList = new ArrayList<>();
    private ArrayList<CommInfo> items = new ArrayList<>();

    RecyclerAdapter_Post_Comm(ArrayList<CommInfo> list) {
        this.items = list;
    }

    RecyclerAdapter_Post_Comm.OnItemClickListener listener;

    public static interface OnItemClickListener {
        public void onItemClick(RecyclerAdapter_Post_Comm.ViewHolder holder, View view, int position);
    }

    @Override
    public RecyclerAdapter_Post_Comm.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_post_comment, parent, false);

        return new RecyclerAdapter_Post_Comm.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Post_Comm.ViewHolder holder, int position) {
        CommInfo item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    public void addItem(CommInfo item) {
        items.add(item);
    }

    public void addItems(ArrayList<CommInfo> items) {
        this.items = items;
    }

    public CommInfo getItem(int position) {
        return items.get(position);
    }


    public void changeItem(CommInfo item, int position){
        items.set(position,item);
        notifyItemChanged(position);
    }


    public void setOnItemClickListener(RecyclerAdapter_Post_Comm.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return items.size();
        //return 100;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img_type, img_menu;
        protected TextView name;
        protected TextView comment;
        protected TextView time;
        RecyclerAdapter_Post_Comm.OnItemClickListener listener;
        private int prePosition = -1;

        public ViewHolder(final View itemView) {
            super(itemView);

            img_type = itemView.findViewById(R.id.type_comm);
            name = itemView.findViewById(R.id.name_comm);
            comment = itemView.findViewById(R.id.comment_comm);
            time = itemView.findViewById(R.id.time);
            img_menu = itemView.findViewById(R.id.menuImage_comm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(RecyclerAdapter_Post_Comm.ViewHolder.this, itemView, position);
                    }
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                }
            });
        }

        public void setItem(CommInfo item) {
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

            for (int i = 0; i < mMemberList.size(); i++) {
                if (name.equals(mMemberList.get(i).getId())) {
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
                    if (items.get(i).getId().equals(firebaseUser.getUid())) {
                        img_menu.setVisibility(itemView.VISIBLE);
                        img_menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //showPopup(v, item);
                            }
                        });
                    } else {
                        img_menu.setVisibility(itemView.INVISIBLE);
                    }
                }
            }
            //type.setImageResource();
            name.setText(item.getName());
            comment.setText(item.getComment());
            time.setText(currentTime);
        }

        public void setOnItemClickListener(RecyclerAdapter_Post_Comm.OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}