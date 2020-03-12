package com.example.coha.google.reply;


/**
 * Created by Samchan on 2016-11-28.
 */

public class ReplyItem {


    private String[] replyData;

    public ReplyItem(String[] replyData) {
        this.replyData = replyData;
    }

    public ReplyItem(int index, String id, String time, String good, String text){
        replyData = new String[10];
        String number = String.valueOf(index);
        replyData[0] = number;
        replyData[1] = id;
        replyData[2] = time;
        replyData[3] = good;
        replyData[4] = text;

    }

    public String getData(int index){
        if(replyData ==null || index>=replyData.length){
            return null;
        }

        return replyData[index];

    }
}
