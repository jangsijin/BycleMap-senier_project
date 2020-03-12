package com.example.coha.google.reply;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.coha.google.R;

/**
 * Created by Samchan on 2016-11-27.
 */

public class ReplyListItemView extends LinearLayout {
    TextView reply_ID;
    TextView reply_Time;
    TextView reply_Good;
    TextView reply_text;

    public ReplyListItemView(Context context) {
        super(context);
        init(context);
    }

    public ReplyListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.reply_item, this, true);
        Log.d("REPLY", "되냐?");
        reply_ID = (TextView) findViewById(R.id.reply_ID_textView);
        reply_Time = (TextView) findViewById(R.id.reply_Time_textView);
        reply_Good = (TextView) findViewById(R.id.reply_Good_textView);
        reply_text = (TextView) findViewById(R.id.reply_Text_textView);


    }

    public void setContents(int index, String reply) {

        if (index == 1) {
            reply_ID.setText(reply);
        } else if (index == 2) {
            reply_Time.setText(reply);
        } else if (index == 3) {
            reply_Good.setText(reply);

        } else if (index == 4) {
            reply_text.setText(reply);

        } else {


        }

    }
}
