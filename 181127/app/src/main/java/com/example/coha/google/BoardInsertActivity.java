package com.example.coha.google;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coha.google.login.SessionManagement;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samchan on 2016-11-17.
 */

public class BoardInsertActivity extends Activity {
    //InsertActivity의 버튼
    Button saveBtn;
    Button exitBtn;

    //입력할 정보
    String title, date, id, good, memo;

    //제목, 내용을 입력할 텍스트 창
    EditText titleEditText;
    EditText memoEditText;

    //작성자를 보여주는 곳
    TextView writerText;

    //시간관련 변수
    long now;
    String curDate,tokenDate;
    String name;

    //삽입할 토큰
    String boardToken;


    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_insert);

        session= new SessionManagement(getApplicationContext());
        saveBtn = (Button) findViewById(R.id.insert_save_Btn);
        exitBtn = (Button) findViewById(R.id.insert_exit_Btn);

        titleEditText = (EditText) findViewById(R.id.title_editText);
        memoEditText = (EditText) findViewById(R.id.memo_editText);
        writerText = (TextView) findViewById(R.id.writerID_textView);



        //작성자의 아이디 가져오기
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManagement.KEY_NAME);
        writerText.setText(name);

        //현재 시간
        time();
        //토큰 가져오기

        Intent intent = getIntent();
        boardToken = intent.getExtras().getString("boardToken").toString();
        Log.d("login", "boardToken확인"+boardToken );
        boardToken = boardToken + tokenDate;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_save_Btn: {
                try {
                    if (titleEditText.getText().toString().equals("")) {
                        titleEditText.setHint("제목을 입력 해주세요.");
                        titleEditText.requestFocus();
                    }else if(memoEditText.getText().toString().equals("")) {
                        memoEditText.setHint("게시물을 입력 해주세요.");
                        memoEditText.requestFocus();
                    }
                    else {

                        //EditText의 입력된 정보를 저장하는 정보
                        title = titleEditText.getText().toString();
                        memo = memoEditText.getText().toString();
                        date = curDate;
                        id = name;
                        good = "0"; // 아직 구현 안함
                        Log.d("TEST", "http 실행완료 후 execute");

                        //저장된 정보를 php로 보내 db에 저장
                        new SaveBoard().execute();
                        finish();
                        Log.d("TEST", "http 실행완료 후 finsh실행");


                        //저장 후 다시 메인으로 돌아간다.
                        Intent intent = new Intent(getApplicationContext(), MainBoardActivity.class);
                        intent.putExtra("Board_Mode", "Back_Board");
                        startActivity(intent);

                    }
                }catch (Exception e){
                    Log.d("TEST", "save버튼 실패");
                }
            }

            break;
            case R.id.insert_exit_Btn: {
                //나가기 버튼
                Log.d("TEST", "나가기버튼 눌렀음");
                finish();
            }
            break;

        }
    }

    protected class SaveBoard extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();


                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_insert.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("title",title));
                nameValuePairs.add(new BasicNameValuePair("date",date));
                nameValuePairs.add(new BasicNameValuePair("id",id));
                nameValuePairs.add(new BasicNameValuePair("good",good));
                nameValuePairs.add(new BasicNameValuePair("memo",memo));
                nameValuePairs.add(new BasicNameValuePair("boardToken",boardToken));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);
                Log.d("login", "보낸 boardToken확인"+boardToken);


            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return "";
        }
    }

    //게시물 저장 시간을 저장한다.
    public void time(){
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("MM-dd",java.util.Locale.getDefault());
        SimpleDateFormat addTokenTimeFormat = new SimpleDateFormat("MMddHHmmss",java.util.Locale.getDefault());
        curDate = String.valueOf(simpledateFormat.format(date));
        tokenDate = String .valueOf(addTokenTimeFormat.format(date));
    }

}
