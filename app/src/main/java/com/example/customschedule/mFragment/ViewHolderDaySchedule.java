package com.example.customschedule.mFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.example.customschedule.R;

import org.w3c.dom.Text;

/**
 * Created by hyt on 2018/2/26.
 */

public class ViewHolderDaySchedule extends RecyclerView.ViewHolder {

    public TextView clsName,clsSite,clsNumber;
    public TimelineView mTimelineView;

    public ViewHolderDaySchedule(View itemView ,int viewType){
        super(itemView);
        clsName = (TextView)itemView.findViewById(R.id.text_timeline_clsName);
        clsNumber = (TextView) itemView.findViewById(R.id.text_timeline_clsNumber);
        clsSite = (TextView) itemView.findViewById(R.id.text_timeline_clsSite);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
        mTimelineView.initLine(viewType);
    }
}
