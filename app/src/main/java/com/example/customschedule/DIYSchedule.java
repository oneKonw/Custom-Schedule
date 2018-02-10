package com.example.customschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYWeek;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.LinkagePicker;

public class DIYSchedule extends AppCompatActivity {
    List<DIYCourses> List_DIYCourses = new ArrayList<>();

    private String txtClsName;//名称
    private String txtClsSite;//地点
    private int txtday;//星期几
    private int txtNumber;//节数
    private int txtCountNumber = 2;//默认值为2
    private int iID;//外键

    private Dialog_ChooseWeek showdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diy_schedule_setting);

        final Button btnConfirm = (Button) findViewById(R.id.bt_confirm);
        final EditText ClsName = (EditText) findViewById(R.id.tv_ClsName);
        final EditText ClsSite = (EditText)findViewById(R.id.tv_ClsSite);
//        Button btn_clsName = (Button) findViewById(R.id.btn_clsNumber);
        Spinner spinner_day = (Spinner) findViewById(R.id.spinner_day);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        int defaultDay = b.getInt("start");
        iID = b.getInt("iID");
        txtClsName = b.getString("clsName");
        txtClsSite = b.getString("clsSite");
        ClsName.setText(txtClsName);
        ClsSite.setText(txtClsSite);
        spinner_day.setSelection(defaultDay,true);//设置星期默认值

        //周数选择
        findViewById(R.id.showdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同时传入iID
                showdialog = new Dialog_ChooseWeek(DIYSchedule.this,String.valueOf(iID));
                showdialog.show();
            }
        });
        //获取星期几的值
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] days = getResources().getStringArray(R.array.day);
                String temp_day = days[position];
                if(temp_day.equals("星期一")){
                    txtday = 1;
                }else if(temp_day.equals("星期二")){
                    txtday = 2;
                }else if(temp_day.equals("星期三")){
                    txtday = 3;
                }else if (temp_day.equals("星期四")){
                    txtday = 4;
                }else if (temp_day.equals("星期五")){
                    txtday = 5;
                }else if (temp_day.equals("星期六")){
                    txtday = 6;
                }else if (temp_day.equals("星期天")){
                    txtday = 7;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtday = 1;
            }
        });
        //获取节数的值
        //region 获取节数
