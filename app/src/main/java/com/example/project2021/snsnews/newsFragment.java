package com.example.project2021.snsnews;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project2021.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class newsFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    ArrayList<News_item> items= new ArrayList<>();

    NewsAdapter adapter;

    public newsFragment() {
        // Required empty public constructor
    }


    public static newsFragment newInstance() {
        newsFragment tab2 = new newsFragment();
        return tab2;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.recycler_news);

        adapter = new NewsAdapter(items,view.getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        readRss();


        return view;
    }

    void readRss(){

        try {
            URL url=new URL("https://www.yonhapnewstv.co.kr/category/news/weather/feed/");

            RssFeedTask task= new RssFeedTask();
            task.execute(url); //doInBackground()메소드가 발동
        } catch (MalformedURLException e) { e.printStackTrace();}

    }// readRss

    class RssFeedTask extends AsyncTask<URL, Void, String>{
        @Override
        protected String doInBackground(URL... urls) {

            URL url= urls[0];

            try {
                InputStream is= url.openStream();

                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
                XmlPullParser xpp= factory.newPullParser();

                xpp.setInput(is, "utf-8");
                int eventType= xpp.getEventType();

                News_item item= null;
                String tagName= null;

                while (eventType != XmlPullParser.END_DOCUMENT){
                    switch (eventType){
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            tagName=xpp.getName();

                            if(tagName.equals("item")){
                                item= new News_item();
                            }else if(tagName.equals("title")){
                                xpp.next();
                                if(item!=null) item.setTitle(xpp.getText());
                            }else if(tagName.equals("link")){
                                xpp.next();
                                if(item!=null) item.setLink(xpp.getText());
                            }else if(tagName.equals("description")){
                                xpp.next();
                                if(item!=null) item.setDesc(xpp.getText());
                            }else if(tagName.equals("image")){
                                xpp.next();
                                if(item!=null) item.setImgUrl(xpp.getText());
                            }else if(tagName.equals("pubDate")){
                                xpp.next();
                                if(item!=null) item.setDate(xpp.getText());
                            }
                            break;
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tagName=xpp.getName();
                            if(tagName.equals("item")){

                                items.add(item);
                                item=null;

                                //어댑터에게 데이터가 변경되었다는 것을 통지
                                //UI 변경작업-> onProgressUpdate()?
                                publishProgress();

                            }
                            break;
                    }
                    eventType= xpp.next();
                }//while


            } catch (IOException e) {e.printStackTrace();} catch (XmlPullParserException e) {e.printStackTrace();}

            return "파싱종료"; // 리턴 값은 onPostExecute(String s) s에 전달
        }//doIBackground()

        @Override
        protected void onProgressUpdate(Void... values) {  //publishProgress 호출하면 자동으로 실행됨
            super.onProgressUpdate(values);
            //UI변경작업 가능함
            adapter.notifyItemInserted(items.size());
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //adapter.notifyDataSetChanged(); //어댑터에게 데이터 변경 공지

            //이 메소드 안에서는 UI변경 작업 가능
           // Toast.makeText(getContext(), s+":"+items.size(), Toast.LENGTH_SHORT).show();
        }
    }
}