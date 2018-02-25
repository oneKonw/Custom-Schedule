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

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by hyt on 2018/2/15.
 */
public class ScheduleWeekFragment extends android.support.v4.app.Fragment {

    int iID ,weekNow;
    private RelativeLayout day1;
    private  RelativeLayout day2;
    private  RelativeLayout day3;
    private  RelativeLayout day4;
    private  RelativeLayout day5;
    private  RelativeLayout day6;
    private  RelativeLayout day7;
    private Button btn_delete;
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
        btn_delete = (Button)view.findViewById(R.id.btn_delete);
        tv_nowWeek = (TextView) view.findViewById(R.id.tab_tv_nowweek);
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
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(DIYCourses.class);
                DataSupport.deleteAll(DIYWeek.class);
                scheduleWeekRefresh.refresh(0);
//                refreshShow(0);
//                spinner_nowWeek.setSelection(0);
            }
        });

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

    //region refresh
//    public void refreshShow(int position){
//        //清空天表
//        DataSupport.deleteAll(DIYDaySchedule.class);
//        //清除所有view
//        day1.removeAllViews();
//        day2.removeAllViews();
//        day3.removeAllViews();
//        day4.removeAllViews();
//        day5.removeAllViews();
//        day6.removeAllViews();
//        day7.removeAllViews();
//        //重新加载
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
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 2) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day2, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 3) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day3, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 4) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day4, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 5) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day5, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 6) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day6, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                } else if (tempDay == 7) {
//                    setTextView(tempTxt, tempID, tempNumber * 50, day7, tempCountNumber);
//                    setDaySchedule(tempClsName,tempClsSite,tempNumber,tempCountNumber,tempID,tempDay);
//                }
//            }
//        }
//    }
//
//    private void setDaySchedule(String clsName,String clsSite,int clsStartNumber,int clsCountNumber,int iID,int day){
//        DIYDaySchedule diyDaySchedule = new DIYDaySchedule();
//        diyDaySchedule.setClsName(clsName);
//        diyDaySchedule.setClsSite(clsSite);
//        diyDaySchedule.setClsStartNumber(clsStartNumber);
//        diyDaySchedule.setClsCountNumber(clsCountNumber);
//        diyDaySchedule.setiID(iID);
//        diyDaySchedule.setDay(day);
//        diyDaySchedule.save();
//        List_DIYDaySchedule.add(diyDaySchedule);
//    }
//    //传入相关数值设置text的参数，并将textview设置到布局上
//    @TargetApi(16)
//    public void setTextView(String text, final int id, int dp, RelativeLayout r1, int countNumber ){
//        final TextView tv1 = new TextView(mContent);
//        tv1.setText(text);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        int topMargin = ConversionPxandDP.dip2px(mContent,dp);
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
//            Toast.makeText(mContent, "课程节数设置错误", Toast.LENGTH_SHORT).show();
//        }
//
//        tv1.setLayoutParams(params);
//        tv1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog showdialog = new Dialog_ShowMessage(mContent,id,v);
//                showdialog.show();
////                DataSupport.delete(DIYCourses.class,id);
////                ViewGroup parent = (ViewGroup) v.getParent();
////                parent.removeView(v);
//            }
//        });
//        r1.addView(tv1);
//    }
//endregion
}
