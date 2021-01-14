package com.example.project2021.snsnews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2021.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter {
    ArrayList<News_item> items;
    Context context;

    public NewsAdapter(ArrayList<News_item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.news_item_list,parent,false);

        VH vh= new VH(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh= (VH)holder;

        News_item item= items.get(position);
        vh.tvTitle.setText(item.getTitle());
        vh.tvDesc.setText(item.getDesc());
        vh.tvDate.setText(item.getDate());

        if(item.getImgUrl()==null){ //이미지 없음
            vh.iv.setVisibility(View.GONE);
        }else{
            vh.iv.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getImgUrl()).into(vh.iv);

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDesc, tvDate;
        ImageView iv;

        public VH(@NonNull View itemView) {
            super(itemView);

            tvTitle=itemView.findViewById(R.id.news_title);
            tvDesc=itemView.findViewById(R.id.news_desc);
            tvDate=itemView.findViewById(R.id.news_date);
            iv=itemView.findViewById(R.id.news_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link = items.get(getLayoutPosition()).getLink();

                    Intent intent = new Intent(context, NewsItemActivity.class);
                    intent.putExtra("Link",link);
                    context.startActivity(intent);
                    //startActivity();
                }
            });
        }
    }
}
