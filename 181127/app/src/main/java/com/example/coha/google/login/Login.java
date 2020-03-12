package com.example.coha.google.login;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.coha.google.MainBoardActivity;
import com.example.coha.google.R;
import com.example.coha.google.push.DeleteTokenService;

import static com.example.coha.google.MainBoardActivity.loginState;
import static com.example.coha.google.R.layout.login;

/**
 * Created by Samchan on 2016-12-01.
 */

public class Login extends Activity {
    EditText idEditText;
    EditText passEditText;

    Button connectBtn;
    Button joinBtn;

    String ID, PW, Nickname, Email;
    SessionManagement session;
    CheckUserInfo check;

    private final long FINSH_INTERVAL_TIME    = 2000;  // 2초안에 뒤로가기를 한번더 누르면 종료되도록 2000설정
    private long  backPressedTime  = 0; // 백키를 눌렀을때 시간 0초      ~2초

    public static String loginState_ID = "";

    AlertDialogManager alert = new AlertDialogManager();
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(login);
        idEditText = (EditText) findViewById(R.id.ID_EditText);
        passEditText = (EditText) findViewById(R.id.password_editText);

        connectBtn = (Button) findViewById(R.id.connect_Btn);
        joinBtn = (Button) findViewById(R.id.join_Btn);

        session = new SessionManagement(getApplicationContext());


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_Btn: {

                try {
                    if (idEditText.getText().toString().equals("")) {
                        idEditText.setHint("ID를 입력 해주세요.");
                        idEditText.requestFocus();
                    } else if (passEditText.getText().toString().equals("")) {
                        passEditText.setHint("비밀번호를 입력 해주세요.");
                        passEditText.requestFocus();
                    } else {
                        ID = idEditText.getText().toString();
                        PW = passEditText.getText().toString();


                        //입력된 ID와 Password를 php로 보내서 DB에 중복이 되는지 확인하고,
                        //결과에 따라 다른 echo값을 받아와 뒷일을 처리한다.
                        check = new CheckUserInfo(getApplicationContext(),ID,PW);
                        check.execute();

                        String checkNum = (check.get()).trim();
                        String[] results = checkNum.split("\\|\\|");
                        String last_check_num;

                        last_check_num = results[0];
                        ID = results[1];
                        Nickname = results[2];
                        Email = results[3];

                        Log.d("login","나눈 값 1 : "+last_check_num);
                        Log.d("login","나눈 값 2 : "+ID);
                        Log.d("login","나눈 값 3 : "+Nickname);
                        Log.d("login","나눈 값 4 : "+Email);

                        //Checknum이 0이면 아이디 중복, 1이면 아이디는 맞지만 비밀번호 틀린 것, 2면 확인 완료
                        if(last_check_num.equals("0")){
                            alert.showAlertDialog(Login.this, "경고", "아이디를 확인해주세요.", false);

                        }else if(last_check_num.equals("1")){
                            alert.showAlertDialog(Login.this, "경고", "비밀번호를 확인해주세요.", false);

                        }else if(last_check_num.equals("2")){

                            //로그인을 만든다.
                            session.createLoginSession(Nickname, Email);


                            //notlogin의 기존 토큰 삭제
                            loginState = 1;
                            loginState_ID = ID;
                            Intent intent = new Intent(this,DeleteTokenService.class);
                            startService(intent);


                            // Staring MainActivity
                            Intent i = new Intent(getApplicationContext(), MainBoardActivity.class);
                            startActivity(i);
                            finish();


                        } else{
                            Toast.makeText(getApplicationContext(), "아이디, 비밀번호가 옳바르지 않습니다. ", Toast.LENGTH_LONG).show();
                        }


                    }

                } catch (Exception e) {
                    Log.d("TEST", "save버튼 실패");
                }


            }

            break;
            case R.id.join_Btn: {
                //User가입
                Intent i = new Intent(getApplicationContext(), JoinUser.class);
                startActivity(i);
                finish();
            }
            break;

        }
    }

    @Override
    public void onBackPressed() {
        //뒤로 가기 버튼을 누르면 메인으로 넘어간다.

        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if ( 0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime ) {
            Intent intent = new Intent(this,MainBoardActivity.class);
            startActivity(intent);
            finish();

            // super.onBackPressed();
        }
        else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(),"한번 더 누르시면 처음 화면으로 돌아갑니다.",Toast.LENGTH_SHORT).show();
        }
    }

}






























//
//    protected class ConnectLogin extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//
//                HttpClient httpC = new DefaultHttpClient();
//
//                HttpPost httppost = new HttpPost(
//                        "http://ehdntjr123.dothome.co.kr/login_test.php");
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("ID", ID));
//                nameValuePairs.add(new BasicNameValuePair("PW", PW));
//
//
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
//                        "UTF-8"));
//                httpC.execute(httppost);
//
//
//                /////////////////////////////////////////////////////////////////////
//
//                HttpResponse res = httpC.execute(httppost);
//                HttpEntity entityRes = res.getEntity();
//                InputStream inputStream = entityRes.getContent();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, HTTP.UTF_8));
//
//                String total = "";
//                String tmp = "";
//
//                while ((tmp = reader.readLine()) != null) {
//                    if (tmp != null) {
//                        total += tmp;
//                    }
//                }
//                inputStream.close();
//
//                return total; //원래 "" 이거였음
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "Login X";
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
//            super.onPostExecute(s);
//        }
//    }
//}
