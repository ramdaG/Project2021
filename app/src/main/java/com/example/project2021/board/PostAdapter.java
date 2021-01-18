package com.example.project2021.board;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.GalleryViewHolder> {
    private ArrayList<PostInfo> mDataset = null;
    private Fragment fragment;

    static class GalleryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        GalleryViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public PostAdapter(Fragment fragment, ArrayList<PostInfo> mDataset) {
        this.mDataset = mDataset;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public PostAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);

        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.txt_post);
        textView.setText(mDataset.get(position).getContents());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
