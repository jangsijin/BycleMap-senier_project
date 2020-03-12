package com.example.coha.google;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Samchan on 2016-11-15.
 * ListView 아이템 하나의 레이아웃을 보여주는 곳이다.
 */

public class BoardListItemView extends LinearLayout{
    TextView num_textView;
    TextView title_textView;
    TextView time_textView;

    public BoardListItemView(Context context) {
        super(context);
        init(context);
    }

    public BoardListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){

        //아이템 하나의 레이아웃 실행
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board_item,this,true);

        num_textView = (TextView)findViewById(R.id.num_textView); //게시물 번호
        title_textView = (TextView)findViewById(R.id.title_textView);//게시물 제목
        time_textView = (TextView)findViewById(R.id.time_textView);//게시물 저장 시간


    }

    /**
     * 여기서 아이템을 각각 저장해서 보여준다.
     * **/
    public void setContents(int index, String board) {
        if (index == 0) {
            num_textView.setText(board);
        } else if (index == 1) {
            title_textView.setText(board);
        } else if(index == 2){
            time_textView.setText(board);

        } else if(index == 3){


        } else if(index == 4){


        }else {
            Log.d("TEST","오류?.");
            throw new IllegalArgumentException();
        }
    }

}
