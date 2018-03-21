package com.example.customschedule;

import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Intent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customschedule.DIYSetting.DIYCourses;
import com.example.customschedule.DIYSetting.DIYWeek;
import com.example.customschedule.Util.MyDatabaseHelper;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportSchedule extends AppCompatActivity {

    public int iID;
    public String urlStuName;
    public String stuName="";
    private String strId = "";
    private String strPassword = "";
    private String strViewstate = "dDwyODE2NTM0OTg7Oz4EdrhGRK2tAP2OLlvKpVtfzXS17g==";
    private String strViewTor = "92719903";
    private String  strRbtList = "%D1%A7%C9%FA";
    private String button1 = "";
    private String hidPdrs = "";
    private String  hidsc = "";
    private String lblanguage = "";
    private String txtsecretcode = "";

    public String regularWeek;
    public String txtClsName;//名称
    public String txtClsSite;//地点
    public int txtday;//星期几
    public int txtNumber;//节数
    public int txtCountNumber = 0;//总共几节


    private MyDatabaseHelper dbHelper;
    EditText edit_account,edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_schedule);

        dbHelper = new MyDatabaseHelper(this,"Schedule.db",null,3);
        searchiID();

        TextView demoSure = (TextView) findViewById(R.id.demo_sure);
        edit_account = (EditText)findViewById(R.id.edit_account);
        edit_password = (EditText)findViewById(R.id.edit_password);

        demoSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空数据
                DataSupport.deleteAll(DIYCourses.class);
                DataSupport.deleteAll(DIYWeek.class);
                if (edit_account.getText().toString().equals("")||edit_password.getText().toString().equals("")){
                    Toast.makeText(ImportSchedule.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }else {
                    strId = edit_account.getText().toString();
                    strPassword = edit_password.getText().toString();
                    getScheduleFromWeb();
                }
            }
        });
    }

    private void getScheduleFromWeb(){
        //利用okhttp封装获取课表
        OkHttpUtils
                .post()
                .url("http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/default2.aspx")
                .addParams("__VIEWSTATE",strViewstate)
                //.addParams("__VIEWSTATEGENERATOR",strViewTor)
                .addParams("Button1",button1).addParams("hidPdrs",hidPdrs).addParams("hidsc",hidsc)
                .addParams("lbLanguage",lblanguage)
                .addParams("RadioButtonList1",strRbtList).addParams("TextBox2",strPassword)
                .addParams("txtSecretCode",txtsecretcode)
                .addParams("txtUserName",strId)
                .addHeader("Host","210.38.162.117")
                .addHeader("Referer","http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    }
                    @Override
                    public void onResponse(String response) {
                        boolean loadState = getLoadState(response);
                        if (loadState){
                            urlStuName = getUrlByGb2312(stuName);
                            String url = "http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/tjkbcx.aspx?xh="+strId+"&xm="+urlStuName+"&gnmkdm=N121601";
                            String referer = "http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/xs_main.aspx?xh="+strId;
                            OkHttpUtils
                                    .get()
                                    .url(url)
//                                .url("http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/xskbcx.aspx?xh=151080123&xm=%BA%E9%D2%F8%CC%CE&gnmkdm=N121603")
//                                .addHeader("Referer", "http://210.38.162.117/(gsox2muncbsmwifbasaxnj45)/xs_main.aspx?xh=151080123")
                                    .addHeader("Referer",referer)
                                    .addHeader("Host", "210.38.162.117")
                                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                        }
                                        @Override
                                        public void onResponse(String response) {
                                            parseStringHtml(response);
                                            Toast.makeText(ImportSchedule.this, "导入完成", Toast.LENGTH_LONG).show();
//                                            ClipboardManager cm = (ClipboardManager) getSystemService(MyApplication.getContext().CLIPBOARD_SERVICE);
//                                            cm.setText(response);

                                        }
                                    });
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                      demoImport.setText(stuName);
////                                    Intent intent = new Intent(LoginSquare.this, MainActivity.class);
////                                    intent.putExtra("account", strId);
////                                    intent.putExtra("password",strPassword);
////                                    intent.putExtra("stuName",stuName);
////                                    startActivity(intent);
//                                }
//                            });
                        }else {
                            Toast.makeText(ImportSchedule.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     *判断是否成功登入
     */
    private boolean getLoadState(String html){
        Document document = Jsoup.parse(html);

        Element elementName = document.getElementById("xhxm");
        Elements elementTitle = document.getElementsByTag("title");
        Element title = elementTitle.first();
        String strTitle = title.html();
        if (strTitle.equals("欢迎使用正方教务管理系统！请登录")){
            return false;
        }
        String strelementName = elementName.html();
        Pattern patternName = Pattern.compile("(?<=\\s)\\w+");
        Matcher matcherName = patternName.matcher(strelementName);
        matcherName.find();
        String longStrName = matcherName.group(0);
        stuName = longStrName.substring(0,longStrName.length()-2);

        return true;
    }

    /**
     * url中文编码
     */
    private String getUrlByGb2312(String str){
        String encodeStr = "";
        try {
            encodeStr = URLEncoder.encode(str, "gb2312");
//            String decodeStr = URLDecoder.decode(encodeStr, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 解析课表
     */
    private void parseStringHtml(String html){
        String tdStr,tdStrOfHtml;
        Document document = Jsoup.parse(html);
        Element elementTable1 = document.getElementById("Table6");
        Elements trs = elementTable1.select("tr");
        //去除前两组tr，前两组tr是星期几和早晨行
        trs.remove(0);
        trs.remove(0);
        //遍历tr
        for (int i = 0; i<trs.size();i++){
            //nbsp用于计算当前的td是星期几,根据正方系统html表格的留空来计算
            int nbsp = 0;
            Element tr = trs.get(i);
            Elements tds = tr.select("td[align]");
            for (int j = 0; j < tds.size(); j++){

                Element td = tds.get(j);
                tdStr = td.text();

                //&nbsp占一个英文字符，别的课不止一个字符，

                if (tdStr.length() == 1){
                    nbsp++;
                }else {
                    nbsp++;
                    if (nbsp==4){
                        nbsp =4;
                    }
                    int lessonCount = 0;
                    //检测同一个时间点存在多少节课
                    tdStrOfHtml = td.html();
                    Pattern checkLesson = Pattern.compile("<\\w+><\\w+><\\w+>");
                    Matcher matcherCheckLesson = checkLesson.matcher(tdStrOfHtml);
                    while (matcherCheckLesson.find()){
                        lessonCount++;
                    }
                    if (lessonCount != 0){
                        String [] tdStrs = tdStrOfHtml.split("<br><br><br>");
                        for (int n = 0; n <= lessonCount;n++ ){
                            String tdStr1 = tdStrs[n];
                            //替换<br>为" ";
                            Pattern replaseBr = Pattern.compile("<br>");
                            Matcher matcherReplaseBr = replaseBr.matcher(tdStr1);
                            tdStr1 = matcherReplaseBr.replaceAll(" ");

                            //名称地点解析
                            regularSchedlue(tdStr1);

                            txtday = nbsp;
                            txtNumber = i;
                            txtCountNumber = Integer.parseInt(td.attr("rowspan"));

                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("txtClsName",txtClsName);
                            values.put("txtClsSite",txtClsSite);
                            values.put("txtday",nbsp);
                            values.put("txtNumber",txtNumber);
                            values.put("txtCountNumber",txtCountNumber);
                            values.put("iID",iID);
                            db.insert("DIYcourses",null,values);
                            values.clear();
                            iID++;
                        }
                    }else {
                        //名称地点解析
                        regularSchedlue(tdStr);

                        txtday = nbsp;
                        txtNumber = i;
                        txtCountNumber = Integer.parseInt(td.attr("rowspan"));

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("txtClsName",txtClsName);
                        values.put("txtClsSite",txtClsSite);
                        values.put("txtday",nbsp);
                        values.put("txtNumber",txtNumber);
                        values.put("txtCountNumber",txtCountNumber);
                        values.put("iID",iID);
                        db.insert("DIYcourses",null,values);
                        values.clear();
                        iID++;
                    }


                }
                //解析td
//                if (tdStr.length() != 1){
//                    String nameAndCls = regularSchedlue(tdStr);
//                    Course course = new Course();
//                    course.setClsName(nameAndCls);
//                    course.setClsNum(i+1);
//                    course.setDay(j+1);
//                    if (td.attr("rowspan")!=""){
//                        course.setClsCont(Integer.valueOf(td.attr("rowspan")));
//                        if (j<5){
//                            course.setWidgetNumber(widget);
//                        }else{
//                            course.setWidgetNumber(100);
//                        }
//
//                    }
//                    else{
//                        course.setClsCont(1);
//                        course.setWidgetNumber(100);
//                    }
//                    course.save();
//                    courses.add(course);
//                }
            }
        }

    }

    /**
     *正则表达式
     */
    public void regularSchedlue(String str){
        //也可以用\S+(?=\s)直接分成四段
        String strCls = "";
        Pattern patternName = Pattern.compile("^.+?(\\s{1})");
        Matcher matcherName = patternName.matcher(str);
        matcherName.find();
        String strName = matcherName.group(0);
        Pattern patternCls = Pattern.compile("(?<=\\s)\\w+\\d");
        Matcher matcherCls = patternCls.matcher(str);
        if (matcherCls.find()){
            strCls = matcherCls.group(0);
        }
        Pattern patternWeek = Pattern.compile("(?<=/)\\S+");
        Matcher matcherWeek = patternWeek.matcher(str);
        matcherWeek.find();

        regularWeek = matcherWeek.group(0);
        txtClsName = strName;
        txtClsSite = strCls;

        regularAndSaveWeek(regularWeek);
    }

    /**
     *周数解析
     * */
    public void regularAndSaveWeek(String str){
        SQLiteDatabase dbWeek = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("iID",iID);

        int weekStart = 0;int weekEnd = 0;
        String oddOrEvenOfEven = "";
        String tempWeek;

        Pattern patternSaveWeek = Pattern.compile("\\w+");
        Matcher matcherSaveWeek = patternSaveWeek.matcher(str);
        int count = 0;
        while (matcherSaveWeek.find()){
            if (count == 0) {
                oddOrEvenOfEven = matcherSaveWeek.group();
            }else if (count == 1){
                 weekStart = Integer.parseInt( matcherSaveWeek.group());
            }else if (count == 2){
                 weekEnd = Integer.parseInt(matcherSaveWeek.group());
            }
            count++;
        }
        //循环1-25，没有课的周数插入0
        for (int i = 1; i <26; i++){
            if (i > weekEnd || i < weekStart){
                tempWeek = "week"+String.valueOf(i);
                values.put(tempWeek,0);
            }else {
                if (oddOrEvenOfEven.equals("周")){
                    tempWeek = "week"+String.valueOf(i);
                    values.put(tempWeek,i);

                }else if (oddOrEvenOfEven.equals("单周")){
                    if (i%2==1){
                        tempWeek = "week"+String.valueOf(i);
                        values.put(tempWeek,i);
                    }else {
                        tempWeek = "week"+String.valueOf(i);
                        values.put(tempWeek,0);
                    }
                }else if (oddOrEvenOfEven.equals("双周")){
                    if (i%2==0){
                        tempWeek = "week"+String.valueOf(i);
                        values.put(tempWeek,i);
                    }else{
                        tempWeek = "week"+String.valueOf(i);
                        values.put(tempWeek,0);
                    }
                }
            }

        }
        dbWeek.insert("DIYWeek",null,values);
        values.clear();
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

}
