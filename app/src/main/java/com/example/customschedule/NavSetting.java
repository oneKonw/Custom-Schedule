package com.example.customschedule;

import android.content.Entity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.Services.RefreshWidget;
import com.example.customschedule.WidgetWeekSchedule.WeekSchedule;

public class NavSetting extends AppCompatActivity {

    private TextView tv_openAgreenment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_setting);
        Toolbar toolbar = (Toolbar)findViewById(R.id.nav_setting_toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("设置");
        //侧边栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        toolbar.setNavigationIcon(R.drawable.ic_back64);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_openAgreenment = (TextView) findViewById(R.id.tv_oppenAgreement);
        tv_openAgreenment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAgreement = new Intent(NavSetting.this,OpenAgreement.class);
                startActivity(openAgreement);
            }
        });

        TextView tv_refresh = (TextView)findViewById(R.id.widget_refresh);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbout = new Intent(NavSetting.this,About.class);
                startActivity(intentAbout);
            }
        });
    }
}
