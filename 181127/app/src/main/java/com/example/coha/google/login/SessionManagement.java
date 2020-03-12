package com.example.coha.google.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.coha.google.MainBoardActivity;

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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Samchan on 2016-12-05.
 */


public class SessionManagement {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "chanwoo";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_NAME = "name";

    public static final String KEY_EMAIL = "email";

    public static  String currNick = null;

    public static String makeBoardToken = null;

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession( String nickname, String email){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, nickname);

        editor.putString(KEY_EMAIL, email);

        editor.commit();
    }

    //로그인 상태를 확인하고 로그인이 되어 있지 않으면 login 클래스로 이동한다.
    public void checkLogin(){

        if(!this.isLoggedIn()){

            Intent i = new Intent(_context, Login.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }

    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        currNick = pref.getString(KEY_NAME, null);
        Log.d("login","currNick KEY-NAME" + currNick);

        getBoardToken getToken = new getBoardToken();
        getToken.execute();

        return user;
    }


    public void logoutUser(){

        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainBoardActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }


    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    protected class getBoardToken extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpClient httpC = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "http://ehdntjr123.dothome.co.kr/board/board_getToken.php");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                Log.d("login" , " 보낸 값 : " + currNick);
                nameValuePairs.add(new BasicNameValuePair("id", currNick));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                        "UTF-8"));
                httpC.execute(httppost);

                HttpResponse response = httpC.execute(httppost);
                String getToken = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

                Log.d("login" , "가져온 토큰 값1 : " + getToken);

                makeBoardToken = getToken;

            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR1";
            }

            return makeBoardToken;
        }
    }
}