package com.example.coha.google;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class firstLayout extends Fragment {
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contanier, Bundle savedlnstanceState) {
        v = inflater.inflate(R.layout.activity_home, contanier, false);
        return v;
    }
}
