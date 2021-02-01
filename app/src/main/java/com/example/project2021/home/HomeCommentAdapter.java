package com.example.project2021.home;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HomeCommentAdapter extends RecyclerView.Adapter<HomeCommentAdapter.myviewholder> {

    ArrayList<Comment_item> itemlist= new ArrayList<>();;
    ArrayList<Memberinfo> userlist = new ArrayList<>();
    Fragment homeFragment;

    public HomeCommentAdapter(ArrayList<Comment_item> itemlist) {
        this.itemlist = itemlist;
    }

    public HomeCommentAdapter(homeFragment homeFragment, ArrayList<Comment_item> mList, ArrayList<Memberinfo> memberList) {
        this.homeFragment = homeFragment;
        this.itemlist = mList;
        this.userlist = memberList;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item_list,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
//        holder.name.setText(itemlist.get(position).getName());
//        holder.context.setText(itemlist.get(position).getComment());
        Comment_item item = itemlist.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView name,context,date;
        ImageView type;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_comm);
            context = itemView.findViewById(R.id.comment_comm);
            date = itemView.findViewById(R.id.time);
        }

        public void setItem(Comment_item item) {
            context.setText(item.getComment());
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getTime()));

            for(int i = 0; i < userlist.size(); i++) {
                if (item.getName().equals(userlist.get(i).getId())) {
                    name.setText(userlist.get(i).getName());
                    String txt_type = userlist.get(i).getType();
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
    }
}
