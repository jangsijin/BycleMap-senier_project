package com.example.coha.google.parser;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.coha.google.ListViewAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.coha.google.login.SessionManagement.currNick;

/**
 * Created by Samchan on 2016-11-16.
 */

public class Downloader extends AsyncTask<Void, Integer, String> {
    Context c;
    String address, SearchWord, sortValue;
    ListView board_ListView;
    ListViewAdapter adapter;

    String data;
    ProgressDialog dialog;

    String returnSearch;
    String returnMyBoard;

    public Downloader(Context c, String address, ListView board_ListView, ListViewAdapter listViewAdapter, String SearchWord, String sortValue) {
        this.address = address;
        this.c = c;
        this.board_ListView = board_ListView;
        this.adapter = listViewAdapter;
        this.SearchWord = SearchWord;
        this.sortValue = sortValue;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(c);
        dialog.setTitle("데이터 가져오는 중");
        dialog.setMessage("잠시만 기다려주세요.");
        dialog.show();
    }


    @Override
    protected String doInBackground(Void... params) {
        Log.d("TEST", "DownLoad!! : doInBack start");

        //전체 게시물을 가져올 것인가? Search한 게시물을 가져 올 것인가?


        //SearchWord의 초기값이 null이기 때문에, 전체 게시물을 가져온다
        if (SearchWord == null) {

            if (sortValue.equals("0") || sortValue.equals("1")) {
                data = downloadData();
            } else if (sortValue.equals("2")) {
                data = downloadSortPreData();
            } else if (sortValue.equals("3")) {
                data = downloadSortGoodData();
            } else if (sortValue.equals("4")) {
                data = downloadMyBoard();
            }

        } else {
            //Search를 시행하면 Search에 값이 저장되기때문에, 검색된 게시물만 가져온다.
            data = downloadSearchData();

        }
        return data;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("TEST", "S!! : " + s);
        Log.d("TEST", "*****************");
        dialog.dismiss();
        //doInBackground에서 return된 값이 s에 저장되었고, 그 값이 존재하면 Parser로 넘어가 Json의 형태로 구분되 변수에 저장된다.
        if (s != null) {
            Parser p = new Parser(c, board_ListView, adapter, s);
            p.execute();

        } else {
            Toast.makeText(c, "다운로드 하지 못했다.", Toast.LENGTH_SHORT).show();
        }


    }

    private String downloadData() {
        //연결, stream 가져오기

        InputStream inputStream = null;
        String line = null;

        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(con.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            if (bufferedReader != null) {

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line + "\n");
                    Log.d("TEST", "Download에서 line" + line);

                }

            } else {
                return null;
            }
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private String downloadSearchData() {
        //띄어쓰기를 기준으로 단어가 나누어지고, 그 단어를 검색하는 것이다.
        //예를 들어 '나는 짱이다' 라는 검색어가 있으면 splitWord[0]에는 '나는' splitWord[1]에는 '짱이다'가 저장된다.
        String[] splitWord = SearchWord.split("\\s");
        for (int i = 0; i < splitWord.length; i++) {
            Log.d("Search", "" + splitWord[i]);
        }

        try {

            DefaultHttpClient httpC = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ehdntjr123.dothome.co.kr/board/board_search.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            //저장된 splitWord의 갯수를 값으로 php에 보낸다.
            //이유는 다중 검색을 하기 위해서다.

            nameValuePairs.add(new BasicNameValuePair("count", String.valueOf(splitWord.length)));
            Log.d("Search", "Search Length : " + splitWord.length);

            for (int i = 0; i < splitWord.length; i++) {
                nameValuePairs.add(new BasicNameValuePair("word" + i, splitWord[i]));

                Log.d("Search", "Search값 : " + splitWord[i]);
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                    "UTF-8"));


            httpC.execute(httppost);

            //php에서 echo로 리턴된 값을 가져온다.
            //그 값은 json의 형태를 가지고 있다.
            //원래는 0이면 게시물이 없다고 Toast를 띄워야하는데 왜 안되는지 이유를 모르겠다.
            HttpResponse response = httpC.execute(httppost);
            String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            Log.d("Search", "PHP서버에서 가져온 값 : " + resStr);

            returnSearch = resStr.trim(); //가져온 값 공백 제거

            if (returnSearch.equals("0")) {
                Toast.makeText(c, "찾는 게시물이 없습니다.", Toast.LENGTH_SHORT).show();
                Log.d("Search", "값 없다." + resStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR1";
        }

        return returnSearch;

    }

    private String downloadSortGoodData() {
        //연결, stream 가져오기

        InputStream inputStream = null;
        String line = null;

        try {
            URL url = new URL("http://ehdntjr123.dothome.co.kr/board/board_sort_good.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(con.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            if (bufferedReader != null) {

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line + "\n");
                    Log.d("TEST", "Download에서 line" + line);

                }

            } else {
                return null;
            }
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private String downloadSortPreData() {
        //연결, stream 가져오기

        InputStream inputStream = null;
        String line = null;

        try {
            URL url = new URL("http://ehdntjr123.dothome.co.kr/board/board_sort_desc.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(con.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            if (bufferedReader != null) {

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuffer.append(line + "\n");
                    Log.d("TEST", "Download에서 line" + line);

                }

            } else {
                return null;
            }
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    private String downloadMyBoard() {

        try {
            DefaultHttpClient httpC = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://ehdntjr123.dothome.co.kr/board/board_sort_mine.php");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            String nickname = currNick;

            nameValuePairs.add(new BasicNameValuePair("id", nickname));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
                    "UTF-8"));
            httpC.execute(httppost);


            HttpResponse response = httpC.execute(httppost);
            String resStr = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);


            returnMyBoard = resStr.trim(); //가져온 값 공백 제거

            if (returnMyBoard.equals("0")) {
                Toast.makeText(c, "작성 게시물이 없습니다.", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR1";
        }

        return returnMyBoard;

    }
    }
