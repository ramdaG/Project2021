package com.example.project2021;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList <Post_item> mList = null;

    RecyclerAdapter(ArrayList<Post_item> list) {
        this.mList = list ;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_list, parent, false);
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
       holder.profile.setImageResource(mList.get(position).img_profile);
       holder.name.setText(mList.get(position).nameStr);
       holder.add.setText(mList.get(position).addressStr);
       holder.text.setText(mList.get(position).textStr);

    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView profile ;
        protected TextView name ;
        protected TextView add ;
        protected TextView text;

        ViewHolder(View itemView) {
            super(itemView) ;

            profile = itemView.findViewById(R.id.img_profile) ;
            name = itemView.findViewById(R.id.txt_name) ;
            add = itemView.findViewById(R.id.txt_address) ;
            text = itemView.findViewById(R.id.txt_post);
        }
    }

}