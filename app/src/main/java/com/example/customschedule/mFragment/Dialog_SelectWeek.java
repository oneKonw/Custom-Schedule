package com.example.customschedule.mFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.R;
import com.example.customschedule.Util.DateUtil;
import com.example.customschedule.Util.RecyclerAdapter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hyt on 2018/2/24.
 */

public class Dialog_SelectWeek extends Dialog implements MyItemClickListener{
    Context mContext;
    private RecyclerView Rv;
    private ArrayList<String> listItem;
    private RecyclerAdapter myAdapter;
    private View view;
    private TextView tv;

    public Dialog_SelectWeek(Context context, View view, TextView tv){
        super(context);
        mContext = context;
        this.view = view;
        this.tv = tv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectweek);
        initData();
        initView();

    }
    public void initView(){
        Rv = (RecyclerView)findViewById(R.id.dialog_selectweek_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        Rv.setLayoutManager(layoutManager);
        Rv.setHasFixedSize(true);

        myAdapter = new RecyclerAdapter(mContext,listItem);
        //将重写方法的接口传入adater
        myAdapter.setOnItemClickListenner(this);
        Rv.setAdapter(myAdapter);
    }
    public void initData(){

        listItem = new ArrayList<>();
        for (int i=1; i < 26; i++){
            listItem.add("第" + String.valueOf(i) + "周");
        }
    }

    //对继承的接口的方法进行重写，并且把重写的方法传入adater
    @Override
    public void onItemClick(View view, int postion) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("getWeek",MODE_PRIVATE).edit();
        editor.putInt("weekNow",postion);
        //此处推算写入第一周的日期
        int dayOfYear = DateUtil.getDayOfYear();
        int dayOfWeek = DateUtil.getWeek();
        int mondayOfNowWeek = dayOfYear - (dayOfWeek - 1);
        int firstDayOfSemester = mondayOfNowWeek - postion * 7;
        //如果设置错误，强制设置当前周为第一周
        if (firstDayOfSemester < 0){
            firstDayOfSemester = mondayOfNowWeek;
            Toast.makeText(mContext, "请正确设置周数，已将本周设置为第一周", Toast.LENGTH_SHORT).show();
        }
        //写入当前学期第一天的日期
        editor.putInt("firstDayofSemester",firstDayOfSemester);
        editor.apply();
        tv.setText("第"+String.valueOf(postion+1)+"周");
        final ScheduleWeekRefresh scheduleWeekRefresh = new ScheduleWeekRefresh(mContext,this.view);
        scheduleWeekRefresh.refresh(postion);
        this.dismiss();
    }
}
