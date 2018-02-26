package com.example.customschedule.mFragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.HashMap;
import java.util.List;
import com.example.customschedule.R;

/**
 * Created by hyt on 2018/2/15.
 */

public class ScheduleDayFragment extends android.support.v4.app.Fragment {

    static String clsName;
    static String clsSite;
    static String  clsNumber;
    static int clsStartNumber;
    static int clsCountNumber;
    static int iID;
    static int day;
    static  List<DIYDaySchedule> List_DIYDaySchedule = new ArrayList<>();
    static ArrayList<HashMap<String,Object>> listItem;
    static AdapterDaySchedule adapterDaySchedule;
    static RecyclerView recyclerView;
    static Context mContext;


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
        recyclerView = (RecyclerView) view.findViewById(R.id.tab_day_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        initData();
        return view;
    }

    public static void initData(){
        //清空

        //
        listItem = new ArrayList<HashMap<String, Object>>();

        day = DateUtil.getWeek()-1;
        if (day == 0){
            //因为星期天的数值为1
            day = 7;
        }
        //读取当天的数据
        List_DIYDaySchedule = DataSupport.where("day = ?",String.valueOf(day)).order("clsStartNumber").find(DIYDaySchedule.class);
        for (int i = 0; i < List_DIYDaySchedule.size();i++) {
            clsName = List_DIYDaySchedule.get(i).getClsName();
            clsSite = List_DIYDaySchedule.get(i).getClsSite();
            clsStartNumber = List_DIYDaySchedule.get(i).getClsStartNumber();
            clsCountNumber = List_DIYDaySchedule.get(i).getClsCountNumber();
            int start = clsStartNumber + 1;
            int end = clsStartNumber + clsCountNumber;
            clsNumber = String.valueOf(start) + "—" + String.valueOf(end) + "节";

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("clsName", clsName);
            map.put("clsSite", clsSite);
            map.put("clsNumber", clsNumber);
            listItem.add(map);
        }
        //实例化adapter
        adapterDaySchedule = new AdapterDaySchedule(listItem);
        recyclerView.setAdapter(adapterDaySchedule);
    }
}
