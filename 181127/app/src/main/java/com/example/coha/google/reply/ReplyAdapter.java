package com.example.coha.google.reply;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Samchan on 2016-11-28.
 */

public class ReplyAdapter extends BaseAdapter {

    private Context rContext;

    public ArrayList<ReplyItem> rItems = new ArrayList<ReplyItem>();

    public ReplyAdapter(Context context) {
        this.rContext = context;
    }


    public void clear() {
        rItems.clear();
    }

    public void addItem(ReplyItem item) {
        rItems.add(item);
    }

    public void removeItem(int position) {
        rItems.remove(position);
    }


    @Override
    public int getCount() {
        return rItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("REPLY","겟뷰");

        ReplyListItemView view = null;
        if (convertView == null) {
            view = new ReplyListItemView(rContext);
        } else {

            view = (ReplyListItemView) convertView;

        }

        view.setContents(0, ((String) rItems.get(position).getData(0)));
        view.setContents(1, ((String) rItems.get(position).getData(1)));
        view.setContents(2, ((String) rItems.get(position).getData(2)));
        view.setContents(3, ((String) rItems.get(position).getData(3)));
        view.setContents(4, ((String) rItems.get(position).getData(4)));
        view.setContents(5, ((String) rItems.get(position).getData(5)));
        return view;
    }
}
