package com.example.project2021.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2021.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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
import java.util.Locale;

import static com.github.mikephil.charting.animation.Easing.*;

public class homeFragment extends Fragment {
    TextView tv_name,tv_temp,tv_maxtemp,tv_mintemp,tv_description,tv_feelslike,tvDate;
    ImageView imgWeather;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;

    View mView;
    AlertDialog.Builder builder;

    Context ct;
    RecyclerView mRecyclerView = null ;
    RecyclerAdapter_Comment mAdapter = null ;
    ArrayList<Comment_item> mList;
    private RecyclerView.LayoutManager mLayoutManager;
    ImageView recommend;

    PieChart pieChart;
    int[] color = new int[]{ R.color.blue_1,
            R.color.blue_2,R.color.blue_3};

    TextView vote;
    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView= getActivity().findViewById(R.id.recycler);
        RecyclerAdapter_Comment adapter = new RecyclerAdapter_Comment(mList);
        mRecyclerView.setAdapter(adapter);

        vote = getActivity().findViewById(R.id.txt_vote);
        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog dlg = new CustomDialog(ct);
                dlg.show();
            }
        });

        tv_name = view.findViewById(R.id.tv_name);
        tv_temp = view.findViewById(R.id.tv_temp);
        tv_description = view.findViewById(R.id.tv_desc);
        tv_maxtemp = view.findViewById(R.id.tv_tempMax);
        tv_mintemp = view.findViewById(R.id.tv_tempMin);
        tv_feelslike = view.findViewById(R.id.tv_feelslike);
        imgWeather = view.findViewById(R.id.img_weather);
        tvDate = view.findViewById(R.id.tv_date);

        mView = view.findViewById(R.id.view);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        //현재 시간
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM월 dd일 HH:mm");
        date = simpleDateFormat.format(calendar.getTime());
        tvDate.setText(date);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();

    }


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ct = container.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerAdapter_Comment(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        //piechart
        pieChart = view.findViewById(R.id.pieChart);

        PieDataSet pieDataSet = new PieDataSet(data1(),"chart");
        pieDataSet.setColors(color,getActivity());
        pieDataSet.setSliceSpace(3);
        //pieDataSet.setColors(colors);

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


        //기본 추천아이템
        recommend = view.findViewById(R.id.img_recommend);

        int weather = 0;
        if (weather >= 6 && weather <= 9){
            recommend.setImageResource(R.mipmap.coat2);}
        else if (weather >= -1 && weather <= 5){
            recommend.setImageResource(R.mipmap.short2); }
        else if (weather <= -2){
            recommend.setImageResource(R.mipmap.long2);
        }

        new MyTask().execute("37.453609","126.731667"); //날씨 표시 시작


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initDataset() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        mList = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            mList.add(new Comment_item(R.id.img_type, "박소현","오늘너무추워요가나다라마바사아자차카", currentTime));
        }
    }

    private ArrayList<PieEntry> data1(){
        ArrayList<PieEntry> datavalue = new ArrayList<>();

        datavalue.add(new PieEntry(30,"롱패딩"));
        datavalue.add(new PieEntry(50,"숏패딩"));
        datavalue.add(new PieEntry(20,"코트"));

        return datavalue;
    }

    public class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                String urlstr = "http://api.openweathermap.org/data/2.5/weather?"
                        + "lat=" + strings[0] + "&lon=" + strings[1] + "&units=metric&&lang=kr"
                        + "&appid=685e3251dbe08d48d31e278a59f0cfc2";
//
//                String urlstr = "http://api.openweathermap.org/data/2.5/weather?"
//                        + "id="+ id + "&units=metric&lang=kr"
//                        + "&appid=685e3251dbe08d48d31e278a59f0cfc2";
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
            System.out.println("jsonObj?"+jsonObj);


            tv_name.setText(""+jsonObj.get("name")); //지역

            JSONArray weatherArray = (JSONArray) jsonObj.get("weather");
            JSONObject obj = (JSONObject) weatherArray.get(0);

            tv_description.setText(""+obj.get("description"));
            //System.out.println("icon "+obj.get("icon"));  //이건 어케가져와

            JSONObject mainArray = (JSONObject) jsonObj.get("main");

            System.out.println(mainArray);
            System.out.println("기온?"+mainArray.get("temp"));

            tv_temp.setText(mainArray.get("temp")+"º");
            tv_maxtemp.setText(mainArray.get("temp_max")+"º");
            tv_mintemp.setText(mainArray.get("temp_min")+"º");
            tv_feelslike.setText(mainArray.get("feels_like")+"º");


        }
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("지역 선택");


        String[] cities = {"서울","인천","수원","부산","울산","광주","대구","대전","춘천","제주","전주","포항","강릉","여수"};
        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0 : new MyTask().execute("37.56826","126.977829"); break;
                    case 1 : new MyTask().execute("37.453609","126.731667"); break;
                    case 2 : new MyTask().execute("37.291","127.008"); break;
                    case 3 : new MyTask().execute("35.728062","126.731941"); break;
                    case 4 : new MyTask().execute("35.53722","129.316666"); break;
                    case 5 : new MyTask().execute("37.41","127.257"); break;
                    case 6 : new MyTask().execute("35.870","128.591"); break;
                    case 7 : new MyTask().execute("36.321","127.419"); break;
                    case 8 : new MyTask().execute("37.874","127.734"); break;
                    case 9 : new MyTask().execute("33.509","126.521"); break;
                    case 10 : new MyTask().execute("35.821","127.148"); break;
                    case 11 : new MyTask().execute("36.032","129.365"); break;
                    case 12 : new MyTask().execute("37.755","128.896"); break;
                    case 13 : new MyTask().execute("34.744","127.737"); break;
                }
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}
