package com.example.project2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList <Post_item> items = new ArrayList<>();

    RecyclerAdapter(ArrayList<Post_item> list) {
        this.items = list ;
    }
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.post_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        Post_item item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    public  void addItem(Post_item item){
        items.add(item);
    }

    public void addItems(ArrayList<Post_item> items){
        this.items = items;
    }

    public  Post_item getItem(int position){
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
        protected ImageView profile ;
        protected TextView name ;
        protected TextView add ;
        protected TextView text;
        OnItemClickListener listener;

        public ViewHolder(final View itemView) {
            super(itemView) ;

            profile = itemView.findViewById(R.id.img_profile) ;
            name = itemView.findViewById(R.id.txt_name) ;
            add = itemView.findViewById(R.id.txt_address) ;
            text = itemView.findViewById(R.id.txt_post);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null ){
                        listener.onItemClick(ViewHolder.this, itemView, position);
                    }
                }
            });
        }

        public void setItem(Post_item item) {
            profile.setId(item.getProfile());
            name.setText(item.getName());
            add.setText(item.getAddress());
            text.setText(item.getText());
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
    }
}


