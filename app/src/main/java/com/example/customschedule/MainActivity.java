package com.example.customschedule;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYDaySchedule;
import com.example.customschedule.DIYSetting.DIYWeek;
import com.example.customschedule.Util.ConversionPxandDP;
import com.example.customschedule.WidgetWeekSchedule.WeekSchedule;
import com.example.customschedule.mFragment.ScheduleDayFragment;
import com.example.customschedule.mFragment.ScheduleWeekFragment;
import com.example.customschedule.mFragment.ScheduleWeekRefresh;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    PopupWindow mPopupWindow;
    TabLayout TB;

    @Override
    protected void onStop() {
        super.onStop();
        //刷新桌面插件
        Intent intent = new Intent(MainActivity.this,WeekSchedule.class);
        intent.setAction("refresh");
        sendBroadcast(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region material 基本设置
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_main:
                        popUpMyOverflow();
                        break;
                }
                return true;
            }
        });
        //设置滑动菜单
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView =(NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //将原有的home隐藏并将menu图片设置为home图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        }
//        mNavigationView.setCheckedItem(R.id.nav_DIYSchedule);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_DIYSchedule:
                        break;
                    case R.id.nav_setting:
                        Intent intent = new Intent(MainActivity.this,NavSetting.class);
                        startActivity(intent);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        //侧边栏沉浸
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            //状态栏字体黑色
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //endregion



        TB = (TabLayout)findViewById(R.id.tab);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        TB.setupWithViewPager(mViewPager);
        TB.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    ScheduleDayFragment.initData();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetAll:
                //重置课表
                DataSupport.deleteAll(DIYCourses.class);
                DataSupport.deleteAll(DIYWeek.class);
                DataSupport.deleteAll(DIYDaySchedule.class);
                ScheduleDayFragment.initData();
                ScheduleWeekFragment.refreshOFMain();
                mPopupWindow.dismiss();
                break;
        }
    if (null != mPopupWindow && mPopupWindow.isShowing()){
        mPopupWindow.dismiss();
    }
    }

    public void popUpMyOverflow() {
        //获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度＋toolbar高度
        int yoffset = frame.top + toolbar.getHeight();
        //初始化
        View popView = getLayoutInflater().inflate(R.layout.popwindow_overflow, null);
        mPopupWindow= new PopupWindow(popView);
        mPopupWindow.setWidth(500);
        mPopupWindow.setHeight(190);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.showAtLocation(toolbar,Gravity.RIGHT|Gravity.TOP,20,0);
        popView.findViewById(R.id.resetAll).setOnClickListener(this);
    }

    private class MainPagerAdapter extends FragmentPagerAdapter{
        public MainPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "默认";
            if (position == 0){
                title = "天表";
            }
            if (position == 1){
                title = "周表";
            }
            return title;
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0){
                fragment = new ScheduleDayFragment();
            }else if (position == 1){
                fragment = new ScheduleWeekFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
