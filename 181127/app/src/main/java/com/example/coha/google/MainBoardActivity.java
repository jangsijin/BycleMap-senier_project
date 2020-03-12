package com.example.coha.google;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.coha.google.login.CheckLoginState;
import com.example.coha.google.login.Login;
import com.example.coha.google.login.SessionManagement;
import com.example.coha.google.parser.Downloader;
import com.example.coha.google.reply.BoardView;
import com.example.coha.google.login.CheckLoginState;
import com.example.coha.google.login.Login;
import com.example.coha.google.login.SessionManagement;
import com.example.coha.google.parser.Downloader;
import com.example.coha.google.reply.BoardView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.example.coha.google.R.id.page_1;
import static com.example.coha.google.R.id.page_2;
import static com.example.coha.google.R.id.page_3;
import static com.example.coha.google.R.id.page_4;
import static com.example.coha.google.R.id.page_5;
import static com.example.coha.google.R.id.page_next;
import static com.example.coha.google.R.id.page_previous;
import static com.example.coha.google.login.SessionManagement.currNick;
import static com.example.coha.google.login.SessionManagement.makeBoardToken;
import static com.example.coha.google.parser.Parser.boardItemNum;
/**
 * 궁금하신 점은
 * Mail = samcsamc07@naver.com으로 연락주시길 바랍니다.
 * 앞서 말씀드렸다시피, 기술적인 부분이나 코딩적 부분이 많이 미흡합니다.
 * 참고자료나, 간단한 실행용 어플로 봐주시길 바랍니다.
 * 필요한 기술(프로그램) : 안드로이드 스튜디오 , Editplus3(php) , apm(아파치 디비 등) 입니다.
 * */
public class MainBoardActivity extends AppCompatActivity {

    Context context;
    ImageButton loginBtn;
    ImageButton insert_Btn;

    public static final String TAG = com.example.coha.google.push.DeleteTokenService.class.getSimpleName();

    ListView board_ListView;
    ListViewAdapter adapter;
    String url = "http://ehdntjr123.dothome.co.kr/board/board.php";

    int position_g;
    BoardItem item;
    private Handler handler;
    Downloader down;
    BoardDelete boardDelete;

    SessionManagement session;

    /**페이지 버튼 부분**/
    public static int pageBtnNum = 1;
    public static Button[] page =new Button[5];
    public static ImageButton preBtn,nextBtn;
    int Btn_one = 1;
    int Btn_two,Btn_tree,Btn_four,Btn_five;

    /**검색 부분**/
    String SearchWord = null;
    LinearLayout searchPart;
    LinearLayout menuBtnPart;
    ImageButton menu_Back_Btn;
    EditText search_EditText;

    ImageButton sort_Btn;
    Spinner sortSpinner;
    String sortValue ="0";

    /**Push알람 부분**/
    public static int loginState = 0;

    public MainBoardActivity() {


    }

    public MainBoardActivity(Context context) {
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);
        //ActionBar 부분
        setCustomActionBar();

        //Push알람
        //FCM에서 받아온 정보를 공지하는 부분
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        //어플 설치시, 시작시 토큰 확인 후 토큰 생성.
        //비회원 로그인 상태로 간주한다.
        FirebaseInstanceId.getInstance().getToken();

//        //토큰을 삭제하는 부분, Manifest에 service추가 했다.
//        //*  http://jo.centis1504.net/?p=968 여기 참조
//        Intent intent = new Intent(this,DeleteTokenService.class);
//        startService(intent);
//        Intent intent = getIntent();
//        String push_State = intent.getExtras().getString("PushCode").toString();
//        Log.d("push","psuh state" + push_State);



        //login버튼 부분
        loginBtn = (ImageButton) findViewById(R.id.login_Btn);

        //게시물 추가 부분
        insert_Btn = (ImageButton) findViewById(R.id.insert_Memo_Btn);

        //ListView관련 부분
        board_ListView = (ListView) findViewById(R.id.board_listView);
        final View header = getLayoutInflater().inflate(R.layout.board_list_header, null, false);
        board_ListView.addHeaderView(header);
        board_ListView.setAdapter(null);
        adapter = new ListViewAdapter(this);
        board_ListView.setAdapter(adapter);//null로 바꿈

        //정렬하는 부분
        sortSpinner = (Spinner)findViewById(R.id.sort_spinner);
        sort_Btn = (ImageButton)findViewById(R.id.sort_ImageBtn);

