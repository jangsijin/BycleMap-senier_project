package com.example.coha.google;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    }


    public void onButtonClicked(View v) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onButton4Clicked(View v) {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }

    public void onButton5Clicked(View v){
        Intent intent = new Intent (this,NoticeActivity.class);
        startActivity(intent);
    }

    public void onButton3Clicked(View v) {
        Intent intent = new Intent(this, MainBoardActivity.class);
        startActivity(intent);
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    View v;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup contanier, Bundle savedlnstanceState) {
        v = inflater.inflate(R.layout.activity_main, contanier, false);
        return v;

    }

}