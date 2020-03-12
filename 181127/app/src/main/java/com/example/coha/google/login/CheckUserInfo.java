package com.example.coha.google.login;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samchan on 2016-12-05.
 */



public class CheckUserInfo extends AsyncTask<String, Integer, String> {
    Context mContext;
    String uid,upassword;
    String returnAnswer;


    private String address = "http://ehdntjr123.dothome.co.kr/board/board_login_test.php";

    public CheckUserInfo(Context context, String id, String password) {
        this.mContext = context;
        this.uid = id;
        this.upassword = password;
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            DefaultHttpClient httpC = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(address);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("id",uid));
            nameValuePairs.add(new BasicNameValuePair("password",upassword));

            Log.d("login", "LoginPage -> id 값 : " + uid);
            Log.d("login", "LoginPage -> pw 값 : " + upassword);


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                    "UTF-8"));


            httpC.execute(httppost);

            HttpResponse response = httpC.execute(httppost);
            String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            Log.v("login" , "PHP서버에서 가져온 값 : " + resStr);

            returnAnswer = resStr;

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR1";
        }

        return returnAnswer;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
