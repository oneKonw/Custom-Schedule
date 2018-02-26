package com.example.customschedule.mFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customschedule.Util.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;
import com.example.customschedule.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by hyt on 2018/2/26.
 */

public class AdapterDaySchedule extends RecyclerView.Adapter<ViewHolderDaySchedule>{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<HashMap<String,Object>> listItem;

    public AdapterDaySchedule(List<HashMap<String,Object>> listItem){
        this.listItem = listItem;
    }


    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public ViewHolderDaySchedule onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = mLayoutInflater.from(mContext);
        //因为只要用到垂直布局，不需要判断
        View view = mLayoutInflater.inflate(R.layout.item_tab_dayschedule,parent,false);
        return  new ViewHolderDaySchedule(view,viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolderDaySchedule holder, int position) {
        //获取数据

        //状态直接用Activity的
        holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        //显示内容
        holder.clsName.setText((String)listItem.get(position).get("clsName"));
        holder.clsNumber.setText((String)listItem.get(position).get("clsNumber"));
        holder.clsSite.setText((String)listItem.get(position).get("clsSite"));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }
}
