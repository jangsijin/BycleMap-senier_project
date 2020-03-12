package com.example.coha.google;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Samchan on 2016-11-15.
 */


//ListView와 아이템을 이어주는 Adapter
public class ListViewAdapter extends BaseAdapter {

    private Context bContext;

    public ArrayList<BoardItem> items = new ArrayList<BoardItem>();


    public ListViewAdapter(Context context) {
        this.bContext = context;
    }

    public void clear() {
        items.clear();
    }

    public void addItem(BoardItem item) {
        //게시물 하나를 추가하는데 필요한 부분 BoardItem에 아이템 하나를 저장한다.
        items.add(item);
    }

    public void removeItem(int position) {
        //게시물 하나를 삭제하는데 필요한 부분
        items.remove(position);
    }

    @Override
    public int getCount() {
        Log.d("TEST", "Adapter 2 : " + items.size());

        return items.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d("TEST", "Adapter 3 : " + position);
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.d("TEST", "Adapter 4 : " + position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Log.d("TEST", "Adapter 5");
        BoardListItemView view = null;

        if (convertView == null) {
            view = new BoardListItemView(bContext);
        } else {

            view = (BoardListItemView) convertView;

        }

        //BoardListItemView에 정보를 전달. 아이템 하나마다 번호,제목,날짜를 저장한다.
        Log.d("TEST", "positionpositionpositionposition" + String.valueOf(position));
        view.setContents(0, ((String) items.get(position).getData(0)));
        view.setContents(1, ((String) items.get(position).getData(1)));
        view.setContents(2, ((String) items.get(position).getData(2)));

        Log.d("TEST", "////////////////////////////////////////////////////////////////////");
//        view.setContents(3, ((String) boardItem.getData(3)));
//        view.setContents(4, ((String) boardItem.getData(4)));
        return view;
    }
}
