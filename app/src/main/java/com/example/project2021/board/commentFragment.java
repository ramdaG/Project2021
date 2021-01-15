package com.example.project2021.board;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2021.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

public class commentFragment extends Fragment {

    RecyclerView recyclerView = null;
    RecyclerAdapter_Post_Comm mAdapter = null;
    ArrayList<Post_Comm_item> mList;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        EditText commText = view.findViewById(R.id.editText);

        TextView commName = view.findViewById(R.id.comm_txt_name);
        String Comment_Name = commName.getText().toString();

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        mList = new ArrayList<Post_Comm_item>();

        Button Comment_Save = view.findViewById(R.id.comm_save);
        Comment_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Comment_Text = commText.getText().toString();
                Toast.makeText(getActivity(), "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                mList.add(new Post_Comm_item(R.id.img_type, Comment_Name, Comment_Text, currentTime));
                //mList.add(new Post_Comm_item(R.id.img_type, Comment_Name, "안녕", currentTime));
                mAdapter.notifyDataSetChanged();
                commText.setText("");
            }
        });

        /*
        for(int i = 0; i < 10; i++){
            mList.add(new Post_Comm_item(R.id.img_type, Comment_Name, Comment_Text, currentTime));
        }
        */
        mAdapter = new RecyclerAdapter_Post_Comm(mList);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}
