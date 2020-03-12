package com.example.coha.google;


import android.util.Log;

/**
 * Created by Samchan on 2016-11-15.
 */

public class BoardItem {

    private String[] boardData;

    public BoardItem(String[] boardData) {
        this.boardData = boardData;
    }

    //Parser에서 넘어온 정보를 여기에 저장한 뒤, Adapter와 연결해 Listview에 보여준다.
    public BoardItem(int index ,String title, String time, String id, String good,String memo,String goodcheck,String boardToken){
        boardData = new String[10];
        String number = String.valueOf(index);
        boardData[0] = number;
        boardData[1] = title;
        boardData[2] = time;
        boardData[3] = id;
        boardData[4] = good;
        boardData[5] = memo;
        boardData[6] = goodcheck;
        boardData[7] = boardToken;

        for (int i = 0; i<6;i++) {
            Log.d("TEST", "BoardData["+ i + "] : " + boardData[i]);
        }
    }


    public String getData(int index){
        if(boardData ==null || index>=boardData.length){
            return null;
        }

        return boardData[index];

    }
}
