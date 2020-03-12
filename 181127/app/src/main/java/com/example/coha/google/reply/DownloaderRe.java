package com.example.coha.google.reply;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samchan on 2016-11-28.
 */

public class DownloaderRe extends AsyncTask<Void, Integer, String> {


    Context c;
    String address;
    ListView listView_Reply;
    ReplyAdapter adapter;

    String boardToken;
    String line;

    ProgressDialog dialog;


    public DownloaderRe(Context c, String address, ListView listView_Reply, ReplyAdapter listViewAdapter,String boardToken) {
        this.address = address;
        this.c = c;
        this.listView_Reply = listView_Reply;
        this.adapter = listViewAdapter;
        this.boardToken = boardToken;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(c);
        dialog.setTitle("Fetch Data");
        dialog.setMessage("Fetch Data...Please wait");
        dialog.show();
    }


    @Override
    protected String doInBackground(Void... params) {
        Log.d("REPLY", "DownLoad!! : doInBack start");

        //댓글을 읽어오는 함수 호출
        String data = downloadData();

        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("REPLY", "S!! : " + s);
        Log.d("REPLY", "*****************");
        dialog.dismiss();

        if (s != null) {
            ParserRe p = new ParserRe(c, listView_Reply,adapter, s);
            p.execute();

        } else {
            Toast.makeText(c, "다운로드 하지 못했다.", Toast.LENGTH_SHORT).show();
        }


    }

    private String downloadData() {
        try {
            HttpClient httpC = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(address);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("boardToken", boardToken));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                    "UTF-8"));
            httpC.execute(httppost);

            HttpResponse response = httpC.execute(httppost);
            String getLine = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            line = getLine;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR1";
        }

        return line;
    }


}