        //아래에 다시 정리 해야함.
        sort_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortSpinner.performClick();
                Log.d("spin","여기까지됬음 ");
                sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Log.d("spin","여기는?");

                        Log.d("spin","position : " + position);
                        if(sortSpinner.getSelectedItemPosition() == 0){
                            sortValue = "1";
                            Down();
                            initialization();

                        }else if(sortSpinner.getSelectedItemPosition() == 1){
                            sortValue = "2";
                            Down();
                            initialization();

                        }else if(sortSpinner.getSelectedItemPosition() == 2){
                            sortValue = "3";
                            Down();
                            initialization();
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });

        //Search관련 부분
        searchPart = (LinearLayout)findViewById(R.id.search_LinearLayout);
        menuBtnPart = (LinearLayout)findViewById(R.id.menu_Btn_LinearLayout);
        menu_Back_Btn = (ImageButton)findViewById(R.id.menu_Back_Btn);
        search_EditText = (EditText)findViewById(R.id.Search_editText);

        //페이지 버튼 관련 부분
        page[0] = (Button)findViewById(page_1);
        page[1] = (Button)findViewById(page_2);
        page[2] = (Button)findViewById(page_3);
        page[3] = (Button)findViewById(page_4);
        page[4] = (Button)findViewById(page_5);
        preBtn = (ImageButton)findViewById(page_previous);
        nextBtn = (ImageButton)findViewById(page_next);
        page[0].setBackgroundColor(Color.parseColor("#FFD4E0DB"));



        //초기 ListItem을 가져옴
        Down();

        //Listview 아이템 하나가 클릭 되었을 때
        board_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //BoardView의 수정 삭제 버튼
                String BtnCheck = "0";
                item = (BoardItem) board_ListView.getItemAtPosition(position);
                if(currNick.equals(item.getData(3))) {
                    position_g = position;
                    BtnCheck = "1";
                    viewBoard(position,BtnCheck); //viewBoard호출 - 아이템 하나 보여주기.
                }else{
                    position_g = position;
                    BtnCheck = "0";
                    viewBoard(position,BtnCheck); //viewBoard호출 - 아이템 하나 보여주기.
                }

            }
        });

        //Listview 아이템 하나가 길게 클릭 되면 삭제된다.
        board_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item = (BoardItem) board_ListView.getItemAtPosition(position);
                Log.d("test","asdasd");
                Log.d("login","curr" + currNick);
                Log.d("login","item.3" + item.getData(3));
                if(currNick.equals(item.getData(3))) {
                    deleteItem(position);//deleteItem 호출 - 아이템 하나 제거
                }
                return true; //이부분이 중요하다. //return true를 하지 않고 false로 해놓으면 롱클릭이 시행된후 바로 숏클릭도 같이 시행되서 꼬인다.
            }
        });

        session = new SessionManagement(getApplicationContext());//로그인 세션을 유지한다. 단말기 내에서 세션 유지

        if(!session.isLoggedIn()){
            Log.d("login","로그인 X");

        }else{

            session.getUserDetails();
            Log.d("login","로그인 O : " +currNick );

        }
        Log.d("login" , "세션확인 Token : " + makeBoardToken);
    }

    /**
     * MainLayour의 모든 버튼들의 기능이있다.
     * **/
    public void onClick(View v) {

        switch (v.getId()) {
            //게시물 찾기 버튼
            case R.id.itemSearch_Btn: {
                //FrameLayout을 사용했기에, 원래 버튼 부분을 보이지 않게 바꾸고, 검색 부분을 보이게 한다.
                searchPart.setVisibility(LinearLayout.VISIBLE);
                menuBtnPart.setVisibility(LinearLayout.INVISIBLE);

            }
            break;
            //게시물 찾기 버튼을 누르고 다시 처음 페이지로 이동 하는 버튼
            case R.id.menu_Back_Btn: {

                //돌아가기 버튼 눌렀을 때, EditText 부분 초기화
                search_EditText.setText("");

                //SearchWord를 null로 초기화해야 원래대로 값을 원래대로 가져 올 수 있다.
                SearchWord = null;

                //page버튼을 다시 1번으로 바꿔주고 체크 표시를 바꿔주는 부분이다.
                initialization();

                //다시 검색부분을 보이지 않게 한다.
                searchPart.setVisibility(LinearLayout.INVISIBLE);
                menuBtnPart.setVisibility(LinearLayout.VISIBLE);

                //게시물을 다시 불러온다. 이때, SearchWord가 null이기때문에 Downloader에서 수행하는 함수가 바뀐다.downloadData();
                Down();
            }
            break;

            //검색을 누른 버튼
            case R.id.itemSearchStart_Btn: {

                //검색어를 넣고 SearchWord에 값을 전달해 Down()을 실행하기 때문에 Downloader부분의 수행하는 함수가 바뀐다.downloadSearchData();
                SearchWord = search_EditText.getText().toString();
                if (search_EditText.getText().toString().equals("")) {
                    search_EditText.setHint("검색어를 입력 해주세요.");
                    search_EditText.requestFocus();
                }else{
                    Down();
                }
                initialization();

            }
            break;

            case R.id.clearBtn: {

                search_EditText.setText("");

            }
            break;

            case R.id.myGeaSiMool_ImageBtn: {

                sortValue = "4";
                Down();
                initialization();

            }
            break;

            //게시물을 추가 할 수 있다. 글쓰기 버튼
            case R.id.insert_Memo_Btn: {
                //session에서 로그인이 되어있지 않으면, 다시 로그인 페이지로 넘어가게 해놨음.
                if(!session.isLoggedIn()){
                    Dialog_Confirm_insert(); //session에서 로그인이 되어 있지 않으면, 로그인 화면으로 넘어가는 다이얼로그를 띄운다.

                }else {
                    //글쓰기 Activity를 실행한다.
                    Intent intent = new Intent(getApplicationContext(), BoardInsertActivity.class);
                    intent.putExtra("Insert_Mode", "Insert_Board"); //사실 없어도 된다.
                    intent.putExtra("boardToken",makeBoardToken);
                    startActivityForResult(intent, 1002); //1002
                }
            }
            break;

            //로그인 Activity를 실행한다.
            case R.id.login_Btn: {
                Intent intent = new Intent(getApplicationContext(), CheckLoginState.class);
                intent.putExtra("Login_Check_Mode", "Login_Check"); //사실 없어도 된다.
                startActivity(intent); //1003
            }
            break;
            case R.id.home_Btn: {
                sortValue = "0"; //홈버튼을 눌렀을 때, 자신 게시물도 초기화
                SearchWord = null;
                //홈 버튼 눌렀을 때, EditText 부분 초기화
                search_EditText.setText("");
                //SearchWord를 null로 초기화해야 원래대로 값을 원래대로 가져 올 수 있다.

                //page버튼을 다시 1번으로 바꿔주고 체크 표시를 바꿔주는 부분이다.
                initialization();

                //다시 검색부분을 보이지 않게 한다.
                searchPart.setVisibility(LinearLayout.INVISIBLE);
                menuBtnPart.setVisibility(LinearLayout.VISIBLE);

                //게시물을 다시 불러온다. 이때, SearchWord가 null이기때문에 Downloader에서 수행하는 함수가 바뀐다.downloadData();
                Down();
            }
            break;


            /////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////

            //첫번째 page버튼을 클릭 체크부분의 표시를 바꿔주는 부분도 있다.
            case page_1: {
                //pageBtnNum의 값은 1로 초기화 되있기 때문에 무조건 처음 실행하면 1번 페이지를 표시하게 된다.
                //처음 화면에는 1,2,3,4,5 버튼의 숫자가 미리 입력 되어있다.

                pageBtnNum = Integer.valueOf(page[0].getText().toString());
                page[0].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
                page[1].setBackgroundColor(Color.parseColor("#112820"));
                page[2].setBackgroundColor(Color.parseColor("#112820"));
                page[3].setBackgroundColor(Color.parseColor("#112820"));
                page[4].setBackgroundColor(Color.parseColor("#112820"));

                Down();
            }
            break;

            case page_2: {

                pageBtnNum = Integer.valueOf(page[1].getText().toString());
                page[1].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
                page[0].setBackgroundColor(Color.parseColor("#112820"));
                page[2].setBackgroundColor(Color.parseColor("#112820"));
                page[3].setBackgroundColor(Color.parseColor("#112820"));
                page[4].setBackgroundColor(Color.parseColor("#112820"));

                Down();

            }
            break;
            case page_3: {

                pageBtnNum = Integer.valueOf(page[2].getText().toString());
                page[2].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
                page[1].setBackgroundColor(Color.parseColor("#112820"));
                page[0].setBackgroundColor(Color.parseColor("#112820"));
                page[3].setBackgroundColor(Color.parseColor("#112820"));
                page[4].setBackgroundColor(Color.parseColor("#112820"));

                Down();
            }
            break;
            case page_4: {

                pageBtnNum = Integer.valueOf(page[3].getText().toString());
                page[3].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
                page[1].setBackgroundColor(Color.parseColor("#112820"));
                page[2].setBackgroundColor(Color.parseColor("#112820"));
                page[0].setBackgroundColor(Color.parseColor("#112820"));
                page[4].setBackgroundColor(Color.parseColor("#112820"));

                Down();
            }
            break;
            case page_5: {

                pageBtnNum = Integer.valueOf(page[4].getText().toString());
                page[4].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
                page[1].setBackgroundColor(Color.parseColor("#112820"));
                page[2].setBackgroundColor(Color.parseColor("#112820"));
                page[3].setBackgroundColor(Color.parseColor("#112820"));
                page[0].setBackgroundColor(Color.parseColor("#112820"));

                Down();
            }
            break;

            //이전 페이지 버튼
            case page_previous: {
                //첫번째 버튼을 기준으로 이동한다.
                Btn_one = Btn_one-5;
                if(Btn_one < 1){
                    Btn_one = 1;
                }

                //첫번째 버튼을 기준으로 나머지 네 버튼에 들어 갈 숫자를 정한다.
                Btn_two = Btn_one + 1;
                Btn_tree = Btn_one + 2;
                Btn_four = Btn_one + 3;
                Btn_five = Btn_one + 4;

                page[0].setText(String.valueOf(Btn_one));
                page[1].setText(String.valueOf(Btn_two));
                page[2].setText(String.valueOf(Btn_tree));
                page[3].setText(String.valueOf(Btn_four));
                page[4].setText(String.valueOf(Btn_five));

                //이전 페이지로 넘어 갈 때는 색변화가 없다. 만약 한 페이지마다 로드를 다시하면 시간이 걸리기 때문에 그렇게 하지 않았다.
                //만약 이전 페이지를 누를 때마다 다시 게시물을 가져오고 싶다면, 체크 값을 1번 버튼만 색이 들어가게 바꿔준 뒤 다시 로드 하면된다.
                for(int i=0;i<5;i++){
                    page[i].setBackgroundColor(Color.parseColor("#112820"));
                }
            }
            break;

            //다음 페이지 버튼 - 이전 페이지와 동일
            case page_next: {
                //모든 게시물을 가져왔을 때, 최대 페이지 수의 값을 조절하는 부분이다. 게시물의 숫자가 바뀌면 이부분을 바꿔준다.
                //페이지 조정할때 Btn_five*13의 숫자를 가져오면 최대 페이지 갯수를 바꿀 수 있다.
                if(boardItemNum <= Btn_five*13){
                    Btn_two = Btn_one + 1;
                    Btn_tree = Btn_one + 2;
                    Btn_four = Btn_one + 3;
                    Btn_five = Btn_one + 4;
                }else {
                    Btn_one = Btn_one + 5;
                    Btn_two = Btn_one + 1;
                    Btn_tree = Btn_one + 2;
                    Btn_four = Btn_one + 3;
                    Btn_five = Btn_one + 4;
                }
                page[0].setText(String.valueOf(Btn_one));
                page[1].setText(String.valueOf(Btn_two));
                page[2].setText(String.valueOf(Btn_tree));
                page[3].setText(String.valueOf(Btn_four));
                page[4].setText(String.valueOf(Btn_five));

                for(int i=0;i<5;i++){
                    page[i].setBackgroundColor(Color.parseColor("#112820"));
                }

            }
            break;
        }
    }

    /**
     * 게시물을 보는 부분이다.
     * ListView의 아이템을 짧게 클릭하면 해당 함수를 호출한다.
     * **/
    private void viewBoard(int position,String BtnCheck) {
        Log.d("TEST", "Adapter getItem position  : " + position);

        //아이템이 존재하면,
        if (board_ListView.getItemAtPosition(position) != null) {

            item = (BoardItem) board_ListView.getItemAtPosition(position);
            //BoardView클래스로 넘어간다. 이때 클릭한 아이템의 모든 정보를 보내 그 아이템의 정보를 확인한다.
            Intent intent = new Intent(getApplicationContext(), BoardView.class);
            intent.putExtra("View_Mode", "View_Board");
            intent.putExtra("BtnCheck",BtnCheck);

            intent.putExtra("Num", item.getData(0));
            intent.putExtra("Title", item.getData(1));
            intent.putExtra("Time", item.getData(2));
            intent.putExtra("ID", item.getData(3));
            intent.putExtra("Good", item.getData(4));
            intent.putExtra("Memo", item.getData(5));
            intent.putExtra("GoodCheck", item.getData(6));
            intent.putExtra("boardToken", item.getData(7));

            startActivityForResult(intent, 1001);

        } else {
            Log.d("TEST", "Adapter array 문제있음.");
        }
    }

    /**
     * 게시물을 지우는 부분
     * **/
    private void deleteItem(int position) {
        //position의 값을 다른 곳에서 사용 할 수 없으므로 전역변수로 값을 저장
        position_g = position;
        //다이얼 로그의 형태로 지우기 때문에 다이얼로그로 이동.

        Dialog_Confirm_delete();

    }

    /**
     * 게시물을 지우는 직접적인 함수. 삭제 확인 다이얼로그에서 확인 버튼을 누르면 이 함수가 실행된다.
     * **/
    //삭제해도 된다.
    protected class deleteBoard extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

            item = (BoardItem) board_ListView.getItemAtPosition(position_g);

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            int position = Integer.parseInt(params[0]);

            //클릭 아이템이 존재할 시 실행
            if (item != null) {

                try {
                    HttpClient httpC = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(
                            "http://ehdntjr123.dothome.co.kr/board/board_delete.php"); // 이 php에는 db에서 게시물 하나를 지우는 코드가 저장되어있다.

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("num", item.getData(0)));
                    nameValuePairs.add(new BasicNameValuePair("title", item.getData(1)));

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

    private void setCustomActionBar() {

        //액션바 부분, 액션바의 커스텀을 담당.
        //단순한 이미지나 색만 바꾸는 수준이다.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#112820")));

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);
    }

    /**
     *삭제 확인 다이얼로그
     **/
    private void Dialog_Confirm_delete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("경고!");
        alert.setMessage("게시물을 지우시겠습니까?");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                deleteBoard deleteBoard = new deleteBoard();
