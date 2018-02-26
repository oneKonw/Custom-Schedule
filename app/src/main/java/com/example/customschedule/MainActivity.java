package com.example.customschedule;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYWeek;
import com.example.customschedule.Util.ConversionPxandDP;
import com.example.customschedule.mFragment.ScheduleDayFragment;
import com.example.customschedule.mFragment.ScheduleWeekFragment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

@TargetApi(16)
public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置滑动菜单
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavigationView =(NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //将原有的home隐藏并将menu图片设置为home图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
//        mNavigationView.setCheckedItem(R.id.nav_DIYSchedule);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_DIYSchedule:
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

        TabLayout TB = (TabLayout)findViewById(R.id.tab);
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
