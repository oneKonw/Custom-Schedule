package com.example.customschedule.WidgetWeekSchedule;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by hyt on 2018/3/1.
 */

public class WeekScheduleService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WeekScheduleFactory(this.getApplicationContext(),intent);
    }
}
