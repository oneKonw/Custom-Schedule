package com.example.customschedule;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.customschedule.DIYSetting.DIYWeek;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyt on 2018/2/1.
 */

public class Dialog_ChooseWeek extends Dialog {
    Context context ;
    private String iID;
    public Dialog_ChooseWeek(Context context,String id){
        super(context);
        this.context = context;
        iID = id;
    }
    public Dialog_ChooseWeek(Context context,int theme){
        super(context,theme);
    }


    private RadioButton radio_odd;
    private RadioButton radio_even;
    private RadioButton radio_all;
    private TableLayout tab;
    private TextView yes;
    private TextView no;

    List<DIYWeek> list_DIYWeek = new ArrayList<>();
    DIYWeek DIYWeeks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_week);
        setCanceledOnTouchOutside(false);//不能点击空白处取消

        tab = (TableLayout)findViewById(R.id.table_week);
         final DIYWeek publicDIYWeek = initColumn(tab,0);

        radio_odd = (RadioButton)findViewById(R.id.radio_odd);
        radio_even = (RadioButton)findViewById(R.id.radio_even);
        radio_all = (RadioButton)findViewById(R.id.radio_all);
        setRadioEvent(radio_odd,1);
        setRadioEvent(radio_even,2);
        setRadioEvent(radio_all,3);

        yes = (TextView)findViewById(R.id.txtChooseWeek_yes);
        no = (TextView)findViewById(R.id.txtChooseWeek_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DataSupport.deleteAll(DIYWeek.class);
                DataSupport.deleteAll(DIYWeek.class,"iID = ?",String.valueOf(iID));
                Dialog_ChooseWeek.this.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询id，存在则更新，不存在则创建
//                List<DIYWeek> searchID = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYWeek.class);
//                if (searchID.size() == 0){
//                }
                Dialog_ChooseWeek.this.dismiss();
            }
        });
    }
    @TargetApi(16)
    /**
     * 为所有checkbox设置监听时间
     * choose用于选择方法执行的模式，0为默认，1为奇数，2为偶数，3为全部,4为重置
     */
    private DIYWeek initColumn (TableLayout tab,int choose){
        if (choose == 1||choose==2){
            initColumn(tab,4);
        }
        //新建数据库,如果不存在iID，则新建，如果存在，就删除再重建
        if (choose == 0){
            List<DIYWeek> searchID = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYWeek.class);
            if (searchID.size() == 0){
                DIYWeeks = new DIYWeek();
                DIYWeeks.setiId(Integer.parseInt(iID));
            }else {
                DataSupport.deleteAll(DIYWeek.class, "iID = ?", String.valueOf(iID));//清除原有的值
                DIYWeeks = new DIYWeek();
                DIYWeeks.setiId(Integer.parseInt(iID));
            }

        }

        int child = tab.getChildCount();
        //遍历每一个row
        for (int i = 0 ; i < child ; i++){
            if (tab.getChildAt(i)instanceof TableRow){
                int child2 = ((TableRow) tab.getChildAt(i)).getChildCount();
                //遍历每一个checkbox
                for (int j = 0; j < child2 ; j++){
                    //默认执行
                    if (choose == 0){
                        if (((TableRow) tab.getChildAt(i)).getChildAt(j)instanceof CheckBox) {
                            //先将checkbox相对应的数据库周设置为默认值0
                            final String defaultWeek = "week"+((CheckBox)((TableRow) tab.getChildAt(i)).getChildAt(j)).getText().toString();
                            DIYWeeks.setToDefault(defaultWeek);//设置为默认值
                            DIYWeeks.save();
                            //为每一个textbox添加监听事件
                            ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked == true){
                                        //数据储存
                                        ContentValues valuesadd =new ContentValues();
                                        //将checkbox的text写入数据库
                                        valuesadd.put(defaultWeek,Integer.parseInt(buttonView.getText().toString()));
                                        DataSupport.updateAll(DIYWeek.class,valuesadd,"iID = ?",String.valueOf(iID));
                                    } else if (isChecked == false){
                                        //数据删除
                                        ContentValues valuesde =new ContentValues();
                                        //设置为默认值
                                        valuesde.put(defaultWeek,0);
                                        DataSupport.updateAll(DIYWeek.class,valuesde,"iID = ?",String.valueOf(iID));
                                    }
                                }
                            });
                        }
                    }
                    //奇数
                    if (choose == 1){
                        //先全部重置
                        //获取checkbox上的文本
                        String strOdd = ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).getText().toString();
                        //文本如果为奇数
                        if (!judgeNumber(strOdd)){
                            //则进行勾选
                            ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).setChecked(true);
                            //并对勾选的进行存储
                        }
                    }
                    //偶数
                    if (choose == 2){
                        //先全部重置
                        //获取checkbox上的文本
                        String strOdd = ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).getText().toString();
                        //文本如果为偶数
                        if (judgeNumber(strOdd)){
                            //则进行勾选
                            ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).setChecked(true);
                            //并对勾选的进行存储
                        }
                    }
                    //全部
                    if (choose == 3){
                        //全部勾选
                        ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).setChecked(true);
                        //并进行存储
                    }
                    //重置清空全部
                    if (choose == 4){
                        ((CheckBox) ((TableRow) tab.getChildAt(i)).getChildAt(j)).setChecked(false);
                    }
                }
            }
        }

        return DIYWeeks;
    }
    /**
     *为RadioButton设定监听事件
     */
    private void setRadioEvent (RadioButton tempRadio, final int orera){
        tempRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initColumn(tab,orera);
            }
        });
    }
    /**
     * 预留数据存储方法
     */
    private void setOrDeleteDate(boolean save){

    }
    /**
     * 判定奇偶
     * 奇false偶true
     */
    private boolean judgeNumber(String tempNumber){
        boolean result = true;
        //如果为奇数
        if (Integer.parseInt(tempNumber)%2 == 1){
            result = false;
        }
        //如果是偶数
        if (Integer.parseInt(tempNumber)%2 == 0){
            result = true;
        }
        return result;
    }

}
