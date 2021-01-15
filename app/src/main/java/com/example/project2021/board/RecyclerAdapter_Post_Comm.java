package com.example.project2021.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.example.project2021.home.Comment_item;

import java.util.ArrayList;

public class RecyclerAdapter_Post_Comm extends RecyclerView.Adapter<RecyclerAdapter_Post_Comm.ViewHolder> {

    private ArrayList<Post_Comm_item> items = new ArrayList<>();

    RecyclerAdapter_Post_Comm(ArrayList<Post_Comm_item> list) {
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
        View itemView = inflater.inflate(R.layout.postcomm_item_list, parent, false);

        return new RecyclerAdapter_Post_Comm.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Post_Comm.ViewHolder holder, int position) {
        Post_Comm_item item = items.get(position);
        holder.setItem(item);
        holder.setOnItemClickListener(listener);
    }

    public void addItem(Post_Comm_item item) {
        items.add(item);
    }

    public void addItems(ArrayList<Post_Comm_item> items) {
        this.items = items;
    }

    public Post_Comm_item getItem(int position) {
        return items.get(position);
    }


    public void changeItem(Post_Comm_item item, int position){
        items.set(position,item);
        notifyItemChanged(position);
    }


    public void setOnItemClickListener(RecyclerAdapter_Post_Comm.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView type;
        protected TextView name;
        protected TextView comment;
        protected TextView time;
        RecyclerAdapter_Post_Comm.OnItemClickListener listener;
        private int prePosition = -1;

        public ViewHolder(final View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.type);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            time = itemView.findViewById(R.id.time);

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

        public void setItem(Post_Comm_item item) {
            type.setId(item.getType());
            name.setText(item.getName());
            comment.setText(item.getComment());
            time.setText(item.getTime());
        }

        public void setOnItemClickListener(RecyclerAdapter_Post_Comm.OnItemClickListener listener) {
            this.listener = listener;
        }
    }
}