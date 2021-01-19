package com.example.project2021.board;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.GalleryViewHolder> {
    private ArrayList<PostInfo> mDataset = null;
    private Fragment fragment;
    private String address, name, type;

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
        TextView textView = cardView.findViewById(R.id.text_post);
        textView.setText(mDataset.get(position).getContents());

        TextView createdAtTextView = cardView.findViewById(R.id.CreatedAtTextView);
        createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));




        //닉네임, 주소, 타입 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            address = document.getString("address");
                            TextView Text_address = cardView.findViewById(R.id.txt_address_post);
                            Text_address.setText(address);

                            name = document.getString("name");
                            TextView Text_name = cardView.findViewById(R.id.txt_name_post);
                            Text_name.setText(name);

                            type = document.getString("type");
                            ImageView img_type = cardView.findViewById(R.id.img_type_post);
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
                        }
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
