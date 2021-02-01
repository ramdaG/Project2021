package com.example.project2021.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.board.PostInfo;
import com.example.project2021.profile.Memberinfo;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RecyclerAdapter_Comment extends RecyclerView.Adapter<RecyclerAdapter_Comment.ViewHolder> {

    private ArrayList <Comment_item> items;
    static ArrayList<Memberinfo> mUserList = new ArrayList<>();
    Fragment fragment;
    FirebaseUser firebaseUser;

    RecyclerAdapter_Comment(ArrayList<Comment_item> list) {
        this.items = list ;
    }
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    @Override
    public RecyclerAdapter_Comment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.comment_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Comment.ViewHolder holder, int position) {
        Comment_item item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);

    }

    public  void addItem(Comment_item item){
        items.add(item);
    }

    public void addItems(ArrayList<Comment_item> items){
        this.items = items;
    }

    public Comment_item getItem(int position){
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return items.size() ;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView type ;
        protected TextView name ;
        protected TextView comment;
        protected TextView time;
        OnItemClickListener listener;

        public ViewHolder(final View itemView) {
            super(itemView) ;

            type = itemView.findViewById(R.id.type_comm) ;
            name = itemView.findViewById(R.id.name_comm) ;
            comment = itemView.findViewById(R.id.comment_comm);
            time = itemView.findViewById(R.id.time);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if(listener != null ){
//                        listener.onItemClick(ViewHolder.this, itemView, position);
//                    }
//                }
//            });
        }

        public void setItem(Comment_item item) {
            //type.setId(item.getType());
            //name.setText(item.getName());
            comment.setText(item.getComment());
            //time.setText(item.getTime());
            for(int i = 0; i < mUserList.size(); i++) {
                if(item.getName().equals(mUserList.get(i).getId())){
                    name.setText(mUserList.get(i).getName());
                    String txt_type = mUserList.get(i).getType();

                    switch (txt_type) {
                        case "더위를 많이 타는":
                            type.setImageResource(R.mipmap.fire_icon);
                            break;
                        case "적당한":
                            type.setImageResource(R.mipmap.water_icon);
                            break;
                        case "추위를 많이 타는":
                            type.setImageResource(R.mipmap.ice_icon);
                            break;
                    }


                }
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
    }

    public RecyclerAdapter_Comment(Fragment fragment, ArrayList<Comment_item> items, ArrayList<Memberinfo> mUserList){
        this.fragment = fragment;
        this.items = items;
        this.mUserList = mUserList;
    }
}

