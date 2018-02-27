package com.example.customschedule.mFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customschedule.DIYSchedule;
import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYDaySchedule;
import com.example.customschedule.DIYSetting.DIYWeek;
import com.example.customschedule.R;
import com.example.customschedule.Util.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hyt on 2018/2/15.
 */
public class ScheduleWeekFragment extends android.support.v4.app.Fragment {

    int iID ,weekNow;
    private static RelativeLayout day1;
    private static RelativeLayout day2;
    private static RelativeLayout day3;
    private static RelativeLayout day4;
    private static RelativeLayout day5;
    private static RelativeLayout day6;
    private static RelativeLayout day7;
//    private Button btn_delete;
    private TextView tv_nowWeek;
    List<DIYCourses> List_DIYCourses = new ArrayList<>();
    List<DIYDaySchedule>List_DIYDaySchedule = new ArrayList<>();
    ScheduleWeekRefresh scheduleWeekRefresh;
    View view;

    protected Context mContent;
    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_schedule_week,container,false);
        scheduleWeekRefresh = new ScheduleWeekRefresh(mContent,view);

        day1 = (RelativeLayout) view.findViewById(R.id.day1);
        day2 = (RelativeLayout) view.findViewById(R.id.day2);
        day3 = (RelativeLayout)view. findViewById(R.id.day3);
        day4 = (RelativeLayout) view.findViewById(R.id.day4);
        day5 = (RelativeLayout) view.findViewById(R.id.day5);
        day6 = (RelativeLayout) view.findViewById(R.id.day6);
        day7 = (RelativeLayout) view.findViewById(R.id.day7);
//        btn_delete = (Button)view.findViewById(R.id.btn_delete);
        tv_nowWeek = (TextView) view.findViewById(R.id.tab_tv_nowweek);

        //读取第一周，如果为零则不进行计算，直接设置为第一周，不为零则进行计算，设置为当前周
        SharedPreferences getFirstMonday = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
        int firstMonday = getFirstMonday.getInt("firstDayofSemester",0);
        if (firstMonday != 0) {
            SharedPreferences.Editor editorSetWeekNow = mContent.getSharedPreferences("getWeek",MODE_PRIVATE).edit();
            //防止周数超过25后出现闪退
            if (DateUtil.getWeekOfNow(firstMonday) < 26){
                editorSetWeekNow.putInt("weekNow", DateUtil.getWeekOfNow(firstMonday)-1);
                editorSetWeekNow.apply();
            }else{
                editorSetWeekNow.putInt("weekNow", 0);
                editorSetWeekNow.apply();
            }
        }
        //读取当前星期值
        SharedPreferences pref = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
        weekNow = pref.getInt("weekNow",0);
        scheduleWeekRefresh.refresh(weekNow);

//        refreshShow(weekNow);
        tv_nowWeek.setText("第"+String.valueOf(weekNow+1)+"周");


        tv_nowWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_SelectWeek dialog_selectWeek = new Dialog_SelectWeek(mContent,view,tv_nowWeek);
                dialog_selectWeek.show();
                //读取当前星期值
                SharedPreferences pref = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
                weekNow = pref.getInt("weekNow",0);
                scheduleWeekRefresh.refresh(weekNow);
//                refreshShow(weekNow);

            }
        });

//        spinner_nowWeek = (Spinner) view.findViewById(R.id.spinner_nowWeek);
        //当前星期选择

//        spinner_nowWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //储存星期
//                SharedPreferences.Editor editor = mContent.getSharedPreferences("getWeek",MODE_PRIVATE).edit();
//                editor.putInt("weekNow",position);
//                editor.apply();
//                refreshShow(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        spinner_nowWeek.setSelection(weekNow);

        //星期设置
        daySetOnClick(day1,0);
        daySetOnClick(day2,1);
        daySetOnClick(day3,2);
        daySetOnClick(day4,3);
        daySetOnClick(day5,4);
        daySetOnClick(day6,5);
        daySetOnClick(day7,6);

        //重置课程表
//        btn_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DataSupport.deleteAll(DIYCourses.class);
//                DataSupport.deleteAll(DIYWeek.class);
//                scheduleWeekRefresh.refresh(0);
////                refreshShow(0);
////                spinner_nowWeek.setSelection(0);
//            }
//        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void daySetOnClick(RelativeLayout rl, final int day){
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchiID();
                Intent intent_day = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("start",day);
                bundle.putInt("iID",iID);
                bundle.putString("clsName","");
                bundle.putString("clsSite","");
                intent_day.putExtras(bundle);
                intent_day.setClass(mContent,DIYSchedule.class);
                startActivity(intent_day);
            }
        });
    }

    /**
     * 查询iID的值
     */
    private void searchiID(){
        //查询表中最后一条数据的iID
        DIYCourses foriID = DataSupport.findLast(DIYCourses.class);
        if (foriID == null){
            iID = 1;
        }else {
            iID = foriID.getiId()+1;
        }
    }
    /**
     * 刷新show页面
     */

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新页面
        SharedPreferences pref = mContent.getSharedPreferences("getWeek",MODE_PRIVATE);
        weekNow = pref.getInt("weekNow",0);

//        spinner_nowWeek.setSelection(weekNow);
        //这里少了一个刷新
        scheduleWeekRefresh.refresh(weekNow);
//        refreshShow(weekNow);
    }


    public static void refreshOFMain(){
        day1.removeAllViews();
        day2.removeAllViews();
        day3.removeAllViews();
        day4.removeAllViews();
        day5.removeAllViews();
        day6.removeAllViews();
        day7.removeAllViews();
    }

}