//                deleteBoard.execute(position_g + "");
                boardDelete = new BoardDelete(getApplicationContext(), item, board_ListView, adapter,position_g);
                boardDelete.execute(); // 실행// execute(position_g + "") 삭제 함
                adapter.notifyDataSetChanged();
                Down();
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
     * 로그인이 되어있지 않으면 글을 쓸 수 없기 때문에,
     * 로그인 페이지로 이동한다.
     * **/
    private void Dialog_Confirm_insert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("확인");
        alert.setMessage("로그인이 되어있지 않습니다. 로그인 페이지로 이동할까요?");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(getApplicationContext(), Login.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                startActivity(i);
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
     * 가장 중요한 부분, 게시물을 가져온다.
     * **/
    public void Down() {
        /**여기서 오류 해결**/
        adapter.notifyDataSetChanged();
        board_ListView.setAdapter(null);
        /**
         * Downloader 클래스 실행 후 Parser 클래스 실행, 그 안에 Context정보 url정보 listview정보 adapter정보 SearchWord 정보를 보내준다.
         * **/
        down = new Downloader(this, url, board_ListView, adapter,SearchWord,sortValue);
        down.execute(); // 실행

    }

    /**
     * 초기화 작업
     * **/
    public void initialization(){


        //sortValue = "0"; 여기서 초기화 시키지 않는 이유는 페이지를 이동하면 초기화 된 페이지가 나온다.
        //SearchWord = null;
        pageBtnNum = 1;
        page[0].setBackgroundColor(Color.parseColor("#FFD4E0DB"));
        page[1].setBackgroundColor(Color.parseColor("#112820"));
        page[2].setBackgroundColor(Color.parseColor("#112820"));
        page[3].setBackgroundColor(Color.parseColor("#112820"));
        page[4].setBackgroundColor(Color.parseColor("#112820"));

        Btn_one = 1;
        Btn_two = Btn_one + 1;
        Btn_tree = Btn_one + 2;
        Btn_four = Btn_one + 3;
        Btn_five = Btn_one + 4;

        page[0].setText(String.valueOf(Btn_one));
        page[1].setText(String.valueOf(Btn_two));
        page[2].setText(String.valueOf(Btn_tree));
        page[3].setText(String.valueOf(Btn_four));
        page[4].setText(String.valueOf(Btn_five));

        adapter.notifyDataSetChanged();
    }

    private String getCurrentSessionID() {
        Log.d("long","id : ");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String curID = preferences.getString("name", null);
        Log.d("long","id : "+curID);
        return  curID;
    }
    public void onClicked(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
