package com.example.customschedule.Util;

import java.util.Calendar;

/**
 * Created by hyt on 2018/2/16.
 */

public class DateUtil {
    static Calendar calendar ;

    public static int getWeek(){
        calendar = Calendar.getInstance();
        int resultWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if (resultWeek == 0)
            resultWeek = 7;
        return resultWeek;
        //注意1代表星期天，其余按顺序推导
    }
    public static int getDayOfYear(){

        int resultDay = calendar.get(Calendar.DAY_OF_YEAR);
        return resultDay;
    }
    public static int getWeekOfNow(int firstMonday){
        int dayOfYear = DateUtil.getDayOfYear();
        int dayOfWeek = DateUtil.getWeek();
        int mondayOfNowWeek = dayOfYear - (dayOfWeek - 1);
        int result = (mondayOfNowWeek - firstMonday) / 7 + 1;
        return result;
    }
}
