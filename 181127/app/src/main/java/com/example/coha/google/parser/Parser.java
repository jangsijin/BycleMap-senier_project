package com.example.coha.google.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.coha.google.BoardItem;
import com.example.coha.google.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.example.coha.google.MainBoardActivity.pageBtnNum;

/**
 * Created by Samchan on 2016-11-16.
 */

public class Parser extends AsyncTask<Void, Integer, Integer> {

    Context context;
    ListView board_ListView;
    String data;
    ProgressDialog dialog;

    //지워도 되는 부분
    ListViewAdapter adapter;


    int boardCount;
    int Count;
    int firstNum;
    int lastNum;


    int num;
    String title;
    String date;
    String id;
    String good;
    String memo;
    String boardToken;
    String goodcheck;


    public static int boardItemNum;


    public Parser(Context c, ListView board_ListView, ListViewAdapter listViewAdapter, String data) {
        this.board_ListView = board_ListView;
        this.context = c;
        this.data = data;
        this.adapter = listViewAdapter;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setTitle("데이터(게시물) 파싱하는 중");
        dialog.setMessage("데이터(게시물) 파싱 중입니다. 잠시만 기다려주세요.");
        dialog.show();
        Log.d("TEST", "Adapter 시작");


        board_ListView.setAdapter(adapter);
        adapter.clear();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        adapter.clear();
        return this.parse();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.d("TEST", "Pase " + integer);
        if (integer == 1) {
            Log.d("TEST", "*****************");
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(context, "파싱 하지 못했다.", Toast.LENGTH_SHORT).show();
        }


        dialog.dismiss();
    }

    public int parse() {

        try {

            JSONObject results = new JSONObject(data);
            JSONArray jarray = results.getJSONArray("results");   // JSONArray 생성
            Log.d("TEST", "" + jarray);
            Log.d("adapter", "이 부분" + adapter.getCount());
            boardItemNum = jarray.length();
            Log.d("Search", "가져온 갯수" + boardItemNum);
            //////////////////////////

            pageNumCal();

            //////////////////////////

            int boardTopNum = firstNum;
            int boardLastNum = lastNum;


            Log.d("page", " 넘어온 FirstNum : " + firstNum);
            Log.d("page", "넘어온 LastNum  : " + lastNum);

            publishProgress();

            if (firstNum < 0) {

                title = "페이지에 게시물이 없습니다.";
                adapter.addItem(new BoardItem(num, title, date, id, good, memo,goodcheck,boardToken));

            } else {

                for (int i = boardTopNum; i >= boardLastNum; i--)
                //for(int i=0; i<jarray.length();i++)
                {
                    try {
                        JSONObject jObject = null;
                        jObject = jarray.getJSONObject(i);  // JSONObject 추출

                        num = jObject.getInt("num");
                        title = jObject.getString("title");
                        date = jObject.getString("date");
                        id = jObject.getString("id");
                        good = jObject.getString("good");
                        memo = jObject.getString("memo");
                        goodcheck =jObject.getString("goodcheck");
                        boardToken =jObject.getString("boardToken");

                        Log.d("TEST", "////////////");
                        adapter.addItem(new BoardItem(num, title, date, id, good, memo,goodcheck,boardToken));
                        Log.d("TEST", "parser에서 불러온" + i + " 번 째 값 : "
                                + num + "//" + title + "//" + date + "//" + id + "//" + good + "//" + goodcheck +"//"+boardToken+ "\n");

                    } catch (Exception E) {
                        Log.d("TEST", "PHP파일이 틀림");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TEST", "Json 쿼리틀림ㅋㅋㅋㅋㅋㅋ");
        }
        return 1;


    }

    public void pageNumCal() {

        boardCount = boardItemNum; // 총 게시물은 몇개인가.

//        firstNum = (boardCount - (pageBtnNum * 12) + 11); // 맨 위 게시물 번호는 몇번인가
//        lastNum = firstNum - 11; // 맨 아래 게시물은 번호는 몇번인가

        firstNum = (boardCount - (pageBtnNum * 13)+12); // 맨 위 게시물 번호는 몇번인가
        lastNum = firstNum - 12; // 맨 아래 게시물은 번호는 몇번인가
        if (lastNum < 0) {
            lastNum = 0;
        }
        Log.d("page", "게시물 수 : " + boardCount);
        Log.d("page", "Page 수 : " + Count);
        Log.d("page", "FirstNum : " + firstNum);
        Log.d("page", "LastNum  : " + lastNum);

    }

}
