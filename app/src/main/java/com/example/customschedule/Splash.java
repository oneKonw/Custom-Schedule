package com.example.customschedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    SharedPreferences pref = getSharedPreferences("getweek",MODE_PRIVATE);
//    int conut = pref.getInt("count",0);
//    if (conut == 0){
//        Intent intent = new Intent(Splash.this,MainActivity.class);
//        startActivity(intent);
//        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
//        editor.putInt("count",1);
//        editor.apply();
//    }else {
        Intent intent = new Intent(Splash.this,MainActivity.class);
        startActivity(intent);
//    }
        this.finish();
    }

}
