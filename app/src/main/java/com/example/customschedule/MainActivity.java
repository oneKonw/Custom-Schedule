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
        mNavigationView.setCheckedItem(R.id.nav_DIYSchedule);
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

        //region 原本的代码
//        day1 = (RelativeLayout) findViewById(R.id.day1);
//        day2 = (RelativeLayout) findViewById(R.id.day2);
//        day3 = (RelativeLayout) findViewById(R.id.day3);
//        day4 = (RelativeLayout) findViewById(R.id.day4);
//        day5 = (RelativeLayout) findViewById(R.id.day5);
//        day6 = (RelativeLayout) findViewById(R.id.day6);
//        day7 = (RelativeLayout) findViewById(R.id.day7);
//        btn_delete = (Button)findViewById(R.id.btn_delete);
//        spinner_nowWeek = (Spinner) findViewById(R.id.spinner_nowWeek);
//        //当前星期选择
//
//        spinner_nowWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //储存星期
//                SharedPreferences.Editor editor = getSharedPreferences("getWeek",MODE_PRIVATE).edit();
//                editor.putInt("weekNow",position);
//                editor.apply();
//                refreshShow(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        //读取当前星期值
//        SharedPreferences pref = getSharedPreferences("getWeek",MODE_PRIVATE);
//        int weekNow = pref.getInt("weekNow",0);
//        spinner_nowWeek.setSelection(weekNow);
//
//        //星期设置
//        daySetOnClick(day1,0);
//        daySetOnClick(day2,1);
//        daySetOnClick(day3,2);
//        daySetOnClick(day4,3);
//        daySetOnClick(day5,4);
//        daySetOnClick(day6,5);
//        daySetOnClick(day7,6);
//
//        //重置课程表
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataSupport.deleteAll(DIYCourses.class);
//                DataSupport.deleteAll(DIYWeek.class);
//                refreshShow(0);
//                spinner_nowWeek.setSelection(0);
//            }
//        }
    }
//    private void daySetOnClick(RelativeLayout rl, final int day){
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchiID();
//                Intent intent_day = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putInt("start",day);
//                bundle.putInt("iID",iID);
//                bundle.putString("clsName","");
//                bundle.putString("clsSite","");
//                intent_day.putExtras(bundle);
//                intent_day.setClass(MainActivity.this,DIYSchedule.class);
//                startActivity(intent_day);
//            }
//        });
//    }
//    //传入相关数值设置text的参数，并将textview设置到布局上
//    public void setTextView(String text, final int id, int dp, RelativeLayout r1, int countNumber ){
//        final TextView tv1 = new TextView(this);
//        tv1.setText(text);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int topMargin = ConversionPxandDP.dip2px(MainActivity.this,dp);
//        params.setMargins(0,topMargin,0,0);
//
//        //根据课节数选择样式
//        if (countNumber == 1){
//            tv1.setBackground(getResources().getDrawable(R.drawable.diytextview));
//        }else if (countNumber == 2){
//            tv1.setBackground(getResources().getDrawable(R.drawable.diytextview2));
//        }else if (countNumber == 3){
//            tv1.setBackground(getResources().getDrawable(R.drawable.diytextview3));
//        }else if (countNumber == 4){
//            tv1.setBackground(getResources().getDrawable(R.drawable.diytextview4));
//        }else {
//            //课程左边比右边大的错误处理方法
//            Toast.makeText(MainActivity.this, "课程节数设置错误", Toast.LENGTH_SHORT).show();
//            tv1.setText("");
//        }
//
//        tv1.setLayoutParams(params);
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog showdialog = new Dialog_ShowMessage(MainActivity.this,id,v);
//                showdialog.show();
////                DataSupport.delete(DIYCourses.class,id);
////                ViewGroup parent = (ViewGroup) v.getParent();
////                parent.removeView(v);
//            }
//        });
//        r1.addView(tv1);
//    }
//    /**
//     * 查询iID的值
//     */
//    private void searchiID(){
//        //查询表中最后一条数据的iID
//        DIYCourses foriID = DataSupport.findLast(DIYCourses.class);
//        if (foriID == null){
//            iID = 1;
//        }else {
//            iID = foriID.getiId()+1;
//        }
//    }
//
//    /**
//     * 刷新show页面
//     */
//    public void refreshShow(int position){
//        //先清除所有
//        day1.removeAllViews();
//        day2.removeAllViews();
//        day3.removeAllViews();
//        day4.removeAllViews();
//        day5.removeAllViews();
//        day6.removeAllViews();
//        day7.removeAllViews();
//        //后重新加载
//        List_DIYCourses = DataSupport.findAll(DIYCourses.class);
//        for (int i=0; i<List_DIYCourses.size(); i++) {
//            DIYCourses tempCourse = List_DIYCourses.get(i);
//            String tempClsName = tempCourse.getTxtClsName();
//            String tempClsSite = tempCourse.getTxtClsSite();
//            int tempDay = tempCourse.getTxtday();
//            int tempNumber = tempCourse.getTxtNumber();
//            int tempCountNumber = tempCourse.getTxtCountNumber();
//            int tempID = tempCourse.getiId();//iID
//            //此处可以进行优化，将课程名和地点名集合到settextview方法中，可以顺带优化输入的逻辑
//            String tempTxt = tempClsName + "@" + tempClsSite;
//            //用原生查询查
//            //先利用iID查询相应课程的position相对应的周是否为0
//            Cursor cursor = DataSupport.findBySQL("select * from DIYWeek where iID = ?",String.valueOf(tempID));
//            cursor.moveToFirst();
//            String getweek = "week"+String.valueOf(position+1);
//            //getcolumnindex获取对应列的index值
//            int show = cursor.getInt(cursor.getColumnIndex(getweek));
//            //如果不为零则显示
//            if (show!=0){
//                if (tempDay == 1) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day1, tempCountNumber);
//                } else if (tempDay == 2) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day2, tempCountNumber);
//                } else if (tempDay == 3) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day3, tempCountNumber);
//                } else if (tempDay == 4) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day4, tempCountNumber);
//                } else if (tempDay == 5) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day5, tempCountNumber);
//                } else if (tempDay == 6) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day6, tempCountNumber);
//                } else if (tempDay == 7) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day7, tempCountNumber);
//                }
//            }
//        }
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //刷新页面
//        SharedPreferences pref = getSharedPreferences("getWeek",MODE_PRIVATE);
//        int weekNow = pref.getInt("weekNow",0);
//        spinner_nowWeek.setSelection(weekNow);
//        refreshShow(weekNow);
//    }
    //endregion

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
