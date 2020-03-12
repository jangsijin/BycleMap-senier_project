package com.example.coha.google;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setTitle("공지사항");


        noticeListView = (ListView) findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        noticeList.add(new Notice("첫번째 공지사항입니다.","관리자","2018-11-15"));
        noticeList.add(new Notice("두번째 공지사항입니다.","관리자","2018-11-15"));
        adapter = new NoticeListAdapter(getApplicationContext(),noticeList);
        noticeListView.setAdapter(adapter);
        new BackgroundTask().execute();


    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {

        String target;

        protected void onPreExecute(){
            target ="http://ehdntjr123.dothome.co.kr/NoticeList.php";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(temp+"Wn");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }



        public void onProgressUpdate(Void...values){
            super.onProgressUpdate();
        }

        public void onPostExecute(String result){
            try{
                JSONObject jsonObject = new JSONObject(result);
                JSONArray  jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String noticeContent,noticeName,noticeDate;
                while (count< jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");
                    Notice notice = new Notice(noticeContent,noticeName,noticeDate);
                    noticeList.add(notice);
                    adapter.notifyDataSetChanged();
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

