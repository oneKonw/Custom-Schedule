package com.example.customschedule.mFragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYDaySchedule;
import com.example.customschedule.Dialog_ShowMessage;
import com.example.customschedule.R;
import com.example.customschedule.Util.ConversionPxandDP;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyt on 2018/2/24.
 */

public class ScheduleWeekRefresh {
    private Context context;
    private View view;
    private RelativeLayout day1;
    private  RelativeLayout day2;
    private  RelativeLayout day3;
    private  RelativeLayout day4;
    private  RelativeLayout day5;
    private  RelativeLayout day6;
    private  RelativeLayout day7;
    List<DIYCourses> List_DIYCourses = new ArrayList<>();
    List<DIYDaySchedule>List_DIYDaySchedule = new ArrayList<>();

    ScheduleWeekRefresh (Context context, View view){
        this.context = context;
        this.view = view;
        day1 = (RelativeLayout) view.findViewById(R.id.day1);
        day2 = (RelativeLayout) view.findViewById(R.id.day2);
        day3 = (RelativeLayout)view. findViewById(R.id.day3);
        day4 = (RelativeLayout) view.findViewById(R.id.day4);
        day5 = (RelativeLayout) view.findViewById(R.id.day5);
        day6 = (RelativeLayout) view.findViewById(R.id.day6);
        day7 = (RelativeLayout) view.findViewById(R.id.day7);
    }

    public void refresh(int position){
        //清空天表
        DataSupport.deleteAll(DIYDaySchedule.class);
        //清除所有view
        day1.removeAllViews();
        day2.removeAllViews();
        day3.removeAllViews();
        day4.removeAllViews();
        day5.removeAllViews();
        day6.removeAllViews();
        day7.removeAllViews();
        //重新加载
        List_DIYCourses = DataSupport.findAll(DIYCourses.class);
        for (int i=0; i<List_DIYCourses.size(); i++) {
            DIYCourses tempCourse = List_DIYCourses.get(i);
            String tempClsName = tempCourse.getTxtClsName();
            String tempClsSite = tempCourse.getTxtClsSite();
            int tempDay = tempCourse.getTxtday();
            int tempNumber = tempCourse.getTxtNumber();
            int tempCountNumber = tempCourse.getTxtCountNumber();
            int tempID = tempCourse.getiId();//iID
            //此处可以进行优化，将课程名和地点名集合到settextview方法中，可以顺带优化输入的逻辑
            String tempTxt = tempClsName + "@" + tempClsSite;
            //用原生查询查
            //先利用iID查询相应课程的position相对应的周是否为0
            Cursor cursor = DataSupport.findBySQL("select * from DIYWeek where iID = ?",String.valueOf(tempID));
            cursor.moveToFirst();
            String getweek = "week"+String.valueOf(position+1);
            //getcolumnindex获取对应列的index值
            int show = cursor.getInt(cursor.getColumnIndex(getweek));
            //如果不为零则显示
            if (show!=0){
                if (tempDay == 1) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day1, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 2) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day2, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 3) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day3, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 4) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day4, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 5) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day5, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 6) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day6, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                } else if (tempDay == 7) {
                    setTextView(tempTxt, tempID, tempNumber * 50, day7, tempCountNumber);
                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
                }
            }
        }

    }

    private void setDaySchedule(String clsName,String clsSite,int clsStartNumber,int clsCountNumber,int iID,int day){
        DIYDaySchedule diyDaySchedule = new DIYDaySchedule();
        diyDaySchedule.setClsName(clsName);
        diyDaySchedule.setClsSite(clsSite);
        diyDaySchedule.setClsStartNumber(clsStartNumber);
        diyDaySchedule.setClsCountNumber(clsCountNumber);
        diyDaySchedule.setiID(iID);
        diyDaySchedule.setDay(day);
        diyDaySchedule.save();
        List_DIYDaySchedule.add(diyDaySchedule);
    }

    //传入相关数值设置text的参数，并将textview设置到布局上
    @TargetApi(16)
    public void setTextView(String text, final int id, int dp, RelativeLayout r1, int countNumber ){
        final TextView tv1 = new TextView(context);
        tv1.setText(text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int topMargin = ConversionPxandDP.dip2px(context,dp);
        params.setMargins(0,topMargin,0,0);

        //根据课节数选择样式
        if (countNumber == 1){
            tv1.setBackground(context.getResources().getDrawable(R.drawable.diytextview));
        }else if (countNumber == 2){
            tv1.setBackground(context.getResources().getDrawable(R.drawable.diytextview2));
        }else if (countNumber == 3){
            tv1.setBackground(context.getResources().getDrawable(R.drawable.diytextview3));
        }else if (countNumber == 4){
            tv1.setBackground(context.getResources().getDrawable(R.drawable.diytextview4));
        }else {
            //课程左边比右边大的错误处理方法
            Toast.makeText(context, "课程节数设置错误", Toast.LENGTH_SHORT).show();
        }

        tv1.setLayoutParams(params);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog showdialog = new Dialog_ShowMessage(context,id,v);
                showdialog.show();
//                DataSupport.delete(DIYCourses.class,id);
//                ViewGroup parent = (ViewGroup) v.getParent();
//                parent.removeView(v);
            }
        });
        r1.addView(tv1);
    }
}
