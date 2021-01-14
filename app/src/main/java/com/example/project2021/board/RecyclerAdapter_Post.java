package com.example.project2021.board;

import android.content.Context;
import android.media.Image;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;

import java.util.ArrayList;

public class RecyclerAdapter_Post extends RecyclerView.Adapter<RecyclerAdapter_Post.ViewHolder> {
    static int HeartNum = 0;
    private ArrayList <Post_item> items = new ArrayList<>();

    public RecyclerAdapter_Post(ArrayList<Post_item> list) {
        this.items = list ;
    }
    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    @Override
    public RecyclerAdapter_Post.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.post_item_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Post.ViewHolder holder, int position) {
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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView profile ;
        protected TextView name ;
        protected TextView add ;
        protected TextView text;
        OnItemClickListener listener;
        ImageButton img_heart;

        public ViewHolder(final View itemView) {
            super(itemView) ;

            profile = itemView.findViewById(R.id.img_profile) ;
            name = itemView.findViewById(R.id.txt_name) ;
            add = itemView.findViewById(R.id.txt_address) ;
            text = itemView.findViewById(R.id.txt_post);
            img_heart = itemView.findViewById(R.id.img_heart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ImageButton heartButton = v.getRootView().findViewById(R.id.img_heart);
                    heartButton.setSelected(true);
                    if(listener != null ){
                        listener.onItemClick(ViewHolder.this, itemView, position);
                    }
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


