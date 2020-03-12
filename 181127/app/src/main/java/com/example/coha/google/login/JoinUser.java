package com.example.coha.google.login;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.coha.google.R;

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
import java.util.regex.Pattern;

/**
 * Created by Samchan on 2016-12-06.
 */

public class JoinUser extends Activity {

    String u_id, u_password, u_nickname, u_email;

    EditText id_join_EditText;
    EditText password_join_EditText;
    EditText password_join_EditText_confirm;
    EditText nickname_join_EditText;
    EditText email_join_EditText;
    Button join_user_Btn;

    String joinCheckNum;
    AlertDialogManager alert;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_join);

        /**아이디 비밀번호 이메일 한글 제한**/
        InputFilter filterAlphaNum = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9]*$");
                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };

        InputFilter filtermail = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern ps = Pattern.compile("^[a-zA-Z0-9@.]*$");
                if (!ps.matcher(source).matches()) {
                    return "";
                }
                return null;
            }
        };

        id_join_EditText = (EditText) findViewById(R.id.join_id);
        id_join_EditText.setFilters(new InputFilter[]{filterAlphaNum});

        password_join_EditText = (EditText) findViewById(R.id.join_password);
        password_join_EditText.setFilters(new InputFilter[]{filterAlphaNum});

        password_join_EditText_confirm = (EditText) findViewById(R.id.join_c_password);
        password_join_EditText_confirm.setFilters(new InputFilter[]{filterAlphaNum});

        nickname_join_EditText = (EditText) findViewById(R.id.join_nickname);

        email_join_EditText = (EditText) findViewById(R.id.join_email);
        email_join_EditText.setFilters(new InputFilter[]{filtermail});

        join_user_Btn = (Button) findViewById(R.id.join_user_Btn);


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_user_Btn: {

                Log.d("join", "버튼 눌림.");

                try {
                    if (id_join_EditText.getText().toString().equals("")) {
                        id_join_EditText.setHint("영문으로 ID를 입력 해주세요.");
                        id_join_EditText.requestFocus();
                    } else if (password_join_EditText.getText().toString().equals("")) {
                        password_join_EditText.setHint("비밀번호를 입력 해주세요.");
                        password_join_EditText.requestFocus();
                    } else if (password_join_EditText_confirm.getText().toString().equals("")) {
                        password_join_EditText_confirm.setHint("비밀번호 확인을 해주세요.");
                        password_join_EditText_confirm.requestFocus();
                    } else if (nickname_join_EditText.getText().toString().equals("")) {
                        nickname_join_EditText.setHint("닉네임을 입력 해주세요.");
                        nickname_join_EditText.requestFocus();
                    } else if (email_join_EditText.getText().toString().equals("")) {
                        email_join_EditText.setHint("이메일을 입력 해주세요.");
                        email_join_EditText.requestFocus();
                    } else if (!password_join_EditText.getText().toString().equals(password_join_EditText_confirm.getText().toString())) {
                        password_join_EditText_confirm.setText("");
                        password_join_EditText_confirm.setHint("비밀번호 확인을 다시 해주세요.");
                        password_join_EditText_confirm.requestFocus();

                    }

                    else if(id_join_EditText.getText().length() < 6 || id_join_EditText.getText().length() > 12){
                        id_join_EditText.setText("");
                        id_join_EditText.setHint("6글자~12글자 사이로 입력.");

                        id_join_EditText.requestFocus();

                    } else if(password_join_EditText.getText().toString().length() < 8 ){
                        password_join_EditText.setText("");
                        password_join_EditText_confirm.setText("");
                        password_join_EditText.setHint("8글자 이상으로 입력.");
                        password_join_EditText.requestFocus();

                    }


                    else {
                        alert = new AlertDialogManager();
                        u_id = id_join_EditText.getText().toString();
                        u_nickname = nickname_join_EditText.getText().toString();
                        //가입시 존재하는 id나 별명 체크
                        joinUserCheckID checkID = new joinUserCheckID();
                        checkID.execute();

                        joinCheckNum = (checkID.get()).trim();

                        Log.d("join", "이전 값" + checkID.get());
                        Log.d("join", "넘어온 값" + joinCheckNum);
                        if (joinCheckNum.equals("13")) {
                            alert.showAlertDialog(JoinUser.this, "오류", "이미 존재하는 ID입니다.", false);

                        } else if (joinCheckNum.equals("23")) {
                            alert.showAlertDialog(JoinUser.this, "오류", "이미 존재하는 닉네임입니다.", false);


                        } else if (joinCheckNum.equals("123")) {
                            alert.showAlertDialog(JoinUser.this, "오류", "아이디와 닉네임이 이미 존재합니다.", false);

                        } else if (joinCheckNum.equals("3")) {
                            Log.d("join", "가입 ㄱㄱ");

                            u_id = id_join_EditText.getText().toString();
                            u_password = password_join_EditText.getText().toString();
                            u_nickname = nickname_join_EditText.getText().toString();
                            u_email = email_join_EditText.getText().toString();


                            Log.d("join", "넘어가는 값" + u_id);
                            Log.d("join", "넘어가는 값" + u_password);
                            Log.d("join", "넘어가는 값" + u_nickname);
                            Log.d("join", "넘어가는 값" + u_email);

                            //위에 내용이 모두 확인되면 가입
                            joinUser joinUser = new joinUser();
                            joinUser.execute();

                            Intent i = new Intent(getApplicationContext(), CheckLoginState.class);
                            startActivity(i);
                            finish();
                            alert.showAlertDialog(JoinUser.this, "확인", "회원 가입이 완료되었습니다. 로그인 해주세요.", true);

                        } else {

                            alert.showAlertDialog(getApplicationContext(), "오류", "예기치 못한 오류.", false);
                        }
                    }
                } catch (Exception e) {
                    Log.d("TEST", "save버튼 실패");
                }
            }

            break;
            case R.id.insert_exit_Btn: {

                Log.d("TEST", "나가기버튼 눌렀음");
                finish();
            }
            break;

        }
    }

    protected class joinUser extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_joinuser.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", u_id));
                nameValuePairs.add(new BasicNameValuePair("password", u_password));
                nameValuePairs.add(new BasicNameValuePair("nickname", u_nickname));
                nameValuePairs.add(new BasicNameValuePair("email", u_email));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

                HttpResponse response = httpC.execute(httppost);
                String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);


            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return "";
        }
    }


    protected class joinUserCheckID extends AsyncTask<String, Integer, String> {
        String aa = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                HttpClient httpC = new DefaultHttpClient();


                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_joinuser_check.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", u_id));
                nameValuePairs.add(new BasicNameValuePair("nickname", u_nickname));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

                HttpResponse response = httpC.execute(httppost);
                String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                aa = resStr.trim();
                Log.v("join", "resStr" + resStr);

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return aa;
        }
    }


}
