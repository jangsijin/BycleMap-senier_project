package com.example.coha.google.reply;


/**
 * Created by Samchan on 2016-12-22.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Samchan on 2016-11-28.
 */


class ParserRe extends AsyncTask<Void, Integer, Integer> {

    Context c;
    ListView listView_Reply;
    String data;
    ProgressDialog dialog;

    //지워도 되는 부분
    ReplyAdapter adapter;


    int num;
    String id;
    String time;
    String good;
    String text;
    String getBoard_num,getTitle;



    public ParserRe(Context c, ListView board_ListView, ReplyAdapter listViewAdapter, String data) {
        this.listView_Reply = board_ListView;
        this.c = c;
        this.data = data;
        this.adapter = listViewAdapter;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(c);
        dialog.setTitle("데이터(댓글) 파싱하는 중");
        dialog.setMessage("데이터(댓글) 파싱 중입니다. 잠시만 기다려주세요.");
        dialog.show();

        listView_Reply.setAdapter(adapter);
        adapter.clear();

    }

    @Override
    protected Integer doInBackground(Void... params) {

        return this.parse();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("REPLY", "Pase " + integer);
        if (integer == 1) {

            adapter.notifyDataSetChanged();


        } else {
            Toast.makeText(c, "파싱 하지 못했다.", Toast.LENGTH_SHORT).show();
        }


        dialog.dismiss();
    }

    public int parse() {

        try {

            JSONObject results = new JSONObject(data);
            JSONArray jarray = results.getJSONArray("results");   // JSONArray 생성


            for (int i = 0; i < jarray.length(); i++) {
                try {

                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    num = jObject.getInt("num");
                    id = jObject.getString("id");
                    time = jObject.getString("time");
                    good = jObject.getString("good");
                    text = jObject.getString("text");
                    adapter.addItem(new ReplyItem(num, id, time, good, text));

                }catch (Exception E){
                    Log.d("REPLY", "PHP파일이 틀림");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("REPLY", "Json 쿼리틀림");
        }
        return 1;


    }
}

