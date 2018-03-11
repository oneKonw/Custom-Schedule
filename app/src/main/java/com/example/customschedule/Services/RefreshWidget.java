package com.example.customschedule.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.customschedule.MainActivity;
import com.example.customschedule.MyApplication;
import com.example.customschedule.Util.DateUtil;
import com.example.customschedule.WidgetWeekSchedule.WeekSchedule;

public class RefreshWidget extends Service {
    int second = 12 * 60 * 60 * 1000;
    public RefreshWidget() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent refreshWidget = new Intent(MyApplication.getContext(),WeekSchedule.class);
        refreshWidget.setAction("refresh");
        sendBroadcast(refreshWidget);



        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //识别当前是周一则将刷新时间改为每周刷新一次，不然每天刷新一次
        if (DateUtil.getWeek() == 1){
            second = 7 * 12 * 60 * 60 * 1000;
        }
        long triggerAtTime = SystemClock.elapsedRealtime()+second;
        Intent i = new Intent(this,RefreshWidget.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
