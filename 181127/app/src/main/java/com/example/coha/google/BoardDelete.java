package com.example.coha.google;

/**
 * Created by Samchan on 2017-01-18.
 */


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시물을 지우는 직접적인 함수. 삭제 확인 다이얼로그에서 확인 버튼을 누르면 이 함수가 실행된다.
 * **/
public class BoardDelete extends AsyncTask<String, Integer, String> {
    Context mContext;
    BoardItem item;
    ListView board_ListView;
    ListViewAdapter adapter;
    int position_g;
    Handler handler;

    public BoardDelete() {
    }

    public BoardDelete(Context context, BoardItem item , ListView board_ListView, ListViewAdapter adapter, int position_g) {
        this.mContext = context;
        this.item = item;
        this.board_ListView = board_ListView;
        this.adapter = adapter;
        this.position_g = position_g;
    }

    @Override
    protected void onPreExecute() {

        item = (BoardItem) board_ListView.getItemAtPosition(position_g);

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //int position = Integer.parseInt(params[0]); execute부분에서 삭제해서 지움.

        //클릭 아이템이 존재할 시 실행
        if (item != null) {

            try {
                HttpClient httpC = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_delete.php"); // 이 php에는 db에서 게시물 하나를 지우는 코드가 저장되어있다.

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("num", item.getData(0)));
                nameValuePairs.add(new BasicNameValuePair("boardToken", item.getData(7)));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //바로 list의 게시물을 하나 지우고 번호를 position값을 하나 빼준 뒤,
                //새로고침을해준다.
                adapter.removeItem(position_g - 1);
                adapter.notifyDataSetChanged(); //새로고침
            }
        }, 0);

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}