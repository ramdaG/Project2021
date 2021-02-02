package com.example.project2021.home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2021.R;
import com.example.project2021.board.PostInfo;
import com.example.project2021.home.charts.CustomDialog_Busan;
import com.example.project2021.home.charts.CustomDialog_Chuncheon;
import com.example.project2021.home.charts.CustomDialog_Daegu;
import com.example.project2021.home.charts.CustomDialog_Daejeon;
import com.example.project2021.home.charts.CustomDialog_Gangneung;
import com.example.project2021.home.charts.CustomDialog_Gwangju;
import com.example.project2021.home.charts.CustomDialog_Incheon;
import com.example.project2021.home.charts.CustomDialog_Jeju;
import com.example.project2021.home.charts.CustomDialog_Jeonju;
import com.example.project2021.home.charts.CustomDialog_Pohang;
import com.example.project2021.home.charts.CustomDialog_Seoul;
import com.example.project2021.home.charts.CustomDialog_Suwon;
import com.example.project2021.home.charts.CustomDialog_Ulsan;
import com.example.project2021.home.charts.CustomDialog_Yeosu;
import com.example.project2021.profile.Memberinfo;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.github.mikephil.charting.animation.Easing.*;

public class homeFragment extends Fragment {
    TextView tv_name,tv_temp,tv_maxtemp,tv_mintemp,tv_description,tv_feelslike,tvDate,weatherIcon;
    //ImageView imgWeather;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date,urlIcon;

    View mView;
    AlertDialog.Builder builder;
    Context ct;
    RecyclerView mRecyclerView = null ;
    //RecyclerAdapter_Comment mAdapter = null ;
    HomeCommentAdapter adapter;
    ArrayList<Comment_item> mList;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageView recommend;
    FloatingActionButton actionButton;
    CustomDialog dlg;

    PieChart pieChart;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    FirebaseUser firebaseUser;
    FirebaseFirestore mFirestore;
    //private ArrayList<Comment_item> commentList;
    private ArrayList<Memberinfo> memberList;
    String mCoat = "Coat",mLong = "Long",mShort = "Short";
    int a,b,c;

    int[] color = new int[]{ R.color.blue_1,
            R.color.blue_2,R.color.blue_3};

    TextView vote;
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //mRecyclerView= getActivity().findViewById(R.id.user_recyclerView);
        //RecyclerAdapter_Comment adapter = new RecyclerAdapter_Comment(mList);
        //mRecyclerView.setAdapter(adapter);

//        vote = getActivity().findViewById(R.id.txt_vote);
//        vote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomDialog dlg = new CustomDialog(ct);
//                dlg.show();
//            }
//        });

        tv_name = view.findViewById(R.id.tv_name);
        tv_temp = view.findViewById(R.id.tv_temp);
        tv_description = view.findViewById(R.id.tv_desc);
        tv_maxtemp = view.findViewById(R.id.tv_tempMax);
        tv_mintemp = view.findViewById(R.id.tv_tempMin);
        tv_feelslike = view.findViewById(R.id.tv_feelslike);
        tvDate = view.findViewById(R.id.tv_date);
        actionButton = view.findViewById(R.id.fab_home);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog_Incheon dlg = new CustomDialog_Incheon(ct);
                dlg.show();
            }
        });

        weatherIcon = view.findViewById(R.id.text_weatherIcon);
        recommend = view.findViewById(R.id.img_recommend);

        mView = view.findViewById(R.id.view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //현재 시간
        Thread t = new Thread(){

            @Override
            public void run() {
                try {
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        if(getActivity() == null){return;}
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                calendar = Calendar.getInstance();
                                simpleDateFormat = new SimpleDateFormat("MM월 dd일 HH:mm");
                                date = simpleDateFormat.format(calendar.getTime());
                                tvDate.setText(date);
                            }
                        });
                    }
                }catch (InterruptedException e){}
            }
        };
        t.start();

        CommentUpdate();

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initDataset();
        //CommentUpdate();


    }

    private void CommentUpdate() {
//        mFirestore.collection("comments_Incheon").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                        for(DocumentSnapshot d:list){
//                        Comment_item obj = d.toObject(Comment_item.class);
//                        mList.add(obj);
//                        }
//                        // update Adapter
//                        adapter.notifyDataSetChanged();
//                    }
//                });
        mList = new ArrayList<>();  //PostList
        //commentList = new ArrayList<>();
        memberList = new ArrayList<>();
        adapter = new HomeCommentAdapter(homeFragment.this,mList,memberList);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            CollectionReference collectionReference = mFirestore.collection("users");
            collectionReference
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("homefragment", document.getId() + " => " + document.getData());
                            final Memberinfo memberinfo = new Memberinfo(
                                    document.getString("name"),
                                    document.getString("type"),
                                    document.getId());
                            memberList.add(memberinfo);
                        }
                    }
                }
            });

        CollectionReference ref = mFirestore.collection("comments_Incheon");
        ref
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("home", document.getId() + " => " + document.getData());
                        final Comment_item commentItem = new Comment_item(
                                //postList.add(postInfo = new PostInfo(
                                document.getString("user"),
                                document.getString("content"),
                                new Date(document.getDate("date").getTime()));
                                //document.getId());
                        mList.add(commentItem);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        }
    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        mRecyclerView = view.findViewById(R.id.user_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        //새로고침
        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.homeRefreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.commit();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 500);

            }
        });

        mList = new ArrayList<>();
        //mAdapter = new RecyclerAdapter_Comment(mList);
        adapter = new HomeCommentAdapter(mList);
        //mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //mAdapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();

        //piechart
        pieChart = view.findViewById(R.id.pieChart);

        //database = FirebaseDatabase.getInstance();
        //myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
