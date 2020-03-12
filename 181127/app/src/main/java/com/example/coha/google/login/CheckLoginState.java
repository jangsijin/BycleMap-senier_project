package com.example.coha.google.login;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coha.google.R;
import com.example.coha.google.push.DeleteTokenService;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.coha.google.MainBoardActivity.loginState;
import static com.example.coha.google.login.Login.loginState_ID;

/**
 * Created by Samchan on 2016-12-05.
 */

public class CheckLoginState extends Activity {
    SessionManagement session;

    // 로그아웃 버튼
    TextView user_id;
    TextView user_email;
    Button logout_Btn;

    AlertDialogManager alert = new AlertDialogManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_state);

        // 세션
        session = new SessionManagement(getApplicationContext());

        user_id = (TextView) findViewById(R.id.state_login_UID);
        user_email = (TextView) findViewById(R.id.state_login_UEmail);


        logout_Btn = (Button) findViewById(R.id.state_login_logoutBtn);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        session.checkLogin();

        //User의 정보를 key값으로 저장한다.
        HashMap<String, String> user = session.getUserDetails();

        //이름
        String name = user.get(SessionManagement.KEY_NAME);

        //이메일
        String email = user.get(SessionManagement.KEY_EMAIL);

        //로그인 부분의 메인에 해당하고, 로그인이 되어있으면 name과 email을 보여준다.
        user_id.setText(name);
        user_email.setText(email);

        logout_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity


                DeleteTokenToLogin deleteTokenToLogin = new DeleteTokenToLogin();
                deleteTokenToLogin.execute();

                loginState = 0;
                Intent intent = new Intent(getApplicationContext(),DeleteTokenService.class);
                startService(intent);

                session.logoutUser();

            }
        });
    }


    //사실 굳이 사용자의 토큰을 지우지 않아도 된다. 하지만, 그냥 지워보자.
    protected class DeleteTokenToLogin extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                Log.d("fcm", "Getting new token");
                HttpClient httpC = new DefaultHttpClient();

                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/register_token_delete.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", loginState_ID));
                Log.d("fcm", "delete id : "+loginState_ID);


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







}
