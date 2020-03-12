package com.example.coha.google.reply;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coha.google.MainBoardActivity;
import com.example.coha.google.R;
import com.example.coha.google.login.AlertDialogManager;
import com.example.coha.google.login.Login;
import com.example.coha.google.login.SessionManagement;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Samchan on 2016-11-22.
 */

public class BoardView extends Activity {

    //클릭한 게시물을 보여주는 Activity
    //게시물의 정보를 담고 있는 변수
    TextView titleView;
    TextView dateView;
    TextView writerView;
    TextView goodView;
    TextView boardNumView;
    TextView memoView;

    //FrameLayout으로 구성된 댓글 부분중, 댓글을 보여주는 Layout부분이다.
    LinearLayout viewReplyLayout;
    FrameLayout frameLayout;

    //댓글을 추가하는 버튼
    Button reWriteBtn;
    Button finishBtn;
    Button reWriteCancelBtn;
    Button boardGoodBtn;


    ReplyAdapter replyAdapter;
    ListView listView_Reply;


    EditText replyWriteEditText;

    DownloaderRe down;
    String url = "http://ehdntjr123.dothome.co.kr/board/board_reply.php";


    String id, time, text, board_num;

    long now;
    String curDate;


    String returnAnswer;
    String uid;
    String goodValue;

    String b_num;
    String writer;
    String title;
    String goodCheck;
    String boardToken;

    /**BoardView의 Frame**/
    //Button modi_Btn;
    Button del_Btn;