//        btn_clsName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                final ArrayList<String> firstData = new ArrayList<>();
////                for (int i = 1; i<13;i++){
////                    firstData.add(String.valueOf(i));
////                }
////                final ArrayList<String> secondData = firstData;
////
////                final DoublePicker picker = new DoublePicker(DIYSchedule.this, firstData, secondData);
////                picker.setLineSpaceMultiplier(2.0f);//设置行间距离
////                picker.setCanceledOnTouchOutside(true);
////                picker.setDividerVisible(true);
////                picker.setCycleDisable(true);
////                picker.setPadding(60);
////                picker.setSelectedIndex(0,1);
////                picker.setFirstLabel(null, null);
////                picker.setSecondLabel("至", null);
////                picker.setTextSize(15);
////                picker.setContentPadding(10,10);
////                picker.setTitleText("节数选择");
////                //先将txtNumber的默认值设置为2，因为只有用户多picker进行了点击才能触发listener,如果用户没有进行点击，则直接为2
////                picker.setOnPickListener(new DoublePicker.OnPickListener() {
////                    @Override
////                    public void onPicked(int selectedFirstIndex, int selectedSecondIndex) {
////                        txtNumber = Integer.parseInt(firstData.get(selectedFirstIndex).toString())-1;
////
////                        int tempTxtCountNumber = Integer.parseInt(secondData.get(selectedSecondIndex)) - Integer.parseInt(firstData.get(selectedFirstIndex).toString());
////                        if (tempTxtCountNumber < 0){
////                            txtCountNumber = 0;
////                            Toast.makeText(DIYSchedule.this, "请确保右边的数字比做左边大", Toast.LENGTH_SHORT).show();
////                        }else if (tempTxtCountNumber > 4){
////                            txtCountNumber = 4;
////                            Toast.makeText(DIYSchedule.this,"节数最大值只能为4", Toast.LENGTH_SHORT).show();
////                        } else {
////                            txtCountNumber = tempTxtCountNumber + 1;
////                        }
////                    }
////                });
//
//            }
//        });
        //endregion
        /**
         * 确定按钮
         * 通过iID查询对应的数据是否存在判定用户是否保存了周数
         * 如果周数没有设置则提示用户进行设置
         */
        //region 确定按钮设置
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DIYWeek> searchID = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYWeek.class);
                List<DIYCourses> DIYCourses_update = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYCourses.class);
                if (searchID.size() == 0){
                    Toast.makeText(DIYSchedule.this, "周数没有设置，请设置周数", Toast.LENGTH_SHORT).show();
                }else{
                    //获取课程名，上课地点，开始周数
                    txtClsName = ClsName.getText().toString();
                    txtClsSite = ClsSite.getText().toString();
                    if (txtCountNumber != 0){
                        DIYCourses DIYCourse = new DIYCourses();
                        DIYCourse.setTxtClsName(txtClsName);
                        DIYCourse.setTxtClsSite(txtClsSite);
                        DIYCourse.setTxtday(txtday);
                        DIYCourse.setTxtCountNumber(txtCountNumber);
                        DIYCourse.setTxtNumber(txtNumber);
                        DIYCourse.setiId(iID);
                        //不为空则存在通iID数据，直接进行修改
                        if (DIYCourses_update.size() != 0){
                            DIYCourse.updateAll("iID = ?",String.valueOf(iID));
                        }else{
                            //为空则不在，直接新建数据与
                            DIYCourse.save();
                            if (DIYCourse.save()){
                                Toast.makeText(DIYSchedule.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DIYSchedule.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                            List_DIYCourses.add(DIYCourse);
                        }
                        finish();
                    }
                }
            }
        });
        //endregion
        Button btn_cancel = (Button)findViewById(R.id.diy_setting_btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DIYCourses> DIYCourses_delete = DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYCourses.class);
                List<DIYWeek>DIYWeeks_delete =  DataSupport.where("iID = ?",String.valueOf(iID)).find(DIYWeek.class);
                if (DIYCourses_delete.size()!=0 || DIYWeeks_delete.size() != 0){
                    //courses可能没有值
                    DataSupport.deleteAll(DIYWeek.class, "iID = ?", String.valueOf(iID));//清除原有的值
                    DataSupport.deleteAll(DIYCourses.class, "iID = ?", String.valueOf(iID));//清除原有的值
                }
                finish();
            }
        });
    }
    public void onLinkagePicker(View view) {
        //联动选择器的更多用法，可参见AddressPicker和CarNumberPicker
        LinkagePicker.DataProvider provider = new LinkagePicker.DataProvider() {

            @Override
            public boolean isOnlyTwo() {
                return true;
            }

            @NonNull
            @Override
            public List<String> provideFirstData() {
                ArrayList<String> firstList = new ArrayList<>();
                for (int i = 1; i < 13; i++){
                    firstList.add(String.valueOf(i));
                }
                return firstList;
            }
            @NonNull
            @Override
            public List<String> provideSecondData(int firstIndex) {
                ArrayList<String> secondList = new ArrayList<>();
                for (int i =firstIndex + 1; i< firstIndex+5 && i<13 ; i++){
                    String str = String.valueOf(i);
                    secondList.add(str);
                }
                return secondList;
            }

            @Nullable
            @Override
            public List<String> provideThirdData(int firstIndex, int secondIndex) {
                return null;
            }

        };
        LinkagePicker picker = new LinkagePicker(this, provider);
        picker.setCycleDisable(true);
        picker.setLineSpaceMultiplier(2.0f);//设置行间距离
        picker.setCanceledOnTouchOutside(true);
        picker.setDividerVisible(true);
        picker.setPadding(60);
        picker.setUseWeight(true);
        picker.setLabel("至",null);
        picker.setSelectedIndex(0, 1);
        //picker.setSelectedItem("12", "9");
        picker.setContentPadding(10, 10);
        picker.setTextSize(15);
        picker.setTitleText("节数选择");
        picker.setGravity(Gravity.CENTER);
        picker.setOnStringPickListener(new LinkagePicker.OnStringPickListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                txtNumber = Integer.parseInt(first)-1;
                int tempTxtCountNumber = Integer.parseInt(second) - Integer.parseInt(first);
                txtCountNumber = tempTxtCountNumber + 1;
            }
        });
        picker.show();
    }




}