//                if(snapshot.hasChildren()){
//
//                }
                Log.d("countA_main",""+snapshot.child("Charts").child(mCoat).getChildrenCount());
                Log.d("countB_main",""+snapshot.child("Charts").child(mLong).getChildrenCount());
                Log.d("countC_main",""+snapshot.child("Charts").child(mShort).getChildrenCount());

                a = (int) snapshot.child("Charts").child("incheon").child(mCoat).getChildrenCount();
                b = (int) snapshot.child("Charts").child("incheon").child(mLong).getChildrenCount();
                c = (int) snapshot.child("Charts").child("incheon").child(mShort).getChildrenCount();

                drawChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //pieDataSet.setColors(colors);


        new MyTask().execute("37.453609","126.731667"); //날씨 표시 시작

        registerAlarm(ct);

        CommentUpdate();


        return view;
    }

    private void drawChart() {
        PieDataSet pieDataSet = new PieDataSet(data1(),"chart");
        pieDataSet.setColors(color,ct);
        pieDataSet.setSliceSpace(3);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setUsePercentValues(true);
        pieData.setValueTextSize(10);
        pieChart.setEntryLabelTextSize(12);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleRadius(0);
        pieChart.animateY(1000, EaseInOutCubic);
        pieChart.setData(pieData);
        pieChart.invalidate();

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
    }

//    public void registerAlarm(Context ct) {
//        Intent intent = new Intent(getActivity(),AlarmReceiver.class);
//        PendingIntent sender = PendingIntent.getBroadcast(getActivity(),0,intent,0);
//        try{
//            Date tomorrow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse("2021-01-27 00:00:00");
//            AlarmManager am = (AlarmManager) ct.getSystemService(Context.ALARM_SERVICE);
//            am.setInexactRepeating(AlarmManager.RTC, tomorrow.getTime(),24*60*60*1000,sender);
//        } catch (java.text.ParseException e){
//            e.printStackTrace();
//        }
//    }

    public static void registerAlarm(Context context){
        AlarmManager resetAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context,AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,resetIntent,0);

        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 0);
        resetCal.set(Calendar.MINUTE,0);
        resetCal.set(Calendar.SECOND, 0);

        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY,sender);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis()+AlarmManager.INTERVAL_DAY));

        Log.d("resetAlarm", "ResetHour : " + setResetTime);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        CommentUpdate();
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(dlg != null && dlg.isShowing()){
            dlg.dismiss();
        }
    }

