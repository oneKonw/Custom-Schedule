package com.example.customschedule.Util;

import java.util.Calendar;

/**
 * Created by hyt on 2018/2/16.
 */

public class DateUtil {
    public static int getWeek(){
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return i;
        //注意1代表星期天，其余按顺序推导
    }
}
