package com.example.customschedule.mFragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYDaySchedule;
import com.example.customschedule.Util.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import com.example.customschedule.R;

/**
 * Created by hyt on 2018/2/15.
 */

public class ScheduleDayFragment extends android.support.v4.app.Fragment {

    static String clsName;
    static String clsSite;
    static int clsStartNumber;
    static int clsCountNumber;
    static int iID;
    static int day;
    static  List<DIYDaySchedule> List_DIYDaySchedule = new ArrayList<>();
    static Context mContext;

    static LinearLayout linearLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_schedule_day,container,false);
        linearLayout = (LinearLayout) view.findViewById(R.id.ll);
        initData();
        return view;
    }
    public static void initData(){
        linearLayout.removeAllViews();
        day = DateUtil.getWeek()-1;
        if (day == 0){
            //因为星期天的数值为1
            day = 7;
        }
        List_DIYDaySchedule = DataSupport.where("day = ?",String.valueOf(day)).order("clsStartNumber").find(DIYDaySchedule.class);
        for (int i = 0; i < List_DIYDaySchedule.size();i++){
            TextView TV = new TextView(mContext);
            TV.setText("很行");
            linearLayout.addView(TV);
        }
    }
}
