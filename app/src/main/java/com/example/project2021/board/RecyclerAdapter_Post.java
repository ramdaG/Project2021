package com.example.project2021.board;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RecyclerAdapter_Post extends RecyclerView.Adapter<RecyclerAdapter_Post.ViewHolder> {
    static int HeartNum = 0;
    private ArrayList <Post_item> items = new ArrayList<>();
    private OnItemLongClickListener mLongListener = null;

    public RecyclerAdapter_Post(ArrayList<Post_item> list) {
        this.items = list ;
    }

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    @Override
    public RecyclerAdapter_Post.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.post_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Post.ViewHolder holder, int position) {
        Post_item item = items.get(position);
        holder.setItem(item);
        holder.setOnItemLongClickListener(mLongListener);
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

    public void setOnItemLongClicklistener(OnItemLongClickListener onItemLongClickListener) {
        this.mLongListener = onItemLongClickListener;
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View v, int pos);
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }

    public void onItemLongClick(View view, int position) {
        if(mLongListener != null){
            mLongListener.onItemLongClick(view,position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView profile ;
        protected TextView name ;
        protected TextView add ;
        protected TextView text;
        RecyclerAdapter_Post.OnItemLongClickListener listener;
        ImageButton img_heart;
        ImageButton comment_Num;

        public ViewHolder(final View itemView) {
            super(itemView) ;

            profile = itemView.findViewById(R.id.img_profile) ;
            name = itemView.findViewById(R.id.txt_name) ;
            add = itemView.findViewById(R.id.txt_address) ;
            text = itemView.findViewById(R.id.addinfo2);
            img_heart = itemView.findViewById(R.id.img_heart);
            comment_Num = itemView.findViewById(R.id.img_com);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION ){
                        if (mLongListener != null){
                            mLongListener.onItemLongClick(v, position);
                        }
                        notifyItemChanged(position);
                    }
                    return true;
                }
            });

            TextView txtHeartNum = itemView.findViewById(R.id.txt_HeartNum);
            img_heart.setOnClickListener(new View.OnClickListener() {
                int count = 0;
                @Override
                public void onClick(View v) {
                    if (count == 0) {
                        img_heart.setImageResource(R.drawable.heart_click);
                        count = 1;
                        HeartNum += 1;
                    }
                    else if (count == 1){
                        img_heart.setImageResource(R.drawable.heart);
                        count = 0;
                        HeartNum -= 1;
                    }
                    txtHeartNum.setText(""+HeartNum);
                }
            });

            comment_Num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), commentActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void setItem(Post_item item) {
            profile.setId(item.getProfile());
            name.setText(item.getName());
            add.setText(item.getAddress());
            text.setText(item.getText());
        }

        public void setOnItemLongClickListener(RecyclerAdapter_Post.OnItemLongClickListener listener) {
            this.listener = listener;
        }
    }
}


