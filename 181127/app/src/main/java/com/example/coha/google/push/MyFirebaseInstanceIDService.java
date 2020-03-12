package com.example.coha.google.push;


/**
 * Created by Samchan on 2017-01-11.
 */


import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.example.coha.google.MainBoardActivity.loginState;
import static com.example.coha.google.login.Login.loginState_ID;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";


    @Override
    public void onTokenRefresh() {

        // 이미 토큰을 받았다면 다시 사용되지 않음.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("fcm", "1 Refreshed token: " + token);
        Log.d("fcm", "2");

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        if(loginState == 0) { //비회원으로 등록
            sendRegistrationToNotLogin(token); //notlogin db에 저장.
            Log.d("fcm", "3 : " + token);
            Log.d("fcm", "3 : 비회원 상태로 저장");

        }else if(loginState == 1){
            sendRegistrationToLogin(token,loginState_ID);
            Log.d("fcm", "4 : 로그인 상태로 저장");

        }
    }

    private void sendRegistrationToNotLogin(String token) {
        // Add custom implementation, as needed.

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://ehdntjr123.dothome.co.kr/board/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRegistrationToLogin(String token,String loginID) {
        // Add custom implementation, as needed.
        Log.d("fcm", "4 : token : " + token);
        Log.d("fcm", "4 : id : "+ loginState_ID);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .add("id",loginID)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://ehdntjr123.dothome.co.kr/board/register_login.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