    SessionManagement session;
    AlertDialogManager alert = new AlertDialogManager();
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);

        //modi_Btn = (Button)findViewById(R.id.board_modify_Btn);
        del_Btn = (Button)findViewById(R.id.board_delete_Btn);
        String checkBtn = getIntent().getStringExtra("BtnCheck");
        if(checkBtn.equals("1")){
            //modi_Btn.setVisibility(Button.VISIBLE);
            del_Btn.setVisibility(Button.VISIBLE);

        }else{
            //modi_Btn.setVisibility(Button.INVISIBLE);
            del_Btn.setVisibility(Button.INVISIBLE);
        }



        titleView = (TextView) findViewById(R.id.viewBoard_textView_title);
        dateView = (TextView) findViewById(R.id.viewBoard_textView_date);
        writerView = (TextView) findViewById(R.id.viewBoard_textView_writer);
        goodView = (TextView) findViewById(R.id.viewBoard_textView_good);
        boardNumView = (TextView) findViewById(R.id.viewBoard_textView_boardNum);
        memoView = (TextView) findViewById(R.id.viewBoard_textView_board);


        replyWriteEditText = (EditText) findViewById(R.id.reply_write_editText);
        reWriteBtn = (Button) findViewById(R.id.reply_write_Btn);
        reWriteCancelBtn = (Button) findViewById(R.id.reply_cancel_Btn);
        finishBtn = (Button) findViewById(R.id.reply_write_finish_Btn);
        boardGoodBtn = (Button) findViewById(R.id.board_good_Btn);


        viewReplyLayout = (LinearLayout) findViewById(R.id.writeReply_Layout);
        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        session = new SessionManagement(getApplicationContext());

        //DB에서 가져온 정보를 Text에 setText를 시킨다.
        titleView.setText(getIntent().getStringExtra("Title"));
        dateView.setText("날짜 : " + getIntent().getStringExtra("Time"));
        writerView.setText("작성자 : " + getIntent().getStringExtra("ID"));
        goodView.setText("좋아요 : " + getIntent().getStringExtra("Good"));
        boardNumView.setText("No. : " + getIntent().getStringExtra("Num"));
        memoView.setText("" + getIntent().getStringExtra("Memo"));

        b_num = getIntent().getStringExtra("Num");
        writer = getIntent().getStringExtra("ID");
        title = getIntent().getStringExtra("Title");
        goodCheck = getIntent().getStringExtra("GoodCheck");
        boardToken = getIntent().getStringExtra("boardToken");

        Log.d("TEST","boardToken : "+ boardToken);

        HashMap<String, String> user = session.getUserDetails();
        uid = user.get(SessionManagement.KEY_NAME);


        listView_Reply = (ListView) findViewById(R.id.listView_reply);
        replyAdapter = new ReplyAdapter(this);
        listView_Reply.setAdapter(replyAdapter);

        DownReply();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reply_write_Btn: {
                //세션에서 로그인이 되어있는지 확인한다.
                if (!session.isLoggedIn()) {
                    Dialog_Confirm();
                }
                else {

                    if(reWriteBtn.getText().toString().equals(String.valueOf("댓글 보기"))){
                        Log.d("reply","111111111111111111111");
                        frameLayout.setVisibility(View.VISIBLE);
                        reWriteBtn.setText("댓글 쓰기");
                        boardGoodBtn.setText("댓글 숨기기");
                    } else if(reWriteBtn.getText().toString().equals(String.valueOf("댓글 쓰기"))){
                        listView_Reply.setVisibility(ListView.INVISIBLE);
                        viewReplyLayout.setVisibility(LinearLayout.VISIBLE);
                    }

                }


            }
            break;

            case R.id.reply_write_finish_Btn: {
                Log.d("test", "확인한번 해보자zzz session" + session.isLoggedIn());
                //session에서 로그인이 되어있지 않으면, 다시 로그인 페이지로 넘어가게 해놨음.

                time();
                if (replyWriteEditText.getText().toString().equals("")) {
                    replyWriteEditText.setHint("댓글을 입력 해주세요.");
                    replyWriteEditText.requestFocus();
                } else {
                    HashMap<String, String> user = session.getUserDetails();
                    id = user.get(SessionManagement.KEY_NAME);
                    time = curDate;
                    text = replyWriteEditText.getText().toString();
                    board_num = b_num;
                    Log.d("good" , "보낸 값1 : " + board_num);

                    new SaveReply().execute();
                    Log.d("fcm","id, writer 확인"+id+"___"+writer);
                    //자기 자신의 댓글에는 알림을 하지 않는다.
                    if(!id.equals(writer)) {
                        new newReplyNotification().execute();
                    }

                    listView_Reply.setVisibility(ListView.VISIBLE);
                    viewReplyLayout.setVisibility(LinearLayout.INVISIBLE);

                    DownReply(); //댓글 listview 새로 고침을 하는 부분, 다시 down을 실행.

                    replyAdapter.notifyDataSetChanged();

                    replyWriteEditText.setText("");

                }
            }
            break;

            case R.id.reply_cancel_Btn: {

                listView_Reply.setVisibility(ListView.VISIBLE);
                viewReplyLayout.setVisibility(LinearLayout.INVISIBLE);

            }
            break;
            case R.id.board_good_Btn: {


                if(boardGoodBtn.getText().toString().equals(String.valueOf("좋아요"))){
                    new GoodBtn().execute();
                    Log.d("good","good버튼1 클릭**");
                } else if(boardGoodBtn.getText().toString().equals(String.valueOf("댓글 숨기기"))){
                    Log.d("reply","22222222222222222222");
                    frameLayout.setVisibility(View.GONE);
                    boardGoodBtn.setText("좋아요");
                    reWriteBtn.setText("댓글 보기");
                }




            }
            break;
            case R.id.board_delete_Btn: {
                //게시물과 관련 댓글을 다 지운다.
                //DB에서 게시물 , 댓글 지우고 Main으로 넘어간다
                //Dialog를 띄운다.
                Dialog_Confirm_delete();

            }
            break;
        }
    }

    protected class SaveReply extends AsyncTask<String, Integer, String> {
        //댓글을 저장하는 부분
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_reply_insert.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("time", time));
                nameValuePairs.add(new BasicNameValuePair("text", text));
                nameValuePairs.add(new BasicNameValuePair("board_num", board_num));
                nameValuePairs.add(new BasicNameValuePair("title", title));
                nameValuePairs.add(new BasicNameValuePair("boardToken", boardToken));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return "";

        }
    }

    protected class newReplyNotification extends AsyncTask<String, Integer, String> {
        //댓글을 저장하는 부분
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/push_notification.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("id", id));
                nameValuePairs.add(new BasicNameValuePair("b_num", b_num));
                nameValuePairs.add(new BasicNameValuePair("title", title));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return "";

        }
    }

    protected class GoodBtn extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();
                httpC = getThreadSafeClient();

                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_good.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                //좋아요버튼을 누른 사람의 정보로 db로 가서, 좋아요를 누른 게시물이면 이미 좋아요를 눌렀습니다 라고 표시 되게해야함.
                //일단 좋아요를 누른 사람의 정보를 보낸 뒤 있으면 0 없으면 1의 형태로 리턴 받아온다.
                //다음에 없다면 1로 가는데, 여기서는 board의 good에 추가로 1을 하고, login디비에는 추가로 게시물의 번호를 등록한다.
                //위에 두가지를 같은 php내에서 수행하자!


                nameValuePairs.add(new BasicNameValuePair("num",b_num));
                nameValuePairs.add(new BasicNameValuePair("id",uid));


                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

                HttpResponse response = httpC.execute(httppost);
                String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                Log.d("good" , "보낸 값 num : " + b_num);
                Log.d("good" , "보낸 값 id : " + uid);
                Log.d("good" , "PHP서버에서 가져온 값 : " + resStr);

                returnAnswer = resStr.trim();
                Log.d("good","good버튼2 클릭&&");

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return returnAnswer;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("1")){
                Log.d("good",returnAnswer+" 이 나왔다.");
                new InsertGood().execute();

            }else if(s.equals("0")){

                alert.showAlertDialog(BoardView.this, "확인", "이미 '좋아요'를 하셨습니다.", true);
                Log.d("good",returnAnswer+" 이 나왔다.");

            }
        }
    }

    protected class InsertGood extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient httpC1 = new DefaultHttpClient();
                httpC1 = getThreadSafeClient();

                HttpPost httppost1 = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_good_insert.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


                nameValuePairs.add(new BasicNameValuePair("num",b_num));
                nameValuePairs.add(new BasicNameValuePair("id",uid));

                httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC1.execute(httppost1);


                HttpResponse response = httpC1.execute(httppost1);
                String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                Log.d("good" , "good 값 : " + resStr);
                goodValue = resStr;
                Log.d("good","몇번 돌아가니?*");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return goodValue;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            goodView.setText("좋아요 : " + s.trim());
        }
    }

    public void time() {
        now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpledateFormat = new SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", java.util.Locale.getDefault());
        curDate = String.valueOf(simpledateFormat.format(date));
    }

    public void DownReply() {
        //댓글을 가져오는 부분
        down = new DownloaderRe(this, url, listView_Reply, replyAdapter,boardToken);
        down.execute();

    }

    private void Dialog_Confirm() {
        //댓글을 작성하고 로그인이 되어있지 않으면, 로그인 페이지로 넘어가는 부분
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("확인");
        alert.setMessage("로그인이 되어있지 않습니다. 로그인 페이지로 이동할까요?");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 로그인 안되있다.
                Intent intent = new Intent(getApplicationContext(), Login.class);
                // 모든 액티비티 종료
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // 새로운 액티비티 실행
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //로그인 액티비티
                startActivity(intent);
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    /**
     *삭제 확인 다이얼로그
     **/
    private void Dialog_Confirm_delete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("경고!");
        alert.setMessage("게시물을 지우시겠습니까? 관련 댓글까지 지워집니다.");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteBoard deleteBoard = new deleteBoard();
                deleteBoard.execute();
                Intent intent = new Intent(getApplicationContext(), MainBoardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }

    public static DefaultHttpClient getThreadSafeClient()  {

        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,

                mgr.getSchemeRegistry()), params);
        return client;
    }

    /**
     * 게시물을 지우는 직접적인 함수. 삭제 확인 다이얼로그에서 확인 버튼을 누르면 이 함수가 실행된다.
     * **/
    protected class deleteBoard extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                HttpClient httpC = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_delete.php"); // 이 php에는 db에서 게시물 하나를 지우는 코드가 저장되어있다.

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("num", b_num));
                nameValuePairs.add(new BasicNameValuePair("boardToken", boardToken));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

            } catch (Exception e) {
                e.printStackTrace();

            }


            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
