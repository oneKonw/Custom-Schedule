package com.example.customschedule;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.Util.ConversionPxandDP;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by hyt on 2018/2/6.
 */

public class Dialog_ShowMessage extends Dialog {
    private int iID;
    private Context context;
    private View v;
    public Dialog_ShowMessage(Context context,int iID,View v){
        super(context);
        this.iID = iID;
        this.context = context;
        this.v = v;
    }

    private String clsName;
    private String clsSite;
    private String clsNumber;
    private TextView tv_clsName;
    private TextView tv_clsSite;
    private TextView tv_clsNumber;
    private Button btn_setting;

    @TargetApi(16)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_showmessage);
        //数据查询
        List<DIYCourses> List_DIYCourses = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYCourses.class);
        final DIYCourses diyCourses =  List_DIYCourses.get(0);//因为对应的iID之可能有一个，直接获取相关数据行
        /**
         *获取课程名称
         */
        tv_clsName =(TextView)findViewById(R.id.dialog_tv_ClsName);
        clsName = diyCourses.getTxtClsName();
        tv_clsName.setText(clsName);
        /**
         * 获取教室
         */
        tv_clsSite = (TextView) findViewById(R.id.dialog_tv_clsSite);
        clsSite = diyCourses.getTxtClsSite();
        tv_clsSite.setText(clsSite);
        /**
         * 获取所在节数（可有可无）
         */
        tv_clsNumber = (TextView)findViewById(R.id.dialog_tv_Number);
        int startNumber = diyCourses.getTxtNumber();
        int countNumber = diyCourses.getTxtCountNumber();
        int start = startNumber + 1;
        int end = startNumber+countNumber;
        clsNumber = String.valueOf(start)+"—"+String.valueOf(end)+"节";
        tv_clsNumber.setText(clsNumber);

        /**
         *生成25个txtview
         */
        RelativeLayout rl_weeks = (RelativeLayout)findViewById(R.id.dialog_rl_weeks);
        for (int i = 1; i < 26; i++){
            TextView tv = new TextView(getContext());
            String week = "week"+String.valueOf(i);
            Cursor cursor = DataSupport.findBySQL("select * from DIYWeek where iID = ?",String.valueOf(iID));
            cursor.moveToFirst();
            int show = cursor.getInt(cursor.getColumnIndex(week));
            //show=0的时候无法为text赋值，直接赋值为当前的i
            if (show != 0){
                int top = show/5;
                if (show%5 == 0){
                    top = show/5-1;
                }
                int left = show%5-1;
                if (left == -1){
                    left = 4;
                }

                int margintop = ConversionPxandDP.dip2px(getContext(),top*50);
                int marginleft = ConversionPxandDP.dip2px(getContext(),left*50);

                //运用param方法对tv进行生成
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(marginleft,margintop,0,0);
                tv.setLayoutParams(params);
                tv.setBackground(getContext().getResources().getDrawable(R.drawable.dialog_message_week));
            }else if (show == 0){
                show = i;
                int top = show/5;
                if (show%5 == 0){
                    top = show/5-1;
                }
                int left = show%5-1;
                if (left == -1){
                    left = 4;
                }
                int margintop = ConversionPxandDP.dip2px(getContext(),top*50);
                int marginleft = ConversionPxandDP.dip2px(getContext(),left*50);
                //运用param方法对tv进行生成
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(marginleft,margintop,0,0);
                tv.setLayoutParams(params);
                tv.setBackground(getContext().getResources().getDrawable(R.drawable.dialog_message_week0));
            }
            tv.setText(String.valueOf(show));
            tv.setGravity(17);
            rl_weeks.addView(tv);
        }
        /**
         * 编辑按钮
         */
        btn_setting = (Button)findViewById(R.id.dialog_btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ViewGroup parent = (ViewGroup) v.getParent();
//                parent.removeView(v);
//                DataSupport.deleteAll(DIYCourses.class,"iID = ?",String.valueOf(iID));
//                DataSupport.deleteAll(DIYWeek.class,"iID = ?",String.valueOf(iID));
                Dialog_ShowMessage.this.dismiss();
                int day = diyCourses.getTxtday() - 1;
                int iID = diyCourses.getiId();
                Intent intent_editor = new Intent();
                Bundle bundle_editor = new Bundle();
                bundle_editor.putInt("start",day);
                bundle_editor.putInt("iID",iID);
                bundle_editor.putString("clsName",clsName);
                bundle_editor.putString("clsSite",clsSite);
                intent_editor.putExtras(bundle_editor);
                intent_editor.setClass(context,DIYSchedule.class);
                getContext().startActivity(intent_editor);
            }
        });


    }
}
