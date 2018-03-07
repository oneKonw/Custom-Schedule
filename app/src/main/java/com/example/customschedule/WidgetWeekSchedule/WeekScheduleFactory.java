package com.example.customschedule.WidgetWeekSchedule;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import com.example.customschedule.DIYSetting.DIYDaySchedule;
import com.example.customschedule.R;

import org.litepal.crud.DataSupport;

/**
 * Created by hyt on 2018/3/1.
 */

public class WeekScheduleFactory  implements RemoteViewsService.RemoteViewsFactory{

    private Context context;
    static List<DIYDaySchedule> List_day1 = new ArrayList<>();
    static List<DIYDaySchedule> List_day2 = new ArrayList<>();
    static List<DIYDaySchedule> List_day3 = new ArrayList<>();
    static List<DIYDaySchedule> List_day4 = new ArrayList<>();
    static List<DIYDaySchedule> List_day5 = new ArrayList<>();

    public WeekScheduleFactory(Context context, Intent intent){
        this.context = context;
    }

    @Override
    public void onCreate() {

        List_day1 = DataSupport.where("day = ?","1").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day2 = DataSupport.where("day = ?","2").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day3 = DataSupport.where("day = ?","3").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day4 = DataSupport.where("day = ?","4").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day5 = DataSupport.where("day = ?","5").order("clsstartnumber").find(DIYDaySchedule.class);
    }

    /*
     * 当调用notifyAppWidgetViewDataChanged方法时，触发这个方法
     * 例如：MyRemoteViewsFactory.notifyAppWidgetViewDataChanged();
     */
    @Override
    public void onDataSetChanged() {
        List_day1.clear();
        List_day2.clear();
        List_day3.clear();
        List_day4.clear();
        List_day5.clear();
        List_day1 = DataSupport.where("day = ?","1").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day2 = DataSupport.where("day = ?","2").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day3 = DataSupport.where("day = ?","3").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day4 = DataSupport.where("day = ?","4").order("clsstartnumber").find(DIYDaySchedule.class);
        List_day5 = DataSupport.where("day = ?","5").order("clsstartnumber").find(DIYDaySchedule.class);
    }

    @Override
    public void onDestroy() {
        List_day1.clear();
        List_day2.clear();
        List_day3.clear();
        List_day4.clear();
        List_day5.clear();
    }

    @Override
    public int getCount() {
        return 1;
    }

    /*
     * 创建并且填充，在指定索引位置显示的View，这个和BaseAdapter的getView类似
     */
    @Override
    public RemoteViews getViewAt(int position) {

        //最终remoteview
        RemoteViews rv_final = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_list);
        rv_final.addView(R.id.widget_week_ll_period,setPeriod());
        rv_final.addView(R.id.widget_week_ll_day1,setListItemview(List_day1));
        rv_final.addView(R.id.widget_week_ll_day2,setListItemview(List_day2));
        rv_final.addView(R.id.widget_week_ll_day3,setListItemview(List_day3));
        rv_final.addView(R.id.widget_week_ll_day4,setListItemview(List_day4));
        rv_final.addView(R.id.widget_week_ll_day5,setListItemview(List_day5));

        return rv_final;
    }

    /*
     * 显示一个"加载"View。返回null的时候将使用默认的View
     */
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }
    /*
     * 不同View定义的数量。默认为1（本人一直在使用默认值）
     * ps:此处定义的是item的总量，也就是你的子布局有多少种
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    /*
     * 如果每个项提供的ID是稳定的，即她们不会在运行时改变，就返回true（没用过。。。）
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    public RemoteViews setListItemview (List<DIYDaySchedule> List_day_temp){
        int pastNumber = 0;


        RemoteViews result = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_day);

        for (int i = 0; i < List_day_temp.size(); i++){
            int clsStartNumber = List_day_temp.get(i).getClsStartNumber();
            int clsCountNumber = List_day_temp.get(i).getClsCountNumber();
            String clsName = List_day_temp.get(i).getClsName();
            String clsSite = List_day_temp.get(i).getClsSite();
            //填充与上个textview之间的空白
            int isNull = clsStartNumber - pastNumber;
            if (isNull != 0){
                result.addView(R.id.widget_week_ll_day,isNull(isNull));
            }
            pastNumber = clsStartNumber + clsCountNumber;

            String content = clsName + "@" + clsSite;
            RemoteViews itemTv = tvType(clsCountNumber,content);
            result.addView(R.id.widget_week_ll_day,itemTv);
        }

        return result;
    }

    public RemoteViews tvType(int count,String content ){
        //默认为2
        RemoteViews rv = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_item2);
        rv.setTextViewText(R.id.widget_week_tv_itme_1,"出错");
        if (count == 1){
            rv = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_item1);
            rv.setTextViewText(R.id.widget_week_tv_itme_1,content);
            return rv;
        }
        if (count == 2){
            rv = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_item2);
            rv.setTextViewText(R.id.widget_week_tv_itme_2,content);
            return rv;
        }
        if (count == 3){
            rv = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_item3);
            rv.setTextViewText(R.id.widget_week_tv_itme_3,content);
            return rv;
        }
        if (count == 4){
            rv = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_item4);
            rv.setTextViewText(R.id.widget_week_tv_itme_4,content);
            return rv;
        }
        return rv;
    }

    @TargetApi(16)
    public RemoteViews isNull(int count){

        RemoteViews result = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_day);
        RemoteViews nullItem = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_itemnull);

        for (int i = 0; i < count; i++){
            result.addView(R.id.widget_week_ll_day,nullItem);
        }
        return result;
    }

    public RemoteViews setPeriod(){
        RemoteViews result = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_day);
        for (int i = 0; i < 12; i++){
            RemoteViews rv_period_item = new RemoteViews(context.getPackageName(),R.layout.widget_weekschedule_period_item);
            rv_period_item.setTextViewText(R.id.widget_period,String.valueOf(i+1));
            result.addView(R.id.widget_week_ll_day,rv_period_item);
        }
        return result;
    }
}