//    private void initDataset() {
//        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
//        mList = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//           mList.add(new Comment_item(R.id.img_type, "박소현","오늘너무추워요가나다라마바사아자차카", currentTime));
//        }
//    }

    private ArrayList<PieEntry> data1(){
        ArrayList<PieEntry> datavalue = new ArrayList<>();

        datavalue.add(new PieEntry(a,"코트"));
        datavalue.add(new PieEntry(b,"롱패딩"));
        datavalue.add(new PieEntry(c,"숏패딩"));
        Log.d("PieEntry",""+(a+b+c));
       // System.out.println(dataChart.getaValue()+"piedataValue");

        return datavalue;
    }

    public class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://api.openweathermap.org/data/2.5/weather?"
                        + "lat=" + strings[0] + "&lon=" + strings[1] + "&units=metric&&lang=kr"
                        + "&appid=685e3251dbe08d48d31e278a59f0cfc2";

                URL url = new URL(urlstr);

                BufferedReader bf;
                String line;
                String result = "";

                //날씨 정보를 받아옴
                bf = new BufferedReader(new InputStreamReader(url.openStream()));

                while ((line = bf.readLine()) != null) {
                    result = result.concat(line);
                    System.out.println(result);
                }


                return result;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = null;
            try {
                jsonObj = (JSONObject) jsonParser.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //System.out.println("jsonObj?"+jsonObj);


            tv_name.setText(""+jsonObj.get("name")); //지역

            JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
            JSONObject obj = (JSONObject) weatherArray.get(0);

            tv_description.setText(""+obj.get("description"));

            JSONObject mainArray = (JSONObject) jsonObj.get("main");

            String strTemp = ""+mainArray.get("temp");

            if(strTemp.contains(".")){
                int idx = strTemp.indexOf(".");
                String resultTemp = strTemp.substring(0,idx);
                tv_temp.setText(resultTemp+"º");
            }
            else{
                tv_temp.setText(mainArray.get("temp")+"º");
            }

            //기본 추천 아이템
            double mainTemp = Double.parseDouble(strTemp);
            if (mainTemp >= 4){
                recommend.setImageResource(R.mipmap.coat2);}
            else if (mainTemp > -2 && mainTemp < 4){
                recommend.setImageResource(R.mipmap.short2); }
            else if (mainTemp <= -2){
                recommend.setImageResource(R.mipmap.long2);
            }


            tv_maxtemp.setText(mainArray.get("temp_max")+"º");
            tv_mintemp.setText(mainArray.get("temp_min")+"º");
            tv_feelslike.setText(mainArray.get("feels_like")+"º");

            String iconValue = setWeatherIcon(""+obj.get("id"));
            String iconStr = Html.fromHtml(iconValue).toString();
            weatherIcon.setText(iconStr);


            /*  기본 아이콘
            urlIcon = "http://openweathermap.org/img/wn/"+obj.get("icon")+"@2x.png";
            Glide.with(getActivity()).load(urlIcon).placeholder(R.mipmap.cloudy)
                    .error(R.mipmap.cloudy)
                    .into(imgWeather);
            */

        }
    }

    private String setWeatherIcon(String id) {
        int actualId = Integer.parseInt(id)/100;
        String icon = "";
        switch (actualId){
            case 2 : icon = "&#xf01e"; break;
            case 3 : icon = "&#xf01c"; break;
            case 5 : icon = "&#xf019"; break;
            case 6 : icon = "&#xf01b"; break;
            case 7 : icon = "&#xf014"; break;
            case 8 : icon = "&#xf00d"; break;
        }
        return icon;
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("지역 선택");


        String[] cities = {"서울","인천","수원","부산","울산","광주","대구","대전","춘천","제주","전주","포항","강릉","여수"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0 : new MyTask().execute("37.56826","126.977829");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Seoul dlg_0 = new CustomDialog_Seoul(ct);
                                dlg_0.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("seoul").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("seoul").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("seoul").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                         break;
                    case 1 : new MyTask().execute("37.453609","126.731667");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Incheon dlg_1 = new CustomDialog_Incheon(ct);
                                dlg_1.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("incheon").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("incheon").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("incheon").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 2 : new MyTask().execute("37.291","127.008");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Suwon dlg_2 = new CustomDialog_Suwon(ct);
                                dlg_2.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("suwon").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("suwon").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("suwon").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 3 : new MyTask().execute("35.728062","126.731941");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Busan dlg_3 = new CustomDialog_Busan(ct);
                                dlg_3.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("busan").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("busan").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("busan").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 4 : new MyTask().execute("35.53722","129.316666");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Ulsan dlg_4 = new CustomDialog_Ulsan(ct);
                                dlg_4.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("ulsan").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("ulsan").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("ulsan").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 5 : new MyTask().execute("37.41","127.257");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Gwangju dlg_5 = new CustomDialog_Gwangju(ct);
                                dlg_5.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Gwangju").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Gwangju").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Gwangju").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 6 : new MyTask().execute("35.870","128.591");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Daegu dlg_6 = new CustomDialog_Daegu(ct);
                                dlg_6.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Daegu").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Daegu").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Daegu").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 7 : new MyTask().execute("36.321","127.419");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Daejeon dlg_7 = new CustomDialog_Daejeon(ct);
                                dlg_7.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Daejeon").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Daejeon").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Daejeon").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 8 : new MyTask().execute("37.874","127.734");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Chuncheon dlg_8 = new CustomDialog_Chuncheon(ct);
                                dlg_8.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Chuncheon").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Chuncheon").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Chuncheon").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 9 : new MyTask().execute("33.509","126.521");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Jeju dlg_9 = new CustomDialog_Jeju(ct);
                                dlg_9.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Jeju").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Jeju").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Jeju").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 10 : new MyTask().execute("35.821","127.148");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Jeonju dlg_10 = new CustomDialog_Jeonju(ct);
                                dlg_10.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Jeonju").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Jeonju").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Jeonju").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 11 : new MyTask().execute("36.032","129.365");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Pohang dlg_11 = new CustomDialog_Pohang(ct);
                                dlg_11.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Pohang").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Pohang").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Pohang").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 12 : new MyTask().execute("37.755","128.896");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Gangneung dlg_12 = new CustomDialog_Gangneung(ct);
                                dlg_12.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Gangneung").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Gangneung").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Gangneung").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                    case 13 : new MyTask().execute("34.744","127.737");
                        actionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CustomDialog_Yeosu dlg_13 = new CustomDialog_Yeosu(ct);
                                dlg_13.show();
                            }
                        });
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                                a = (int) snapshot.child("Charts").child("Yeosu").child(mCoat).getChildrenCount();
                                b = (int) snapshot.child("Charts").child("Yeosu").child(mLong).getChildrenCount();
                                c = (int) snapshot.child("Charts").child("Yeosu").child(mShort).getChildrenCount();

                                drawChart();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    break;
                }
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}
